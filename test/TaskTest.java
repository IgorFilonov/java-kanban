import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;


class TaskTest {

    @Test
    void testEndTimeCalculation() {
        LocalDateTime startTime = LocalDateTime.now();
        Task task = new Task("Test Task", "Description", Duration.ofHours(2), startTime);
        assertEquals(startTime.plusHours(2), task.getEndTime());
    }

    @Test
    void testIsOverlapping() {
        LocalDateTime now = LocalDateTime.now();
        Task task1 = new Task("Task 1", "Desc", Duration.ofHours(1), now);
        Task task2 = new Task("Task 2", "Desc", Duration.ofHours(1), now.plusMinutes(30));
        assertTrue(task1.isOverlapping(task2));

        Task task3 = new Task("Task 3", "Desc", Duration.ofHours(1), now.plusHours(2));
        assertFalse(task1.isOverlapping(task3));
    }
}