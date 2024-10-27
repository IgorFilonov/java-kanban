import static org.junit.jupiter.api.Assertions.*;

import manager.FileBackedTaskManager;
import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import tasks.Task;
import tasks.Epic;
import tasks.Subtask;

class FileBackedTaskManagerTest {
    private File file;
    private FileBackedTaskManager manager;

    @BeforeEach
    void setUp() throws Exception {
        file = Files.createTempFile("test", ".csv").toFile();
        manager = new FileBackedTaskManager(file);
    }

    @AfterEach
    void tearDown() {
        file.delete();
    }

    @Test
    void saveAndLoadTasks() {
        // Создаем задачи и сохраняем их
        manager.createTask(new Task("Задача 1", "Описание 1"));
        manager.createEpic(new Epic("Эпик 1", "Описание 1"));
        manager.createSubtask(new Subtask("Подзадача 1", "Описание 1", 1));

        // Загружаем из файла и проверяем
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        List<Task> tasks = loadedManager.getAllTasks();
        Assertions.assertEquals(1, tasks.size());
        Assertions.assertEquals("Задача 1", tasks.get(0).getName());
    }
}