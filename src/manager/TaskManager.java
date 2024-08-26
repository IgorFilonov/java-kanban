package manager;

import tasks.Task;
import tasks.Epic;
import tasks.Subtask;

import java.util.List;


public interface TaskManager {

    // Получение списков
    List<Task> getAllTasks();
    List<Epic> getAllEpics();
    List<Subtask> getAllSubtasks();

    // Получение  по идентификатору
    Task getTask(int id);
    Epic getEpic(int id);
    Subtask getSubtask(int id);

    // Удаление всех задач
    void deleteAllTasks();
    void deleteTaskById(int id);
    void deleteEpicById(int id);
    void deleteSubtaskById(int id);

    // Обновление
    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubtask(Subtask subtask);

    // Создание
    void createTask(Task task);
    void createEpic(Epic epic);
    void createSubtask(Subtask subtask);

    // Получение истории последних просмотренных задач
    List<Task> getHistory();
}
