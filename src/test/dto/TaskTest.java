package test.dto;

import dto.*;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import java.util.ArrayList;

import static dto.Status.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void addNewTask() {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        final int taskId = inMemoryTaskManager.saveTask(task);

        final Task savedTask = inMemoryTaskManager.findTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final ArrayList<Task> tasks = inMemoryTaskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
    }
}