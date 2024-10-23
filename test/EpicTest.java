

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;

import static org.junit.jupiter.api.Assertions.*;

 class EpicTest {

    @Test
    void testEpicsEqualityById() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        epic1.setId(1);
        Epic epic2 = new Epic("Эпик 2", "Описание 2");
        epic2.setId(1);

        assertEquals(epic1, epic2, "Эпики с одинаковым id должны быть равны.");
    }


}
