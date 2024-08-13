package test.service;

import org.junit.jupiter.api.Test;
import service.*;
import dto.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static dto.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    @Test
    void savedAndLoadedTasks () {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        Task task = new Task("taskName", "taskDescription", NEW,
                LocalDateTime.now().plusMinutes(44), Duration.ofMinutes(1));
        final int taskId = inMemoryTaskManager.saveTask(task);
        Epic epic = new Epic("epicName", "epicDescription");
        final int epicId = inMemoryTaskManager.saveEpic(epic);
        Subtask subtask = new Subtask("subtaskName", "subtaskDescription",
                NEW, epicId, LocalDateTime.now().plusMinutes(46), Duration.ofMinutes(1));
        final int subtaskId = inMemoryTaskManager.saveSubtask(subtask);

        TaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(new File("Memory/StoringTasks.csv"));

        assertEquals(inMemoryTaskManager.findTask(taskId), loadedTaskManager.findTask(taskId),
                "Задачи не совпадают");
        assertEquals(inMemoryTaskManager.findEpic(epicId), loadedTaskManager.findEpic(epicId),
                "Эпики не совпадают");
        assertEquals(inMemoryTaskManager.findSubtask(subtaskId), loadedTaskManager.findSubtask(subtaskId),
                "Подзадачи не совпадают");
    }

    @Test
    void savedAndLoadedAnEmptyFile() throws IOException {
        TaskManager loadedManager = FileBackedTaskManager.loadFromFile(File.createTempFile("Memory/",
                "TestFile.csv"));
        List<Task> emptyList = loadedManager.getTasks();

        loadedManager.saveTask(new Task("taskName", "taskDescription", NEW,
                LocalDateTime.now().plusMinutes(48), Duration.ofMinutes(1)));
        List<Task> notEmptyList = loadedManager.getTasks();

        assertEquals(0, emptyList.size(), "Файл не пустой.");
        assertEquals(1, notEmptyList.size(), "Задача не сохранена.");

    }
}
