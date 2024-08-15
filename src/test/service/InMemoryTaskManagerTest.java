package test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.*;
import dto.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static dto.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getInMemoryTaskManager();
    }

    @Test
    void checkHistoryOfTasks() {
        Task task1 = new Task("taskName1", "taskDescription1", Status.IN_PROGRESS,
                LocalDateTime.now().plusMinutes(62), Duration.ofMinutes(1));
        int idOfTask1 = taskManager.saveTask(task1);
        Task savedTask1 = taskManager.findTask(idOfTask1);

        Task task2 = new Task("taskName2", "taskDescription2", Status.NEW,
                LocalDateTime.now().plusMinutes(64), Duration.ofMinutes(1));
        int idOfTask2 = taskManager.saveTask(task2);
        Task savedTask2 = taskManager.findTask(idOfTask2);

        Epic epic1 = new Epic("epicName1", "epicDescription1");
        int idOfEpic1 = taskManager.saveEpic(epic1);
        Epic savedEpic1 = taskManager.findEpic(idOfEpic1);

        Subtask subtask1 = new Subtask("subtaskName1", "subtaskDescription1",
                Status.DONE, idOfEpic1, LocalDateTime.now().plusMinutes(66), Duration.ofMinutes(1));
        int idOfSubtask1 = taskManager.saveSubtask(subtask1);

        Subtask subtask2 = new Subtask("subtaskName2", "subtaskDescription2", Status.DONE,
                idOfEpic1, LocalDateTime.now().plusMinutes(68), Duration.ofMinutes(1));
        int idOfSubtask2 = taskManager.saveSubtask(subtask2);

        Epic epic2 = new Epic("epicName2", "epicDescription2");
        int idOfEpic2 = taskManager.saveEpic(epic2);

        Subtask subtask3 = new Subtask("subtaskName3", "subtaskDescription3",
                Status.IN_PROGRESS, idOfEpic2, LocalDateTime.now().plusMinutes(70), Duration.ofMinutes(1));
        int idOfSubtask3 = taskManager.saveSubtask(subtask3);

        List<Task> actualHistory1 = taskManager.getHistory();
        List<Task> history1 = Arrays.asList(task1, task2, epic1);

        Subtask savedSubtask3 = taskManager.findSubtask(idOfSubtask3);
        Epic savedEpic2 = taskManager.findEpic(idOfEpic2);
        Subtask savedSubtask2 = taskManager.findSubtask(idOfSubtask2);
        Subtask savedSubtask1 = taskManager.findSubtask(idOfSubtask1);

        List<Task> actualHistory2 = taskManager.getHistory();
        List<Task> history2 = Arrays.asList(task1, task2, epic1, subtask3, epic2, subtask2, subtask1);

        taskManager.removeEpic(idOfEpic2);
        taskManager.removeTask(idOfTask1);

        List<Task> actualHistory3 = taskManager.getHistory();
        List<Task> history3 = Arrays.asList(task2, epic1, subtask2, subtask1);

        Subtask newTaskView = taskManager.findSubtask(idOfSubtask2);

        List<Task> actualHistory4 = taskManager.getHistory();
        List<Task> history4 = Arrays.asList(task2, epic1, subtask1, subtask2);

        assertNotNull(actualHistory1, "История просмотров задач пустая");
        assertEquals(history1, actualHistory1, "История просмотров задач неверная");
        assertEquals(history2, actualHistory2, "История просмотров задач неверная");
        assertEquals(history3, actualHistory3, "История просмотров задач неверная");
        assertEquals(history4, actualHistory4, "История просмотров задач неверная");

    }

    @Test
    public void shouldNotThrowExceptionIfStartTimeOfTasksDoesNotIntersect() {
        assertDoesNotThrow(() -> {
            Task task1 = new Task("taskName1", "taskDescription1", NEW,
                    LocalDateTime.now().plusMinutes(72), Duration.ofMinutes(1));
            taskManager.saveTask(task1);
            Task task2 = new Task("taskName1", "taskDescription1", NEW,
                    LocalDateTime.now().plusMinutes(74), Duration.ofMinutes(1));
            taskManager.saveTask(task2);
        });
    }
}
