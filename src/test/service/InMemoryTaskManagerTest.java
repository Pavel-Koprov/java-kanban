package test.service;

import org.junit.jupiter.api.Test;
import service.*;
import dto.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static dto.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    @Test
    void checkHistoryOfTasks() {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        Task task1 = new Task("taskName1", "taskDescription1", Status.IN_PROGRESS,
                LocalDateTime.now().plusMinutes(62), Duration.ofMinutes(1));
        int idOfTask1 = inMemoryTaskManager.saveTask(task1);
        Task savedTask1 = inMemoryTaskManager.findTask(idOfTask1);

        Task task2 = new Task("taskName2", "taskDescription2", Status.NEW,
                LocalDateTime.now().plusMinutes(64), Duration.ofMinutes(1));
        int idOfTask2 = inMemoryTaskManager.saveTask(task2);
        Task savedTask2 = inMemoryTaskManager.findTask(idOfTask2);

        Epic epic1 = new Epic("epicName1", "epicDescription1");
        int idOfEpic1 = inMemoryTaskManager.saveEpic(epic1);
        Epic savedEpic1 = inMemoryTaskManager.findEpic(idOfEpic1);

        Subtask subtask1 = new Subtask("subtaskName1", "subtaskDescription1",
                Status.DONE, idOfEpic1, LocalDateTime.now().plusMinutes(66), Duration.ofMinutes(1));
        int idOfSubtask1 = inMemoryTaskManager.saveSubtask(subtask1);

        Subtask subtask2 = new Subtask("subtaskName2", "subtaskDescription2", Status.DONE,
                idOfEpic1, LocalDateTime.now().plusMinutes(68), Duration.ofMinutes(1));
        int idOfSubtask2 = inMemoryTaskManager.saveSubtask(subtask2);

        Epic epic2 = new Epic("epicName2", "epicDescription2");
        int idOfEpic2 = inMemoryTaskManager.saveEpic(epic2);

        Subtask subtask3 = new Subtask("subtaskName3", "subtaskDescription3",
                Status.IN_PROGRESS, idOfEpic2, LocalDateTime.now().plusMinutes(70), Duration.ofMinutes(1));
        int idOfSubtask3 = inMemoryTaskManager.saveSubtask(subtask3);

        List<Task> actualHistory1 = inMemoryTaskManager.getHistory();
        List<Task> history1 = Arrays.asList(task1, task2, epic1);

        Subtask savedSubtask3 = inMemoryTaskManager.findSubtask(idOfSubtask3);
        Epic savedEpic2 = inMemoryTaskManager.findEpic(idOfEpic2);
        Subtask savedSubtask2 = inMemoryTaskManager.findSubtask(idOfSubtask2);
        Subtask savedSubtask1 = inMemoryTaskManager.findSubtask(idOfSubtask1);

        List<Task> actualHistory2 = inMemoryTaskManager.getHistory();
        List<Task> history2 = Arrays.asList(task1, task2, epic1, subtask3, epic2, subtask2, subtask1);

        inMemoryTaskManager.removeEpic(idOfEpic2);
        inMemoryTaskManager.removeTask(idOfTask1);

        List<Task> actualHistory3 = inMemoryTaskManager.getHistory();
        List<Task> history3 = Arrays.asList(task2, epic1, subtask2, subtask1);

        Subtask newTaskView = inMemoryTaskManager.findSubtask(idOfSubtask2);

        List<Task> actualHistory4 = inMemoryTaskManager.getHistory();
        List<Task> history4 = Arrays.asList(task2, epic1, subtask1, subtask2);

        assertNotNull(actualHistory1, "История просмотров задач пустая");
        assertEquals(history1, actualHistory1, "История просмотров задач неверная");
        assertEquals(history2, actualHistory2, "История просмотров задач неверная");
        assertEquals(history3, actualHistory3, "История просмотров задач неверная");
        assertEquals(history4, actualHistory4, "История просмотров задач неверная");

    }

    @Test
    public void shouldNotThrowExceptionIfStartTimeOfTasksDoesNotIntersect() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        assertDoesNotThrow(() -> {
            Task task1 = new Task("taskName1", "taskDescription1", NEW,
                    LocalDateTime.now().plusMinutes(72), Duration.ofMinutes(1));
            inMemoryTaskManager.saveTask(task1);
            Task task2 = new Task("taskName1", "taskDescription1", NEW,
                    LocalDateTime.now().plusMinutes(74), Duration.ofMinutes(1));
            inMemoryTaskManager.saveTask(task2);
        });
    }
}
