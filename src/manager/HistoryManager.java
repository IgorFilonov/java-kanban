package manager;

import tasks.Task;

import java.util.List;


public interface HistoryManager {
    void add(Task task); // Добавление задачи в историю просмотров
    List<Task> getHistory(); //  список последних просмотренных задач
}
