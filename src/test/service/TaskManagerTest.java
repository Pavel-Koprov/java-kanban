package test.service;

import dto.Epic;
import dto.Status;
import dto.Subtask;
import dto.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static dto.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;

    @Test
    void getTasksTest() {
        Task task1 = new Task("taskName1", "taskDescription1", Status.IN_PROGRESS,
                LocalDateTime.now().plusMinutes(100), Duration.ofMinutes(1));
        taskManager.saveTask(task1);

        Task task2 = new Task("taskName2", "taskDescription2", Status.NEW,
                LocalDateTime.now().plusMinutes(102), Duration.ofMinutes(1));
        taskManager.saveTask(task2);

        List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Список задач не выводится");
        assertEquals(2, tasks.size(), "Неверный размер списка");
    }

    @Test
    void getEpicsTest() {
        Epic epic1 = new Epic("epicName1", "epicDescription1");
        taskManager.saveEpic(epic1);

        Epic epic2 = new Epic("epicName2", "epicDescription2");
        taskManager.saveEpic(epic2);

        List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Список эпиков не выводится");
        assertEquals(2, epics.size(), "Неверный размер списка");
    }

    @Test
    void getSubtasksTest() {
        Epic epic1 = new Epic("epicName1", "epicDescription1");
        int idOfEpic1 = taskManager.saveEpic(epic1);

        Subtask subtask1 = new Subtask("subtaskName1", "subtaskDescription1",
                Status.NEW, idOfEpic1, LocalDateTime.now().plusMinutes(104), Duration.ofMinutes(1));
        int idOfSubtask1 = taskManager.saveSubtask(subtask1);

        Subtask subtask2 = new Subtask("subtaskName2", "subtaskDescription2", Status.NEW,
                idOfEpic1, LocalDateTime.now().plusMinutes(106), Duration.ofMinutes(1));
        int idOfSubtask2 = taskManager.saveSubtask(subtask2);

        List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Список подзадач не выводится");
        assertEquals(2, subtasks.size(), "Неверный размер списка");
    }

    @Test
    void removeAllTasksTest() {
        Task task1 = new Task("taskName1", "taskDescription1", Status.IN_PROGRESS,
                LocalDateTime.now().plusMinutes(108), Duration.ofMinutes(1));
        taskManager.saveTask(task1);

        Task task2 = new Task("taskName2", "taskDescription2", Status.NEW,
                LocalDateTime.now().plusMinutes(110), Duration.ofMinutes(1));
        taskManager.saveTask(task2);

        taskManager.removeAllTasks();

        assertEquals(0, taskManager.getTasks().size(), "Список задач не пуст");
    }

    @Test
    void removeAllEpicsTest() {
        Epic epic1 = new Epic("epicName1", "epicDescription1");
        int idOfEpic1 = taskManager.saveEpic(epic1);

        Subtask subtask1 = new Subtask("subtaskName1", "subtaskDescription1",
                Status.NEW, idOfEpic1, LocalDateTime.now().plusMinutes(112), Duration.ofMinutes(1));
        taskManager.saveSubtask(subtask1);

        Subtask subtask2 = new Subtask("subtaskName2", "subtaskDescription2", Status.NEW,
                idOfEpic1, LocalDateTime.now().plusMinutes(114), Duration.ofMinutes(1));
        taskManager.saveSubtask(subtask2);

        Epic epic2 = new Epic("epicName2", "epicDescription2");
        taskManager.saveEpic(epic2);

        taskManager.removeAllEpics();

        assertEquals(0, taskManager.getEpics().size(), "Список эпиков не пуст");
        assertEquals(0, taskManager.getSubtasks().size(), "Список подзадач не пуст");
    }

    @Test
    void removeAllSubtasksTest() {
        Epic epic1 = new Epic("epicName1", "epicDescription1");
        int idOfEpic1 = taskManager.saveEpic(epic1);

        Subtask subtask1 = new Subtask("subtaskName1", "subtaskDescription1",
                Status.NEW, idOfEpic1, LocalDateTime.now().plusMinutes(116), Duration.ofMinutes(1));
        taskManager.saveSubtask(subtask1);

        Subtask subtask2 = new Subtask("subtaskName2", "subtaskDescription2", Status.NEW,
                idOfEpic1, LocalDateTime.now().plusMinutes(118), Duration.ofMinutes(1));
        taskManager.saveSubtask(subtask2);

        taskManager.removeAllSubtasks();

        assertEquals(0, taskManager.getSubtasks().size(), "Список подзадач не пуст");
        assertEquals(0, epic1.getSubtasksId().size(), "Список подзадач у эпика не пуст");
    }

    @Test
    void findTaskTest() {
        Task task = new Task("taskName1", "taskDescription1", NEW,
                LocalDateTime.now().plusMinutes(120), Duration.ofMinutes(1));
        int taskId = taskManager.saveTask(task);

        Task savedTask = taskManager.findTask(taskId);

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task, savedTask, "Задачи не совпадают");
    }

    @Test
    void findEpicTest() {
        Epic epic = new Epic("epicName1", "epicDescription1");
        int idOfEpic1 = taskManager.saveEpic(epic);
        Epic savedEpic = taskManager.findEpic(idOfEpic1);

        assertNotNull(savedEpic, "Эпик не найден");
        assertEquals(epic, savedEpic, "Эпики не совпадают");
    }

    @Test
    void findSubtaskTest() {
        Epic epic = new Epic("epicName1", "epicDescription1");
        int idOfEpic = taskManager.saveEpic(epic);

        Subtask subtask = new Subtask("subtaskName1", "subtaskDescription1",
                Status.NEW, idOfEpic, LocalDateTime.now().plusMinutes(122), Duration.ofMinutes(1));
        int idOfSubtask = taskManager.saveSubtask(subtask);
        Subtask savedSubtask = taskManager.findSubtask(idOfSubtask);

        assertNotNull(savedSubtask, "Эпик не найден");
        assertEquals(subtask, savedSubtask, "Эпики не совпадают");
        assertEquals(epic, taskManager.findEpic(savedSubtask.getEpicId()),
                "Невверный эпик у подзадачи");

    }

    @Test
    void updateTaskTest() {
        Task task1 = new Task("taskName1", "taskDescription1", Status.IN_PROGRESS,
                LocalDateTime.now().plusMinutes(124), Duration.ofMinutes(1));
        int idOfTask = taskManager.saveTask(task1);
        task1.setTaskName("newTaskName");
        task1.setTaskStatus(Status.DONE);
        Task savedTask = taskManager.findTask(idOfTask);

        assertEquals("newTaskName", savedTask.getTaskName(), "Имена не совпадают");
        assertEquals(Status.DONE, savedTask.getTaskStatus(), "Статусы не совпадают");
    }

    @Test
    void updateEpicTest() {
        Epic epic = new Epic("epicName1", "epicDescription1");
        int idOfEpic = taskManager.saveEpic(epic);
        epic.setDuration(Duration.ofMinutes(2));
        epic.setTaskName("newEpicName");
        Epic savedEpic = taskManager.findEpic(idOfEpic);

        assertEquals("newEpicName", savedEpic.getTaskName(), "Имена не совпадают");
        assertEquals(Duration.ofMinutes(2), savedEpic.getDuration(), "Продолжительности не совпадают");
    }

    @Test
    void updateSubtaskTest() {
        Epic epic1 = new Epic("epicName1", "epicDescription1");
        int idOfEpic1 = taskManager.saveEpic(epic1);

        Subtask subtask1 = new Subtask("subtaskName1", "subtaskDescription1", Status.IN_PROGRESS,
                idOfEpic1, LocalDateTime.of(2001, 6, 9, 12, 0),
                Duration.ofMinutes(1));
        int idOfSubtask = taskManager.saveSubtask(subtask1);
        Subtask newSubtask = new Subtask("newSubtaskName", "subtaskDescription1", idOfSubtask,
                Status.DONE, idOfEpic1, LocalDateTime.of(2001, 6, 9, 13, 0),
                Duration.ofMinutes(1));
        taskManager.updateSubtask(newSubtask);
        Subtask savedSubtask = taskManager.findSubtask(epic1.getSubtasksId().get(0));

        assertEquals("newSubtaskName", savedSubtask.getTaskName(), "Имена не совпадают");
        assertEquals(LocalDateTime.of(2001, 6, 9, 13, 0),
                savedSubtask.getStartTime(), "Время старта не совпадает");
        assertEquals(Status.DONE, epic1.getTaskStatus(), "Статусы у эпика не совпадают");
    }
}
