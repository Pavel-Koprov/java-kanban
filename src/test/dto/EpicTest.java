package test.dto;

import dto.Epic;
import dto.Status;
import dto.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    TaskManager inMemoryTaskManager;
    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = Managers.getDefault();
    }

    @Test
    void addNewEpic() {
        Epic epic = new Epic("epicName", "epicDescription");
        final int epicId = inMemoryTaskManager.saveEpic(epic);

        final Epic savedEpic = inMemoryTaskManager.findEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = inMemoryTaskManager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
    }

    @Test
    void addEpicWithSubtasksAndRemoveSubtask() {
        Epic epic1 = new Epic("epicName1", "epicDescription1");
        int idOfEpic1 = inMemoryTaskManager.saveEpic(epic1);

        Subtask subtask1 = new Subtask("subtaskName1", "subtaskDescription1",
                Status.DONE, idOfEpic1, LocalDateTime.now().plusMinutes(22), Duration.ofMinutes(1));
        int idOfSubtask1 = inMemoryTaskManager.saveSubtask(subtask1);

        Subtask subtask2 = new Subtask("subtaskName2", "subtaskDescription2", Status.DONE,
                idOfEpic1, LocalDateTime.now().plusMinutes(24), Duration.ofMinutes(1));
        int idOfSubtask2 = inMemoryTaskManager.saveSubtask(subtask2);

        List<Subtask> listOfSubtasks1 = Arrays.asList(subtask1, subtask2);
        List<Subtask> actualListOfSubtasks1 = inMemoryTaskManager.getEpicSubtasks(idOfEpic1);

        inMemoryTaskManager.removeSubtask(idOfSubtask2);

        List<Subtask> listOfSubtasks2 = Arrays.asList(subtask1);
        List<Subtask> actualListOfSubtasks2 = inMemoryTaskManager.getEpicSubtasks(idOfEpic1);

        inMemoryTaskManager.removeAllSubtasks();

        List<Subtask> listOfSubtasks3 = Arrays.asList();
        List<Subtask> actualListOfSubtasks3 = inMemoryTaskManager.getEpicSubtasks(idOfEpic1);

        assertEquals(listOfSubtasks1, actualListOfSubtasks1, "Список подзадач неверный");
        assertEquals(listOfSubtasks2, actualListOfSubtasks2, "Список подзадач неверный");
        assertEquals(listOfSubtasks3, actualListOfSubtasks3, "Список подзадач неверный");
    }

    @Test
    void checkStatusOfEpicDependingOnSubtask() {
        Epic epic1 = new Epic("epicName1", "epicDescription1");
        int idOfEpic1 = inMemoryTaskManager.saveEpic(epic1);

        Subtask subtask1 = new Subtask("subtaskName1", "subtaskDescription1",
                Status.NEW, idOfEpic1, LocalDateTime.now().plusMinutes(26), Duration.ofMinutes(1));
        int idOfSubtask1 = inMemoryTaskManager.saveSubtask(subtask1);

        Subtask subtask2 = new Subtask("subtaskName2", "subtaskDescription2", Status.NEW,
                idOfEpic1, LocalDateTime.now().plusMinutes(28), Duration.ofMinutes(1));
        int idOfSubtask2 = inMemoryTaskManager.saveSubtask(subtask2);

        Epic epic2 = new Epic("epicName2", "epicDescription2");
        int idOfEpic2 = inMemoryTaskManager.saveEpic(epic2);

        Subtask subtask3 = new Subtask("subtaskName3", "subtaskDescription3", Status.NEW,
                idOfEpic2, LocalDateTime.now().plusMinutes(30), Duration.ofMinutes(1));
        int idOfSubtask3 = inMemoryTaskManager.saveSubtask(subtask3);
        Subtask subtask4 = new Subtask("subtaskName4", "subtaskDescription4", Status.DONE,
                idOfEpic2, LocalDateTime.now().plusMinutes(32), Duration.ofMinutes(1));
        int idOfSubtask4 = inMemoryTaskManager.saveSubtask(subtask4);

        Epic epic3 = new Epic("epicName3", "epicDescription3");
        int idOfEpic3 = inMemoryTaskManager.saveEpic(epic3);

        Subtask subtask5 = new Subtask("subtaskName5", "subtaskDescription5", Status.DONE,
                idOfEpic3, LocalDateTime.now().plusMinutes(34), Duration.ofMinutes(1));
        int idOfSubtask5 = inMemoryTaskManager.saveSubtask(subtask5);
        Subtask subtask6 = new Subtask("subtaskName6", "subtaskDescription6", Status.DONE,
                idOfEpic3, LocalDateTime.now().plusMinutes(36), Duration.ofMinutes(1));
        int idOfSubtask6 = inMemoryTaskManager.saveSubtask(subtask6);

        Epic epic4 = new Epic("epicName4", "epicDescription4");
        int idOfEpic4 = inMemoryTaskManager.saveEpic(epic4);

        Subtask subtask7 = new Subtask("subtaskName7", "subtaskDescription7", Status.IN_PROGRESS,
                idOfEpic4, LocalDateTime.now().plusMinutes(38), Duration.ofMinutes(1));
        int idOfSubtask7 = inMemoryTaskManager.saveSubtask(subtask7);
        Subtask subtask8 = new Subtask("subtaskName8", "subtaskDescription8", Status.IN_PROGRESS,
                idOfEpic4, LocalDateTime.now().plusMinutes(40), Duration.ofMinutes(1));
        int idOfSubtask8 = inMemoryTaskManager.saveSubtask(subtask8);

        assertEquals(Status.NEW, epic1.getTaskStatus(), "Статус эпика неверный.");
        assertEquals(Status.IN_PROGRESS, epic2.getTaskStatus(), "Статус эпика неверный.");
        assertEquals(Status.DONE, epic3.getTaskStatus(), "Статус эпика неверный.");
        assertEquals(Status.IN_PROGRESS, epic4.getTaskStatus(), "Статус эпика неверный.");
    }

}