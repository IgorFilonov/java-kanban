

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import manager.InMemoryTaskManager;
import tasks.Task;


import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private InMemoryTaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void testAddingAndRetrievingTasks() {
        Task task = new Task("Задача", "Описание");
        taskManager.createTask(task);

        assertEquals(task, taskManager.getTask(task.getId()), "Задача должна добавляться и находиться по id.");
    }

    @Test
    void testNoConflictBetweenGeneratedAndAssignedIds() {
        Task task1 = new Task("Задача 1", "Описание 1");
        task1.setId(1);
        taskManager.createTask(task1);

        Task task2 = new Task("Задача 2", "Описание 2");
        taskManager.createTask(task2);

        assertNotEquals(task1.getId(), task2.getId(), "Сгенерированный id не должен конфликтовать с заданным id.");
    }

    @Test
    void testTaskUnchangedWhenAddedToManager() {
        Task task = new Task("Задача", "Описание");
        taskManager.createTask(task);

        Task retrievedTask = taskManager.getTask(task.getId());

        assertEquals(task.getName(), retrievedTask.getName(), "Имя задачи должно совпадать.");
        assertEquals(task.getDescription(), retrievedTask.getDescription(), "Описание задачи должно совпадать.");
        assertEquals(task.getStatus(), retrievedTask.getStatus(), "Статус задачи должен совпадать.");
    }
}
