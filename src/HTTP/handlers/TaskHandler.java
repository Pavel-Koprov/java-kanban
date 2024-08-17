package HTTP.handlers;

import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import exceptions.TimeConflictException;
import HTTP.Endpoint;
import HTTP.HttpTaskServer;
import service.TaskManager;
import dto.Task;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class TaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {

            case GET_TASKS:
                handleGetTasks(exchange);
                break;
            case GET_TASK_BY_ID:
                handleGetTaskById(exchange);
                break;
            case POST_TASK:
                handleCreateOrUpdateTask(exchange);
                break;
            case DELETE_TASK_BY_ID:
                handleDeleteTaskById(exchange);
                break;
            case UNKNOWN:
                sendText(exchange, 400, "Во время выполнения запроса возникла ошибка. " +
                        "Проверьте, пожалуйста, параметры запроса и повторите попытку.");
                break;
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        String response = HttpTaskServer.getGson().toJson(taskManager.getTasks());
        sendText(exchange, 200, response);
    }

    private void handleGetTaskById(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> taskIdOpt = getId(exchange);

            if (taskIdOpt.isEmpty()) {
                sendText(exchange, 400, "Некорректный идентификатор задачи");
                return;
            }
            int taskId = taskIdOpt.get();
            Task task = taskManager.findTask(taskId);

            if (task == null) {
                sendText(exchange, 404, String.format("Задача с идентификатором %d не найдена", taskId));
                return;
            }
            String response = HttpTaskServer.getGson().toJson(task);
            sendText(exchange, 200, response);
        } catch (NotFoundException e) {
            sendText(exchange, 404, e.getMessage());
        }
    }

    void handleCreateOrUpdateTask(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            Optional<Task> optionalTask = parseTask(inputStream);
            if (optionalTask.isEmpty()) {
                sendText(exchange, 400, "Поля задачи не могут быть пустыми");
                return;
            }
            Task task = optionalTask.get();
            try {
                if (task.getTaskId() != 0) {
                    taskManager.updateTask(task);
                    sendText(exchange, 200, "Задача была успешно обновлена");
                    return;
                }
                taskManager.saveTask(task);
                sendText(exchange, 201, "Задача была успешно добавлена");
            } catch (TimeConflictException e) {
                sendText(exchange, 406, "Задача не может быть добавлена, " +
                        "так как пересекается с другой задачей");
            }
        }
    }

    private void handleDeleteTaskById(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> taskIdOpt = getId(exchange);

            if (taskIdOpt.isEmpty()) {
                sendText(exchange, 400, "Некорректный идентификатор задачи");
                return;
            }
            int taskId = taskIdOpt.get();
            Task task = taskManager.findTask(taskId);

            if (task == null) {
                sendText(exchange, 404, String.format("Задача с идентификатором %d не найдена", taskId));
                return;
            }
            taskManager.removeTask(taskId);
            sendText(exchange, 200, "Задача была успешно удалена");
        } catch (NotFoundException e) {
            sendText(exchange, 404, e.getMessage());
        }
    }

    private Optional<Task> parseTask(InputStream bodyInputStream) throws IOException {
        String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8);
        if (!body.contains("taskName") || !body.contains("taskDescription")
            || !body.contains("startTime") || !body.contains("duration")) {
            return Optional.empty();
        }
        Task task = HttpTaskServer.getGson().fromJson(body, Task.class);
        return Optional.of(task);
    }
}
