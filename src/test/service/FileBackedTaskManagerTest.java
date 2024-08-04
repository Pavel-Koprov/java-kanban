package test.service;

import org.junit.jupiter.api.Test;
import service.*;
import dto.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static dto.Status.NEW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
public class FileBackedTaskManagerTest {

    @Test
    void savedAndLoadedTasks () {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        final int taskId = inMemoryTaskManager.saveTask(task);
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = inMemoryTaskManager.saveEpic(epic);
        Subtask subtask = new Subtask("Test addNewEpic", "Test addNewEpic description",
                NEW, epicId);
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

        loadedManager.saveTask(new Task("Test addNewTask", "Test addNewTask description", NEW));
        List<Task> notEmptyList = loadedManager.getTasks();

        assertEquals(0, emptyList.size(), "Файл не пустой.");
        assertEquals(1, notEmptyList.size(), "Задача не сохранена.");

    }
}
