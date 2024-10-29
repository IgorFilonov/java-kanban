import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;
import manager.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;

    @BeforeEach
    abstract void setUp(); // Инициализация менеджера в конкретных тестах

    @Test
    void testCreateAndGetTask() {
        Task task = new Task("Task 1", "Description", Duration.ofHours(1), LocalDateTime.now());
        manager.createTask(task);

        Task retrievedTask = manager.getTask(task.getId());
        assertNotNull(retrievedTask, "Задача должна быть сохранена");
        assertEquals(task, retrievedTask, "Созданная и полученная задача должны совпадать");
    }

    @Test
    void testDeleteTaskById() {
        Task task = new Task("Task 1", "Description", Duration.ofHours(1), LocalDateTime.now());
        manager.createTask(task);
        manager.deleteTaskById(task.getId());

        assertNull(manager.getTask(task.getId()), "Задача должна быть удалена");
    }

    @Test
    void testEpicStatusCalculation() {
        Epic epic = new Epic("Epic", "Description");
        manager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", Duration.ofHours(1), LocalDateTime.now(), epic.getId());
        manager.createSubtask(subtask1);
        assertEquals(Status.NEW, manager.getEpic(epic.getId()).getStatus());

        // Меняем статус подзадачи и обновляем в менеджере
        subtask1.setStatus(Status.DONE);
        manager.updateSubtask(subtask1);

        // Проверяем статус эпика
        assertEquals(Status.DONE, manager.getEpic(epic.getId()).getStatus());
    }
    @Test
    void testHistory() {
        Task task1 = new Task("Task 1", "Desc", Duration.ofHours(1), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Desc", Duration.ofHours(1), LocalDateTime.now().plusHours(1)); // Добавляем интервал

        manager.createTask(task1);
        manager.createTask(task2);

        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        List<Task> history = manager.getHistory();

        assertEquals(2, history.size(), "История должна содержать 2 задачи");
    }
}



