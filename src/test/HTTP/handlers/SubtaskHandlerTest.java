package test.HTTP.handlers;

import dto.Status;
import test.HTTP.HttpTaskServerTest;
import dto.Epic;
import dto.Subtask;
import org.junit.jupiter.api.Test;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SubtaskHandlerTest extends HttpTaskServerTest {

    protected SubtaskHandlerTest() throws IOException {
    }

    @Test
    public void testGetListOfAllSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("epicName", "epicDescription");
        manager.saveEpic(epic);
        Subtask subtask1 = new Subtask("subtaskName1", "subtaskDescription1", Status.NEW,
                epic.getTaskId(), LocalDateTime.of(2024, 8, 16, 15, 40),
                Duration.ofMinutes(1));
        manager.saveSubtask(subtask1);
        Subtask subtask2 = new Subtask("subtaskName2", "subtaskDescription2", Status.NEW,
                epic.getTaskId(), LocalDateTime.of(2024, 8, 16, 15, 45),
                Duration.ofMinutes(1));
        manager.saveSubtask(subtask2);
        List<Subtask> listOfAllSubtasks = manager.getSubtasks();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> subtasksFromRequest = gson.fromJson(response.body(), new SubtaskListTypeToken().getType());

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом 200");
        assertEquals(listOfAllSubtasks, subtasksFromRequest, "Списки подзадач не совпадают");
    }

    @Test
    public void testGetSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("epicName", "epicDescription");
        manager.saveEpic(epic);
        Subtask subtask = new Subtask("subtaskName", "subtaskDescription", Status.NEW,
                epic.getTaskId(), LocalDateTime.of(2024, 8, 16, 15, 50),
                Duration.ofMinutes(1));
        manager.saveSubtask(subtask);
        URI url = URI.create("http://localhost:8080/subtasks/" + subtask.getTaskId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtaskFromRequest = gson.fromJson(response.body(), Subtask.class);

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом 200");
        assertEquals(subtask, subtaskFromRequest, "Подзадачи не совпадают");
    }

    @Test
    public void testCreateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("epicName", "epicDescription");
        manager.saveEpic(epic);
        Subtask subtask = new Subtask("subtaskName", "subtaskDescription", Status.NEW,
                epic.getTaskId(), LocalDateTime.of(2024, 8, 16, 15, 55),
                Duration.ofMinutes(1));
        String subtaskJson = gson.toJson(subtask);
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом 201");

        List<Subtask> subtasksFromManager = manager.getSubtasks();

        assertNotNull(subtasksFromManager, "Подзадачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество подззадач");
        assertEquals("subtaskName", subtasksFromManager.get(0).getTaskName(),
                "Некорректное имя подзадачи");
    }

    @Test
    public void testUpdateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("epicName", "epicDescription");
        manager.saveEpic(epic);
        Subtask subtask = new Subtask("subtaskName1", "subtaskDescription1", Status.NEW,
                epic.getTaskId(), LocalDateTime.of(2024, 8, 16, 16, 0),
                Duration.ofMinutes(1));
        manager.saveSubtask(subtask);
        subtask = new Subtask("subtaskName2", "subtaskDescription2", Status.NEW,
                epic.getTaskId(), LocalDateTime.of(2024, 8, 16, 16, 5),
                Duration.ofMinutes(1));
        subtask.setTaskId(subtask.getTaskId());
        String subtaskJson = gson.toJson(subtask);
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом 201");
        assertNotEquals("subtaskName1", subtask.getTaskName(), "Некорректное имя подзадачи");
        assertNotEquals("subtaskDescription1", subtask.getTaskDescription(),
                "Некорректное описание подзадачи");
        assertEquals(subtask.getTaskName(), "subtaskName2", "Некорректное имя подзадачи");
        assertEquals(subtask.getTaskDescription(), "subtaskDescription2",
                "Некорректное описание подзадачи");
    }

    @Test
    public void shouldDeleteSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("epicName", "epicDescription");
        manager.saveEpic(epic);
        Subtask subtask = new Subtask("subtaskName", "subtaskDescription", Status.NEW,
                epic.getTaskId(), LocalDateTime.of(2024, 8, 16, 16, 10),
                Duration.ofMinutes(1));
        manager.saveSubtask(subtask);
        URI url = URI.create("http://localhost:8080/subtasks/" + subtask.getTaskId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> listSubtasks = manager.getSubtasks();

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом 200");
        assertEquals(0, listSubtasks.size(), "Подзадача не удалена");
    }

    @Test
    public void shouldNotAddNewSubtaskIfItIntersectsWithExistingSubtask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/subtasks");
        Epic epic = new Epic("epicName", "epicDescription");
        manager.saveEpic(epic);
        Subtask subtask1 = new Subtask("subtaskName1", "subtaskDescription1", Status.NEW,
                epic.getTaskId(), LocalDateTime.of(2024, 8, 16, 16, 15),
                Duration.ofMinutes(1));
        Subtask subtask2 = new Subtask("subtaskName2", "subtaskDescription2", Status.NEW,
                epic.getTaskId(), LocalDateTime.of(2024, 8, 16, 16, 15),
                Duration.ofMinutes(1));
        manager.saveSubtask(subtask2);
        String subtaskJson = gson.toJson(subtask1);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом 406");
    }

    @Test
    public void shouldReturn404IfRequestedSubtaskDoesNotExist() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом 404");
    }

    static class SubtaskListTypeToken extends TypeToken<List<Subtask>> {
    }
}
