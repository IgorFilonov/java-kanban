
import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.*;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTasksTest {

    private TaskManager manager;
    private HttpTaskServer taskServer;
    private Gson gson = HttpTaskServer.getGson();

    @BeforeEach
    public void setUp() throws IOException {
        manager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(manager);
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        // Создаём задачу
        Task task = new Task("Test 2", "Testing task 2", Duration.ofMinutes(5), LocalDateTime.now());
        String taskJson = gson.toJson(task);

        // Создаём HTTP-запрос для добавления задачи
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем успешный статус ответа
        assertEquals(201, response.statusCode(), "Некорректный статус ответа");

        // Проверяем, что задача была добавлена в менеджер
        assertEquals(1, manager.getAllTasks().size(), "Задача не была добавлена в менеджер");
        assertEquals("Test 2", manager.getAllTasks().get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        // Подготавливаем менеджер с одной задачей
        Task task = new Task("Existing Task", "This is an existing task", Duration.ofMinutes(10), LocalDateTime.now());
        manager.createTask(task);

        // Создаём HTTP-запрос для получения задач
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем успешный статус ответа
        assertEquals(200, response.statusCode(), "Некорректный статус ответа");

        // Проверяем, что ответ содержит корректное количество задач
        Task[] tasks = gson.fromJson(response.body(), Task[].class);
        assertEquals(1, tasks.length, "Некорректное количество задач в ответе");
        assertEquals("Existing Task", tasks[0].getName(), "Имя задачи не соответствует ожидаемому");
    }

    @Test
    public void testDeleteAllTasks() throws IOException, InterruptedException {
        // Создаём задачу и добавляем её в менеджер
        Task task = new Task("Task to delete", "This task will be deleted", Duration.ofMinutes(5), LocalDateTime.now());
        manager.createTask(task);

        // Отправляем DELETE-запрос для удаления всех задач
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем успешный статус ответа
        assertEquals(200, response.statusCode(), "Некорректный статус ответа");

        // Проверяем, что все задачи были удалены
        assertEquals(0, manager.getAllTasks().size(), "Задачи не были удалены");
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        // Создаем и добавляем задачу
        Task task = new Task("Update Test", "Testing update", Duration.ofMinutes(10), LocalDateTime.now());
        manager.createTask(task);
        int taskId = manager.getAllTasks().get(0).getId();  // Получаем ID созданной задачи
        task.setId(taskId);  // Устанавливаем ID для корректного обновления

        // Изменяем имя задачи
        task.setName("Updated Task Name");
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + taskId); // Добавляем ID в URL
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .PUT(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Ожидаемый статус 200 при обновлении задачи");

        // Проверяем, что имя задачи было обновлено в менеджере
        assertEquals("Updated Task Name", manager.getTask(taskId).getName(), "Имя задачи не было обновлено");
    }
}