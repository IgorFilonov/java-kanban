
import com.google.gson.Gson;
import manager.*;
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
        Task task = new Task("Тест 2", "Тестовое задание 2", Duration.ofMinutes(5), LocalDateTime.now());
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
        assertEquals("Тест 2", manager.getAllTasks().get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        // Подготавливаем менеджер с одной задачей
        Task task = new Task("Существующая задача", "This is an existing task", Duration.ofMinutes(10), LocalDateTime.now());
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
        assertEquals("Существующая задача", tasks[0].getName(), "Имя задачи не соответствует ожидаемому");
    }

    @Test
    public void testDeleteAllTasks() throws IOException, InterruptedException {
        // Создаём задачу и добавляем её в менеджер
        Task task = new Task("Задача на удаление", "Эта задача будет удалена", Duration.ofMinutes(5), LocalDateTime.now());
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
        // Удаляем все задачи для избежания пересечений
        manager.deleteAllTasks();

        // Создаем и добавляем задачу с уникальным временем начала
        Task task = new Task("Обновление теста", "Тестирование обновляется", Duration.ofMinutes(10), LocalDateTime.now().plusHours(1));
        manager.createTask(task);
        int taskId = manager.getAllTasks().get(0).getId();  // Получаем ID созданной задачи
        task.setId(taskId);  // Устанавливаем ID для корректного обновления

        // Изменяем имя задачи
        task.setName("Обновленное имя задачи");
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
        assertEquals("Обновленное имя задачи", manager.getTask(taskId).getName(), "Имя задачи не было обновлено");


    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        // Подготавливаем задачу
        Task task = new Task("Задача по ID", "Получить эту задачу по ID", Duration.ofMinutes(10), LocalDateTime.now().plusHours(2));
        manager.createTask(task);
        int taskId = manager.getAllTasks().get(0).getId();

        // Создаём HTTP-запрос для получения задачи по ID
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем успешный статус ответа
        assertEquals(200, response.statusCode(), "Некорректный статус ответа");

        // Проверяем, что полученная задача соответствует ожидаемой
        Task retrievedTask = gson.fromJson(response.body(), Task.class);
        assertEquals("Задача по ID", retrievedTask.getName(), "Имя задачи не соответствует ожидаемому");
    }

    @Test
    public void testGetNonExistentTaskById() throws IOException, InterruptedException {
        // Создаём HTTP-запрос для получения задачи с несуществующим ID
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/999"); // ID 999 не существует
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем статус 404
        assertEquals(404, response.statusCode(), "Ожидаемый статус 404 для несуществующей задачи");
    }

    @Test
    public void testAddConflictingTask() throws IOException, InterruptedException {
        // Добавляем первую задачу
        Task task1 = new Task("Базовая задача", "Это основная задача", Duration.ofMinutes(30), LocalDateTime.now().plusHours(1));
        manager.createTask(task1);

        // Создаём конфликтующую задачу, которая пересекается с первой
        Task conflictingTask = new Task("Конфликтующая задача", "Эта задача будет перекрываться", Duration.ofMinutes(30), task1.getStartTime());
        String conflictingTaskJson = gson.toJson(conflictingTask);

        // Отправляем POST-запрос для добавления конфликтующей задачи
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(conflictingTaskJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем, что статус ответа — 409 Conflict
        assertEquals(409, response.statusCode(), "Ожидаемый статус 409 для конфликтующей задачи");

        // Проверяем, что конфликтующая задача не была добавлена в менеджер
        assertEquals(1, manager.getAllTasks().size(), "Конфликтующая задача не должна была быть добавлена");
    }
}