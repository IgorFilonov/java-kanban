package manager;

import tasks.Task;

import java.util.List;


public interface HistoryManager {
    void add(Task task);   // Добавление задачи в историю
    void remove(int id);   // Удаление задачи по её id
    List<Task> getHistory();  // Получение истории просмотров
}
