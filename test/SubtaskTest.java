
import org.junit.jupiter.api.Test;
import tasks.*;

import static org.junit.jupiter.api.Assertions.*;


import java.time.Duration;
import java.time.LocalDateTime;

class SubtaskTest {

    @Test
    void testEpicIdAssignment() {
        Subtask subtask = new Subtask("Subtask 1", "Description", Duration.ofHours(1), LocalDateTime.now(), 1);
        assertEquals(1, subtask.getEpicId());
    }

    @Test
    void testSubtaskStatus() {
        Subtask subtask = new Subtask("Subtask", "Desc", Duration.ofHours(1), LocalDateTime.now(), 1);
        subtask.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, subtask.getStatus());
    }
}
