package server;

import com.google.gson.Gson;
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

    private Integer getIdFromPath(String path) {
        String[] parts = path.split("/");
        if (parts.length == 3) {
            try {
                return Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                return null; // Если id не является числом
            }
        }
        return null; // Если путь не содержит id
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        Integer idFromRequest = getIdFromPath(exchange.getRequestURI().getPath());

        try {
            switch (method) {
                case "GET": {
                    if (idFromRequest == null) {
                        List<Task> tasks = taskManager.getAllTasks();
                        sendText(exchange, gson.toJson(tasks), 200);
                        System.out.println("Получили все задачи");
                    } else {
                        Task task = taskManager.getTask(idFromRequest);
                        if (task != null) {
                            sendText(exchange, gson.toJson(task), 200);
                            System.out.println("Получили задачу id=" + idFromRequest);
                        } else {
                            sendNotFound(exchange);
                            System.out.println("Задача с id=" + idFromRequest + " не найдена");
                        }
                    }
                    break;
                }
                case "POST": {
                    Task task = gson.fromJson(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8), Task.class);
                    try {
                        taskManager.createTask(task);
                        sendText(exchange, gson.toJson(task), 201);
                    } catch (TaskValidationException e) {
                        System.out.println("Задача пересекается с существующей");
                        sendHasInteractions(exchange); // Специальный код для конфликтов задач
                    }
                    break;
                }
                case "PUT": {
                    if (idFromRequest != null) {
                        Task taskToUpdate = gson.fromJson(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8), Task.class);
                        taskToUpdate.setId(idFromRequest);
                        try {
                            taskManager.updateTask(taskToUpdate);
                            sendText(exchange, gson.toJson(taskToUpdate), 200);
                        } catch (TaskValidationException e) {
                            System.out.println("Задача пересекается с существующей");
                            sendHasInteractions(exchange); // Специальный код для конфликтов задач
                        }
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                }
                case "DELETE": {
                    if (idFromRequest == null) {
                        taskManager.deleteAllTasks();
                        sendText(exchange, "{\"message\":\"Все задачи удалены\"}", 200);
                    } else {
                        taskManager.deleteTaskById(idFromRequest);
                        sendText(exchange, "{\"message\":\"Задача удалена\"}", 200);
                    }
                    break;
                }
                default:
                    sendNotFound(exchange);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при обработке запроса: " + e.getMessage());
            sendError(exchange, "Произошла ошибка: " + e.getMessage());
        }
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        String response = "{\"error\":\"Задача пересекается с другой задачей\"}";
        byte[] resp = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(409, resp.length); // Код 409 для конфликта
        exchange.getResponseBody().write(resp);
        exchange.close();
    }
}