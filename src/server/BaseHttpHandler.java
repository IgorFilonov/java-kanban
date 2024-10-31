package server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class BaseHttpHandler {

    protected Integer getIdFromPath(String path) {
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

    protected void sendText(HttpExchange exchange, String text, int code) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(code, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        sendText(exchange, "{\"error\":\"Resource not found\"}", 404);
    }

    protected void sendError(HttpExchange exchange, String errorMessage) throws IOException {
        System.err.println("Error: " + errorMessage);  // Логирование ошибки в консоль
        byte[] resp = ("{\"error\":\"" + errorMessage + "\"}").getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(500, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
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
