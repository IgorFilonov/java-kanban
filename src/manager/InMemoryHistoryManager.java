package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>(); // Список для хранения истории последних 10 просмотров

    @Override
    public void add(Task task) {
        if (history.size() == 10) {
            history.remove(0); // Если в истории уже 10 задач, удаляем самую старую
        }
        history.add(task); // Добавляем новую задачу в конец истории
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history); // Возвращаем копию списка с историей просмотров
    }
}
