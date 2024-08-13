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
        InMemoryTaskManager inMemoryTaskManager = Managers.getInMemoryTaskManager();

        Task task = new Task("taskName", "taskDescription", NEW,
                LocalDateTime.of(2024, 8, 13, 13, 0), Duration.ofMinutes(1));
        final int taskId = inMemoryTaskManager.saveTask(task);
        Epic epic = new Epic("epicName", "epicDescription");
        final int epicId = inMemoryTaskManager.saveEpic(epic);
        Subtask subtask = new Subtask("subtaskName", "subtaskDescription", NEW, epicId,
                LocalDateTime.of(2024, 8, 13, 14, 0), Duration.ofMinutes(1));
        final int subtaskId = inMemoryTaskManager.saveSubtask(subtask);

        InMemoryTaskManager loadedTaskManager = FileBackedTaskManager.loadFromFile(new File("Memory/StoringTasks.csv"));

        assertEquals(inMemoryTaskManager.findTask(taskId), loadedTaskManager.findTask(taskId),
                "Задачи не совпадают");
        assertEquals(inMemoryTaskManager.findEpic(epicId), loadedTaskManager.findEpic(epicId),
                "Эпики не совпадают");
        assertEquals(inMemoryTaskManager.findSubtask(subtaskId), loadedTaskManager.findSubtask(subtaskId),
                "Подзадачи не совпадают");
        assertEquals(task, inMemoryTaskManager.getPrioritizedTasks().get(0), "Неверный порядок задач в списке");
        assertEquals(subtask, inMemoryTaskManager.getPrioritizedTasks().get(1), "Неверный порядок задач в списке");
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
