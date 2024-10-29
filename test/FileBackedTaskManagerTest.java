import org.junit.jupiter.api.BeforeEach;

import java.io.File;

import manager.FileBackedTaskManager;


class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private static final String FILE_PATH = "tasks_test.csv"; // Временный файл для тестирования

    @BeforeEach
    void setUp() {
        manager = new FileBackedTaskManager(new File(FILE_PATH)); // Инициализируем FileBackedTaskManager
    }
}

