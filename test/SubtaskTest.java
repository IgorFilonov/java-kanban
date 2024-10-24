
import org.junit.jupiter.api.Test;
import tasks.Subtask;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void testSubtasksEqualityById() {
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 1);
        subtask1.setId(1);
        Subtask subtask2 = new Subtask("Подзадача 1", "Описание подзадачи 2", 1);
        subtask2.setId(1);

        assertEquals(subtask1, subtask2, "Подзадачи с одинаковым id должны быть равны.");
    }


}
