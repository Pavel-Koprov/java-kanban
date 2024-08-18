package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import http.Endpoint;
import http.HttpTaskServer;
import service.TaskManager;
import java.io.IOException;
public class UserHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public UserHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_HISTORY:
                handleGetHistory(exchange);
                break;
            case GET_PRIORITIZED:
                handleGetPrioritized(exchange);
                break;
            case UNKNOWN:
                sendText(exchange, 400, "Во время выполнения запроса возникла ошибка. " +
                        "Проверьте, пожалуйста, параметры запроса и повторите попытку.");
                break;
        }
    }

    private void handleGetHistory(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getHistory());
        sendText(exchange, 200, response);
    }

    private void handleGetPrioritized(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getPrioritizedTasks());
        sendText(exchange, 200, response);
    }
}
