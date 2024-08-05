package test.dto;

import dto.Epic;
import dto.Subtask;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.util.ArrayList;

import static dto.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    void addNewSubtask() {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = inMemoryTaskManager.saveEpic(epic);

        Subtask subtask = new Subtask("Test addNewEpic", "Test addNewEpic description",
                NEW, epicId);
        final int subtaskId = inMemoryTaskManager.saveSubtask(subtask);

        final Subtask savedSubtask = inMemoryTaskManager.findSubtask(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");

        final ArrayList<Subtask> subtasks = inMemoryTaskManager.getSubtasks();

        assertNotNull(subtasks, "Задачи не возвращаются.");
    }

}