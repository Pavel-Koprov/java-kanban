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

class EpicHandlerTest extends HttpTaskServerTest {

    @Test
    public void testGetListOfAllEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("epicName1", "epicDescription1");
        manager.saveEpic(epic1);
        Epic epic2 = new Epic("epicName12", "epicDescription2");
        manager.saveEpic(epic2);
        List<Epic> listOfAllEpics = manager.getEpics();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> epicsFromRequest = gson.fromJson(response.body(), new EpicListTypeToken().getType());

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом 200");
        assertEquals(listOfAllEpics, epicsFromRequest, "Списки эпиков не совпадают");
    }

    @Test
    public void testGetEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("epicName1", "epicDescription1");
        manager.saveEpic(epic);
        URI url = URI.create("http://localhost:8080/epics/" + epic.getTaskId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epicFromRequest = gson.fromJson(response.body(), Epic.class);

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом 200");
        assertEquals(epic, epicFromRequest, "Эпики не совпадают");
    }

    @Test
    public void testCreateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("epicName1", "epicDescription1");
        String epicJson = gson.toJson(epic);
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом 201");
        List<Epic> epicsFromManager = manager.getEpics();
        assertNotNull(epicsFromManager, "Эпики не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество эпиков");
        assertEquals("epicName1", epicsFromManager.get(0).getTaskName(), "Некорректное имя эпика");
    }

    @Test
    public void testUpdateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("epicName1", "epicDescription1");
        manager.saveEpic(epic);
        epic = new Epic("epicName2", "epicDescription2");
        epic.setTaskId(epic.getTaskId());
        String epicJson = gson.toJson(epic);
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом 201");
        assertNotEquals("epicName1", epic.getTaskName(), "Некорректное имя эпика");
        assertNotEquals("epicDescription1", epic.getTaskDescription(), "Некорректное описание эпика");
        assertEquals(epic.getTaskName(), "epicName2", "Некорректное имя эпика");
        assertEquals(epic.getTaskDescription(), "epicDescription2", "Некорректное описание эпика");
    }

    @Test
    public void testGetEpicSubtasksById() throws IOException, InterruptedException {
        Epic epic = new Epic("epicName1", "epicDescription1");
        manager.saveEpic(epic);
        Subtask subtask1 = new Subtask("subtaskName1", "subtaskDescription1", Status.NEW,
                epic.getTaskId(), LocalDateTime.of(2024, 8, 16, 16, 30),
                Duration.ofMinutes(1));
        Subtask subtask2 = new Subtask("subtaskName2", "subtaskDescription2", Status.NEW,
                epic.getTaskId(), LocalDateTime.of(2024, 8, 16, 16, 35),
                Duration.ofMinutes(1));
        manager.saveSubtask(subtask1);
        manager.saveSubtask(subtask2);
        List<Subtask> listOfAllSubtasks = manager.getSubtasks();
        URI url = URI.create(String.format("http://localhost:8080/epics/%s/subtasks", epic.getTaskId()));
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> subtasksFromRequest = gson.fromJson(response.body(), new SubtaskListTypeToken().getType());

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом 200");
        assertEquals(listOfAllSubtasks, subtasksFromRequest, "Списки подзадач не совпадают");
    }

    @Test
    public void shouldDeleteEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("epicName1", "epicDescription1");
        manager.saveEpic(epic);
        URI url = URI.create("http://localhost:8080/epics/" + epic.getTaskId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> listOfAllEpics = manager.getEpics();

        assertEquals(200, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом 200");
        assertEquals(0, listOfAllEpics.size(), "Эпик не удален");
    }

    @Test
    public void shouldReturn404IfRequestedEpicDoesNotExist() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Код ответа не совпадает с ожидаемым кодом 404");
    }

    static class EpicListTypeToken extends TypeToken<List<Epic>> {
    }

    static class SubtaskListTypeToken extends TypeToken<List<Subtask>> {
    }
}
