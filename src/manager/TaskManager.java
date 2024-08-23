package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.Status;

import java.util.HashMap;
import java.util.ArrayList;


public class TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int idCounter = 0;

    // Генерация  идентификатора
    private int generateId() {
        return ++idCounter;
    }


    // Создание задачи , эпика , подзадачи
    public void addTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    public void addSubtask(Subtask subtask) {
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);

        // Добавление подзадачи в  эпик
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(subtask);
            epic.updateStatus(); // Обновление статуса эпика после добавления подзадачи
        }
    }

    // Получение задачи по идентификатору
    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    // Получение списка всех задач
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // Удаление задачи по идентификатору
    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void removeEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
            }
        }
    }

    public void removeSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.getSubtasks().remove(subtask);
                epic.updateStatus(); // Обновление статуса эпика после удаления подзадачи
            }
        }
    }

    // Обновление задачи
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            epic.updateStatus(); // Обновление статуса эпика после обновления
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.updateStatus(); // Обновление статуса эпика после обновления подзадачи
            }
        }
    }

    // Удаление всех задач
    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllEpics() {
        epics.clear();
        subtasks.clear(); // Также удаляем все подзадачи
    }

    public void removeAllSubtasks() {
        subtasks.clear();
        // Обновляем статусы эпиков
        for (Epic epic : epics.values()) {
            epic.updateStatus();
        }
    }

    // Получение всех подзадач эпика
    public ArrayList<Subtask> getSubtasksOfEpic(int epicId) {
        Epic epic = epics.get(epicId);
        return (epic != null) ? epic.getSubtasks() : null;
    }
}