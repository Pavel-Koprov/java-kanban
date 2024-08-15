package test.dto;

import dto.*;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static dto.Status.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void addNewTask() {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        Task task = new Task("taskName1", "taskDescription1", NEW,
                LocalDateTime.now().plusMinutes(20), Duration.ofMinutes(1));
        final int taskId = inMemoryTaskManager.saveTask(task);

        final Task savedTask = inMemoryTaskManager.findTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = inMemoryTaskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
    }
}