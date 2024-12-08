package manager;

import tasks.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    public FileBackedTaskManager(File file) {
        this.file = file;

    }

    // Метод сохранения
    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,duration,startTime,epic\n");
            for (Task task : getAllTasks()) {
                writer.write(toString(task) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл", e);
        }
    }

    // Преобразование задачи в строку
    private String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",")
                .append(task.getType()).append(",")
                .append(task.getName()).append(",")
                .append(task.getStatus()).append(",")
                .append(task.getDescription()).append(",")
                .append(task.getDuration() != null ? task.getDuration().toMinutes() : "").append(",")
                .append(task.getStartTime() != null ? task.getStartTime().format(DATE_FORMATTER) : "");

        if (task instanceof Subtask) {
            sb.append(",").append(((Subtask) task).getEpicId());
        }

        return sb.toString();
    }

    // Восстановление задачи из строки
    private Task fromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String name = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];
        Duration duration = !fields[5].isEmpty() ? Duration.ofMinutes(Long.parseLong(fields[5])) : null;
        LocalDateTime startTime = !fields[6].isEmpty() ? LocalDateTime.parse(fields[6], DATE_FORMATTER) : null;

        Task task;
        switch (type) {
            case TASK:
                task = new Task(name, description, duration, startTime);
                break;
            case EPIC:
                task = new Epic(name, description);
                break;
            case SUBTASK:
                int epicId = Integer.parseInt(fields[7]);
                task = new Subtask(name, description, duration, startTime, epicId);
                break;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
        task.setId(id);
        task.setStatus(status);
        return task;
    }

    // Загрузка данных из файла
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Пропускаем заголовок
            while ((line = reader.readLine()) != null) {
                Task task = manager.fromString(line);
                if (task != null) {
                    manager.addTaskToManager(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения из файла", e);
        }

        return manager;
    }

    private void addTaskToManager(Task task) {
        if (task instanceof Epic) {
            createEpic((Epic) task);
        } else if (task instanceof Subtask) {
            createSubtask((Subtask) task);
        } else {
            createTask(task);
        }
    }


    // Переопределение методов для автосохранения
    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    //Проверка на пересечения


    @Override
    public void updateSubtask(Subtask subtask) {
        validateOverlap(subtask);
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }
}
