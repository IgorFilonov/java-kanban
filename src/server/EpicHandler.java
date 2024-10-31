package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Epic;
import manager.TaskManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        Integer idFromRequest = getIdFromPath(exchange.getRequestURI().getPath());

        try {
            switch (method) {
                case "GET":
                    if (idFromRequest == null) {
                        sendText(exchange, gson.toJson(taskManager.getAllEpics()), 200);
                    } else {
                        Epic epic = taskManager.getEpic(idFromRequest);
                        if (epic != null) {
                            sendText(exchange, gson.toJson(epic), 200);
                        } else {
                            sendNotFound(exchange);
                        }
                    }
                    break;
                case "POST":
                    Epic epic = gson.fromJson(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8), Epic.class);
                    taskManager.createEpic(epic);
                    sendText(exchange, gson.toJson(epic), 201);
                    break;
                case "DELETE":
                    if (idFromRequest == null) {
                        taskManager.deleteAllTasks();
                        sendText(exchange, "{\"message\":\"Все эпики удалены\"}", 200);
                    } else {
                        taskManager.deleteEpicById(idFromRequest);
                        sendText(exchange, "{\"message\":\"Эпик удален\"}", 200);
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