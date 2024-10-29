
import org.junit.jupiter.api.BeforeEach;
import manager.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager(); // Инициализируем InMemoryTaskManager
    }
}


