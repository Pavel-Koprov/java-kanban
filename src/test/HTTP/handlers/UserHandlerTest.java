package test.HTTP.handlers;

import test.HTTP.HttpTaskServerTest;
import dto.Task;
import org.junit.jupiter.api.Test;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static dto.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

public class UserHandlerTest extends HttpTaskServerTest {

    protected UserHandlerTest() throws IOException {
    }

    @Test
    public void testGetHistory() throws IOException, InterruptedException {
        Task task1 = new Task("taskName1", "taskDescription1", NEW,
                LocalDateTime.of(2024, 8, 16, 13, 0), Duration.ofMinutes(1));
        manager.saveTask(task1);
        Task task2 = new Task("taskName2", "taskDescription2", NEW,
                LocalDateTime.of(2024, 8, 16, 13, 5), Duration.ofMinutes(1));
        manager.saveTask(task2);
        manager.findTask(task1.getTaskId());
        manager.findTask(task2.getTaskId());
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> tasksFromRequest = gson.fromJson(response.body(), new TaskListTypeToken().getType());

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом 200");
        assertNotNull(tasksFromRequest, "Задачи не возвращаются");
        assertEquals(2, tasksFromRequest.size(), "Некорректное количество задач в истории");
    }

    @Test
    public void testGetPrioritized() throws IOException, InterruptedException {
        Task task1 = new Task("taskName1", "taskDescription1", NEW,
                LocalDateTime.of(2024, 8, 16, 14, 0), Duration.ofMinutes(1));
        manager.saveTask(task1);
        Task task2 = new Task("taskName2", "taskDescription2", NEW,
                LocalDateTime.of(2024, 8, 16, 14, 5), Duration.ofMinutes(1));
        manager.saveTask(task2);
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> tasksFromRequest = gson.fromJson(response.body(), new TaskListTypeToken().getType());

        assertEquals(200, response.statusCode(), "Коды ответа не совпадают");
        assertNotNull(tasksFromRequest, "Задачи не возвращаются");
        assertEquals(2, tasksFromRequest.size(), "Некорректное количество задач в истории");
    }

    static class TaskListTypeToken extends TypeToken<List<Task>> {
    }
}
