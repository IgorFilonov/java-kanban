package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tasks.Subtask;
import manager.TaskManager;

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
        String path = exchange.getRequestURI().getPath();

        try {
            switch (method) {
                case "GET":
                    if ("/subtasks".equals(path)) {
                        sendText(exchange, gson.toJson(taskManager.getAllSubtasks()), 200);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case "POST":
                    if ("/subtasks".equals(path)) {
                        Subtask subtask = gson.fromJson(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8), Subtask.class);
                        taskManager.createSubtask(subtask);
                        sendText(exchange, gson.toJson(subtask), 201);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case "DELETE":
                    if ("/subtasks".equals(path)) {
                        taskManager.deleteAllTasks();
                        sendText(exchange, "{\"message\":\"All subtasks deleted\"}", 200);
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