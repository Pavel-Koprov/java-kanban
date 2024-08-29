package test.HTTP.handlers;

import dto.Status;
import test.HTTP.HttpTaskServerTest;
import dto.Task;
import org.junit.jupiter.api.Test;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static dto.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

class TaskHandlerTest extends HttpTaskServerTest {

    @Test
    public void testGetListOfAllTasks() throws IOException, InterruptedException {
        Task task1 = new Task("taskName1", "taskDescription1", NEW,
                LocalDateTime.of(2024, 8, 16, 15, 0), Duration.ofMinutes(1));
        manager.saveTask(task1);
        Task task2 = new Task("taskName2", "taskDescription2", NEW,
                LocalDateTime.of(2024, 8, 16, 15, 5), Duration.ofMinutes(1));
        manager.saveTask(task2);
        URI url = URI.create("http://localhost:8080/tasks");
        List<Task> listOfAllTasks = manager.getTasks();
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> tasksFromRequest = gson.fromJson(response.body(), new TaskListTypeToken().getType());

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом 200");
        assertEquals(listOfAllTasks, tasksFromRequest, "Списки задач не совпадают");
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        Task task = new Task("taskName", "taskDescription", NEW,
                LocalDateTime.of(2024, 8, 16, 15, 10), Duration.ofMinutes(1));
        manager.saveTask(task);
        URI url = URI.create("http://localhost:8080/tasks/" + task.getTaskId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskFromRequest = gson.fromJson(response.body(), Task.class);

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом 200");
        assertEquals(task, taskFromRequest, "Задачи не совпадают");
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("taskName", "taskDescription", Status.NEW,
                LocalDateTime.of(2024, 8, 16, 15, 15), Duration.ofMinutes(1));
        String taskJson = gson.toJson(task);
        System.out.println(taskJson);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers
                .ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response);

        assertEquals(201, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом 200");

        List<Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("taskName", tasksFromManager.get(0).getTaskName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("taskName1", "taskDescription1", Status.NEW,
                LocalDateTime.of(2024, 8, 16, 15, 20), Duration.ofMinutes(1));
        manager.saveTask(task);
        task = new Task("taskName2", "taskDescription2", Status.NEW,
                LocalDateTime.of(2024, 8, 16, 15, 25), Duration.ofMinutes(1));
        task.setTaskId(task.getTaskId());
        String taskJson = gson.toJson(task);
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом 200");
        assertNotEquals("taskName1", task.getTaskName(), "Некорректное имя задачи");
        assertNotEquals("taskDescription1", task.getTaskDescription(), "Некорректное описание задачи");
        assertEquals(task.getTaskName(), "taskName2", "Некорректное имя задачи");
        assertEquals(task.getTaskDescription(), "taskDescription2", "Некорректное описание задачи");
    }

    @Test
    public void shouldDeleteTaskById() throws IOException, InterruptedException {
        Task task = new Task("taskName", "taskDescription", Status.NEW,
                LocalDateTime.of(2024, 8, 16, 15, 30), Duration.ofMinutes(1));
        manager.saveTask(task);
        URI url = URI.create("http://localhost:8080/tasks/" + task.getTaskId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> listOfAllTasks = manager.getTasks();

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом 200");
        assertEquals(listOfAllTasks.size(), 0, "Задача не удалена");
    }

    @Test
    public void shouldNotAddNewTaskIfItIntersectsWithExistingTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks");
        Task task1 = new Task("taskName", "taskDescription", Status.NEW,
                LocalDateTime.of(2024, 8, 16, 15, 35), Duration.ofMinutes(1));

        Task task2 = new Task("taskName", "taskDescription", Status.NEW,
                LocalDateTime.of(2024, 8, 16, 15, 35), Duration.ofMinutes(1));
        manager.saveTask(task2);
        String taskJson = gson.toJson(task1);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом 406");
    }

    @Test
    public void shouldReturn404IfRequestedTaskDoesNotExist() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом 404");
    }

    static class TaskListTypeToken extends TypeToken<List<Task>> {
    }
}
