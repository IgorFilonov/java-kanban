package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Subtask;
import manager.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        Integer idFromRequest = getIdFromPath(exchange.getRequestURI().getPath());

        try {
            switch (method) {
                case "GET": {
                    if (idFromRequest == null) {
                        sendText(exchange, gson.toJson(taskManager.getAllSubtasks()), 200);
                    } else {
                        Subtask subtask = taskManager.getSubtask(idFromRequest);
                        if (subtask != null) {
                            sendText(exchange, gson.toJson(subtask), 200);
                        } else {
                            sendNotFound(exchange);
                        }
                    }
                    break;
                }
                case "POST": {
                    Subtask subtask = gson.fromJson(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8), Subtask.class);
                    try {
                        taskManager.createSubtask(subtask);
                        sendText(exchange, gson.toJson(subtask), 201);
                    } catch (TaskValidationException e) {
                        System.out.println("Подзадача пересекается с существующей");
                        sendHasInteractions(exchange); // Отправляем код 409
                    }
                    break;
                }
                case "PUT": {
                    if (idFromRequest != null) {
                        Subtask subtaskToUpdate = gson.fromJson(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8), Subtask.class);
                        subtaskToUpdate.setId(idFromRequest);
                        try {
                            taskManager.updateSubtask(subtaskToUpdate);
                            sendText(exchange, gson.toJson(subtaskToUpdate), 200);
                        } catch (TaskValidationException e) {
                            System.out.println("Подзадача пересекается с существующей");
                            sendHasInteractions(exchange); // Отправляем код 409
                        }
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                }
                case "DELETE": {
                    if (idFromRequest == null) {
                        taskManager.deleteAllTasks();
                        sendText(exchange, "{\"message\":\"Все подзадачи удалены\"}", 200);
                    } else {
                        taskManager.deleteSubtaskById(idFromRequest);
                        sendText(exchange, "{\"message\":\"Подзадача удалена\"}", 200);
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
}