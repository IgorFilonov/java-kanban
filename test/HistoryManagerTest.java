import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import manager.*;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;

class HistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void testEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty(), "История должна быть пустой");
    }

    @Test
    void testAddDuplicate() {
        Task task = new Task("Task 1", "Description", Duration.ofHours(1), LocalDateTime.now());
        task.setId(1);
        historyManager.add(task);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "Дубликаты не должны добавляться в историю");
    }

    @Test
    void testRemoveFromHistory() {
        Task task1 = new Task("Task 1", "Description", Duration.ofHours(1), LocalDateTime.now());
        task1.setId(1);
        Task task2 = new Task("Task 2", "Description", Duration.ofHours(1), LocalDateTime.now());
        task2.setId(2);
        Task task3 = new Task("Task 3", "Description", Duration.ofHours(1), LocalDateTime.now());
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task1.getId());
        historyManager.remove(task2.getId());
        historyManager.remove(task3.getId());

        assertTrue(historyManager.getHistory().isEmpty(), "История должна быть пустой после удаления всех задач");
    }
}