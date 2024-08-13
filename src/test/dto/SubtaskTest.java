package test.dto;

import dto.Epic;
import dto.Subtask;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static dto.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    void addNewSubtask() {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        Epic epic = new Epic("epicName", "epicDescription");
        final int epicId = inMemoryTaskManager.saveEpic(epic);

        Subtask subtask = new Subtask("subtaskName", "subtaskDescription", NEW, epicId,
                LocalDateTime.now().plusMinutes(42), Duration.ofMinutes(1));
        final int subtaskId = inMemoryTaskManager.saveSubtask(subtask);

        final Subtask savedSubtask = inMemoryTaskManager.findSubtask(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");

        final List<Subtask> subtasks = inMemoryTaskManager.getSubtasks();

        assertNotNull(subtasks, "Задачи не возвращаются.");
    }

}