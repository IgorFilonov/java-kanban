

import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.Status;
import static org.junit.jupiter.api.Assertions.*;

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

    // Тест добавления задачи в историю, проверка уникальности и перемещения в конец
    @Test
    void testAddingToHistory() {
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История должна содержать одну задачу.");
        assertEquals(task, history.get(0), "Задача в истории должна совпадать с добавленной.");

        // Проверяем, что повторное добавление перемещает задачу в конец, без дубликатов
        historyManager.add(task);
        history = historyManager.getHistory();
        assertEquals(1, history.size(), "История должна содержать одну уникальную задачу.");
        assertEquals(task, history.get(0), "Задача должна остаться единственной.");
    }

    // Тест сохранения состояния задачи в истории
    @Test
    void testHistoryPreservesTaskState() {
        // Сохраняем ожидаемые значения до добавления задачи в историю
        String expectedName = task.getName();
        String expectedDescription = task.getDescription();
        Status expectedStatus = task.getStatus();

        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        Task historyTask = history.get(0);

        assertEquals(expectedName, historyTask.getName(), "Имя задачи должно совпадать.");
        assertEquals(expectedDescription, historyTask.getDescription(), "Описание задачи должно совпадать.");
        assertEquals(expectedStatus, historyTask.getStatus(), "Статус задачи должен совпадать.");
    }

    // Тест удаления задачи из истории
    @Test
    void testRemoveTaskFromHistory() {
        historyManager.add(task);

        // Проверяем, что задача была добавлена
        assertEquals(1, historyManager.getHistory().size(), "История должна содержать одну задачу.");

        // Удаляем задачу
        historyManager.remove(task.getId());

        // Проверяем, что задача была удалена
        assertTrue(historyManager.getHistory().isEmpty(), "История должна быть пустой после удаления задачи.");
    }

    // Тест добавления нескольких задач и проверки их порядка

}
