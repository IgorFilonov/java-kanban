package Server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.*;
import manager.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public TaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            switch (method) {
                case "GET":
                    if ("/tasks".equals(path)) {
                        List<Task> tasks = taskManager.getAllTasks();
                        sendText(exchange, gson.toJson(tasks), 200);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case "POST":
                    if ("/tasks".equals(path)) {
                        try {
                            Task task = gson.fromJson(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8), Task.class);
                            taskManager.createTask(task);
                            sendText(exchange, gson.toJson(task), 201);
                        } catch (JsonSyntaxException e) {
                            sendError(exchange, "Invalid JSON format");
                        } catch (Exception e) {
                            sendError(exchange, "An error occurred: " + e.getMessage());
                        }
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case "DELETE":
                    if ("/tasks".equals(path)) {
                        taskManager.deleteAllTasks();
                        sendText(exchange, "{\"message\":\"All tasks deleted\"}", 200);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case "PUT":
                    if (path.matches("/tasks/\\d+")) {  // Проверка на наличие ID в URL
                        int taskId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
                        Task taskToUpdate = taskManager.getTask(taskId);
                        if (taskToUpdate != null) {
                            Task updatedTask = gson.fromJson(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8), Task.class);
                            updatedTask.setId(taskId);  // Устанавливаем ID, чтобы сохранить идентичность
                            taskManager.updateTask(updatedTask);
                            sendText(exchange, gson.toJson(updatedTask), 200);
                        } else {
                            sendNotFound(exchange);  // Задача не найдена — возвращаем 404
                        }
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                default:
                    sendNotFound(exchange);
            }
        } catch (Exception e) {
            System.err.println("Error handling request: " + e.getMessage());
            sendError(exchange, "An error occurred: " + e.getMessage());
        }
    }
}