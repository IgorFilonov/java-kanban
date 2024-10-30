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
        String path = exchange.getRequestURI().getPath();

        try {
            switch (method) {
                case "GET":
                    if ("/epics".equals(path)) {
                        sendText(exchange, gson.toJson(taskManager.getAllEpics()), 200);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case "POST":
                    if ("/epics".equals(path)) {
                        Epic epic = gson.fromJson(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8), Epic.class);
                        taskManager.createEpic(epic);
                        sendText(exchange, gson.toJson(epic), 201);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case "DELETE":
                    if ("/epics".equals(path)) {
                        taskManager.deleteAllTasks();
                        sendText(exchange, "{\"message\":\"All epics deleted\"}", 200);
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