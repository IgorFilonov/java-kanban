package Server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class BaseHttpHandler {
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
}
