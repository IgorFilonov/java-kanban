package test;

import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {

    @Test
    void testTasksEqualityById() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        task1.setId(1);
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        task2.setId(1);

        assertEquals(task1, task2, "Задачи с одинаковым id должны быть равны.");
    }
}
