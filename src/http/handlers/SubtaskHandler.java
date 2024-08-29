package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import exceptions.TimeConflictException;
import http.Endpoint;
import http.HttpTaskServer;
import service.TaskManager;
import dto.Subtask;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
public class SubtaskHandler extends BaseHttpHandler {

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_SUBTASKS:
                handleGetSubtasks(exchange);
                break;
            case GET_SUBTASK_BY_ID:
                handleGetSubtaskById(exchange);
                break;
            case POST_SUBTASK:
                handleCreateOrUpdateSubtask(exchange);
                break;
            case DELETE_SUBTASK_BY_ID:
                handleDeleteSubtaskById(exchange);
                break;
            case UNKNOWN:
                sendText(exchange, 400, "Во время выполнения запроса возникла ошибка. " +
                        "Проверьте, пожалуйста, параметры запроса и повторите попытку.");
                break;
        }
    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getSubtasks());
        sendText(exchange, 200, response);
    }

    private void handleGetSubtaskById(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> subtaskIdOpt = getId(exchange);

            if (subtaskIdOpt.isEmpty()) {
                sendText(exchange, 400, "Некорректный идентификатор подзадачи");
                return;
            }
            int subtaskId = subtaskIdOpt.get();
            Subtask subtask = taskManager.findSubtask(subtaskId);

            if (subtask == null) {
                sendText(exchange, 404, String.format("Подзадача с идентификатором %d не найдена", subtaskId));
                return;
            }
            String response = gson.toJson(subtask);
            sendText(exchange, 200, response);
        } catch (NotFoundException e) {
            sendText(exchange, 404, e.getMessage());
        }
    }

    void handleCreateOrUpdateSubtask(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            Optional<Subtask> optionalSubtask = parseSubtask(inputStream);

            if (optionalSubtask.isEmpty()) {
                sendText(exchange, 400, "Поля подзадачи не могут быть пустыми");
                return;
            }
            Subtask subtask = optionalSubtask.get();

            try {
                if (subtask.getTaskId() != 0) {
                    taskManager.updateSubtask(subtask);
                    sendText(exchange, 200, "Подзадача была успешно обновлена");
                    return;
                }
                taskManager.saveSubtask(subtask);
                sendText(exchange, 201, "Подзадача была успешно добавлена");
            } catch (TimeConflictException e) {
                sendText(exchange, 406, "Подзадача не может быть добавлена, " +
                        "так как пересекается с другой задачей");
            }
        }
    }

    private void handleDeleteSubtaskById(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> subtaskIdOpt = getId(exchange);

            if (subtaskIdOpt.isEmpty()) {
                sendText(exchange, 400, "Некорректный идентификатор");
                return;
            }
            int subtaskId = subtaskIdOpt.get();
            Subtask subtask = taskManager.findSubtask(subtaskId);

            if (subtask == null) {
                sendText(exchange, 404, String.format("Подзадача с идентификатором %d не найдена", subtaskId));
                return;
            }
            taskManager.removeSubtask(subtaskId);
            sendText(exchange, 200, "Подзадача была успешно удалена");
        } catch (NotFoundException e) {
            sendText(exchange, 404, e.getMessage());
        }
    }

    private Optional<Subtask> parseSubtask(InputStream bodyInputStream) throws IOException {
        String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8);
        if (!body.contains("taskName") || !body.contains("taskDescription") || !body.contains("startTime")
            || !body.contains("duration") || !body.contains("epicId")) {
            return Optional.empty();
        }
        Subtask subtask = gson.fromJson(body, Subtask.class);
        return Optional.of(subtask);
    }
}
