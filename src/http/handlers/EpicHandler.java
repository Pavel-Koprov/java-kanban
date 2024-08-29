package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import exceptions.TimeConflictException;
import http.Endpoint;
import http.HttpTaskServer;
import service.TaskManager;
import dto.Epic;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_EPICS:
                handleGetEpics(exchange);
                break;
            case GET_EPIC_BY_ID:
                handleGetEpicById(exchange);
                break;
            case GET_EPIC_SUBTASKS_BY_ID:
                handleGetEpicSubtasksById(exchange);
                break;
            case POST_EPIC:
                handleCreateOrUpdateEpic(exchange);
                break;
            case DELETE_EPIC_BY_ID:
                handleDeleteEpicById(exchange);
                break;
            case UNKNOWN:
                sendText(exchange, 400, "Во время выполнения запроса возникла ошибка. " +
                        "Проверьте, пожалуйста, параметры запроса и повторите попытку.");
                break;
        }
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        String response = gson.toJson(taskManager.getEpics());
        sendText(exchange, 200, response);
    }

    private void handleGetEpicById(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> epicIdOpt = getId(exchange);

            if (epicIdOpt.isEmpty()) {
                sendText(exchange, 400, "Некорректный идентификатор");
                return;
            }
            int epicId = epicIdOpt.get();
            Epic epic = taskManager.findEpic(epicId);

            if (epic == null) {
                sendText(exchange, 404, String.format("Эпик с идентификатором %d не существует", epicId));
                return;
            }
            String response = gson.toJson(epic);
            sendText(exchange, 200, response);
        } catch (NotFoundException e) {
            sendText(exchange, 404, e.getMessage());
        }
    }

    private void handleGetEpicSubtasksById(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> epicIdOpt = getId(exchange);

            if (epicIdOpt.isEmpty()) {
                sendText(exchange, 400, "Некорректный идентификатор эпика");
                return;
            }
            int epicId = epicIdOpt.get();
            Epic epic = taskManager.findEpic(epicId);

            if (epic == null) {
                sendText(exchange, 404, String.format("Эпик с идентификатором %d не найден", epicId));
                return;
            }
            String response = gson.toJson(taskManager.getEpicSubtasks(epicId));
            sendText(exchange, 200, response);
        } catch (NotFoundException e) {
            sendText(exchange, 404, e.getMessage());
        }
    }

    void handleCreateOrUpdateEpic(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            Optional<Epic> optionalEpic = parseEpic(inputStream);
            if (optionalEpic.isEmpty()) {
                sendText(exchange, 400, "Поля эпика не могут быть пустыми");
                return;
            }
            Epic epic = optionalEpic.get();
            try {
                if (epic.getTaskId() != 0) {
                    taskManager.updateEpic(epic);
                    sendText(exchange, 200, "Эпик был успешно обновлен");
                    return;
                }
                taskManager.saveEpic(epic);
                sendText(exchange, 201, "Эпик был успешно создан");
            } catch (TimeConflictException e) {
                sendText(exchange, 406, "Эпик не может быть добавлен, " +
                        "так как пересекается с другой задачей");
            }
        }
    }

    private void handleDeleteEpicById(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> epicIdOpt = getId(exchange);

            if (epicIdOpt.isEmpty()) {
                sendText(exchange, 400, "Некорректный идентификатор");
                return;
            }
            int epicId = epicIdOpt.get();
            Epic epic = taskManager.findEpic(epicId);

            if (epic == null) {
                sendText(exchange, 404, String.format("Эпик с идентификатором %d не найден", epicId));
                return;
            }
            taskManager.removeEpic(epicId);
            sendText(exchange, 200, "Эпик был успешно удален");
        } catch (NotFoundException e) {
            sendText(exchange, 404, e.getMessage());
        }
    }

    private Optional<Epic> parseEpic(InputStream bodyInputStream) throws IOException {
        String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8);
        if (!body.contains("taskName") || !body.contains("taskDescription")) {
            return Optional.empty();
        }
        Epic epic = gson.fromJson(body, Epic.class);
        return Optional.of(epic);
    }
}
