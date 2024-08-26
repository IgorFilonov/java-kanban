package test;

import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager historyManager;
    private Task task;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task = new Task("Задача", "Описание");
    }

    @Test
    void testAddingToHistory() {
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История должна содержать одну задачу.");
        assertEquals(task, history.get(0), "Задача в истории должна совпадать с добавленной.");
    }

    @Test
    void testHistoryPreservesTaskState() {
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        Task historyTask = history.get(0);

        assertEquals(task.getName(), historyTask.getName(), "Имя задачи должно совпадать.");
        assertEquals(task.getDescription(), historyTask.getDescription(), "Описание задачи должно совпадать.");
        assertEquals(task.getStatus(), historyTask.getStatus(), "Статус задачи должен совпадать.");
    }
}
