package HTTP.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import HTTP.Endpoint;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public abstract class BaseHttpHandler implements HttpHandler {
    protected void sendText(HttpExchange exchange, int statusCode, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(statusCode, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    protected Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        String taskType = pathParts[1];

        switch (taskType) {

            case "tasks":
                if (pathParts.length == 2) {
                    if (requestMethod.equals("GET")) {
                        return Endpoint.GET_TASKS;
                    }
                    if (requestMethod.equals("POST")) {
                        return Endpoint.POST_TASK;
                    }
                }

                if (pathParts.length == 3) {
                    if (requestMethod.equals("GET")) {
                        return Endpoint.GET_TASK_BY_ID;
                    }
                    if (requestMethod.equals("DELETE")) {
                        return Endpoint.DELETE_TASK_BY_ID;
                    }
                }

            case "subtasks":
                if (pathParts.length == 2) {
                    if (requestMethod.equals("GET")) {
                        return Endpoint.GET_SUBTASKS;
                    }
                    if (requestMethod.equals("POST")) {
                        return Endpoint.POST_SUBTASK;
                    }
                }
                if (pathParts.length == 3) {
                    if (requestMethod.equals("GET")) {
                        return Endpoint.GET_SUBTASK_BY_ID;
                    }
                    if (requestMethod.equals("DELETE")) {
                        return Endpoint.DELETE_SUBTASK_BY_ID;
                    }
                }

            case "epics":
                if (pathParts.length == 2) {
                    if (requestMethod.equals("GET")) {
                        return Endpoint.GET_EPICS;
                    }
                    if (requestMethod.equals("POST")) {
                        return Endpoint.POST_EPIC;
                    }
                }
                if (pathParts.length == 3) {
                    if (requestMethod.equals("GET")) {
                        return Endpoint.GET_EPIC_BY_ID;
                    }
                    if (requestMethod.equals("DELETE")) {
                        return Endpoint.DELETE_EPIC_BY_ID;
                    }
                }
                if (pathParts.length == 4) {
                    if (requestMethod.equals("GET")) {
                        return Endpoint.GET_EPIC_SUBTASKS_BY_ID;
                    }
                }
            case "history":
                return Endpoint.GET_HISTORY;
            case "prioritized":
                return Endpoint.GET_PRIORITIZED;
            default:
                return Endpoint.UNKNOWN;
        }
    }

    protected Optional<Integer> getId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathParts[2]));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }
}
