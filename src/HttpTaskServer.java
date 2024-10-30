import com.sun.net.httpserver.HttpServer;

import manager.*;
import Server.*;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private final HttpServer server;
    private final TaskManager taskManager;

    // Инициализация Gson с зарегистрированными адаптерами для Duration и LocalDateTime
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())  // Регистрация LocalDateTimeAdapter
            .create();

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(8080), 0);
        this.taskManager = taskManager;
        createContext();
    }

    private void createContext() {
        server.createContext("/tasks", new TaskHandler(taskManager, gson));
        server.createContext("/subtasks", new SubtaskHandler(taskManager, gson));
        server.createContext("/epics", new EpicHandler(taskManager, gson));
        server.createContext("/history", new HistoryHandler(taskManager, gson));
        server.createContext("/prioritized", new PrioritizedTasksHandler(taskManager, gson));
    }

    public void start() {
        System.out.println("Starting server on port 8080...");
        server.start();
        System.out.println("Server started on port 8080");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("Server stopped");
        }
    }

    public static Gson getGson() {
        return gson;
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start();
        server.stop();
    }
}