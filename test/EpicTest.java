

import org.junit.jupiter.api.Test;
import tasks.*;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;


class EpicTest {

    @Test
    void testStatusUpdate() {
        Epic epic = new Epic("Epic", "Description");
        Subtask subtask1 = new Subtask("Subtask 1", "Desc", Duration.ofHours(1), LocalDateTime.now(), epic.getId());
        subtask1.setStatus(Status.NEW);
        epic.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask 2", "Desc", Duration.ofHours(1), LocalDateTime.now(), epic.getId());
        subtask2.setStatus(Status.DONE);
        epic.addSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void testEpicTimeCalculation() {
        Epic epic = new Epic("Epic", "Description");
        LocalDateTime startTime = LocalDateTime.now();

        Subtask subtask1 = new Subtask("Subtask 1", "Desc", Duration.ofHours(1), startTime, epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Desc", Duration.ofHours(1), startTime.plusHours(2), epic.getId());

        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        assertEquals(startTime, epic.getStartTime());
        assertEquals(startTime.plusHours(3), epic.getEndTime());
    }
}
