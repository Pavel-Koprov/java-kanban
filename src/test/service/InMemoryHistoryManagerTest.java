package test.service;

import org.junit.jupiter.api.BeforeEach;
import service.*;
import dto.*;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
public class InMemoryHistoryManagerTest {
    HistoryManager historyManager;
    @BeforeEach
    public void beforeEach() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void shouldAddToHistory() {
        Task task1 = new Task("taskName1", "taskDescription1", Status.IN_PROGRESS,
                LocalDateTime.now().plusMinutes(50), Duration.ofMinutes(1));
        historyManager.add(task1);
        List<Task> historyList = historyManager.getHistory();

        assertEquals(1, historyList.size());
        assertEquals(task1, historyManager.getHeadData());
    }
    @Test
    void shouldSaveInHistoryManagerOnlyTheLastVersionOfTask() {
        Task task1 = new Task("taskName1", "taskDescription1", Status.NEW,
                LocalDateTime.now().plusMinutes(52), Duration.ofMinutes(1));
        task1.setTaskId(10);
        historyManager.add(task1);
        task1.setTaskName("newTaskName1");
        task1.setTaskDescription("newTaskDescription1");
        task1.setStartTime(LocalDateTime.of(2024, Month.JUNE, 9, 13, 0));
        task1.setDuration(Duration.ofMinutes(20));
        List<Task> historyList = historyManager.getHistory();

        assertEquals(1, historyList.size());
        assertEquals(task1, historyManager.getHeadData());
    }

    @Test
    void shouldBeRemovedFromHistoryManagerWhenDeleted() {

        Task task1 = new Task("taskName1", "taskDescription1", Status.IN_PROGRESS,
                LocalDateTime.now().plusMinutes(54), Duration.ofMinutes(1));
        task1.setTaskId(1);
        historyManager.add(task1);
        Task task2 = new Task("taskName2", "taskDescription2", Status.NEW,
                LocalDateTime.now().plusMinutes(56), Duration.ofMinutes(1));
        task2.setTaskId(2);
        historyManager.add(task2);
        Task task3 = new Task("taskName3", "taskDescription3", Status.IN_PROGRESS,
                LocalDateTime.now().plusMinutes(58), Duration.ofMinutes(1));
        task3.setTaskId(3);
        historyManager.add(task3);
        List<Task> historyList = historyManager.getHistory();

        assertEquals(3, historyList.size());
        historyManager.remove(task2.getTaskId());
        historyList = historyManager.getHistory();
        assertEquals(2, historyList.size());

        for (Task task : historyList) {
            assertNotEquals(task2, task);
        }
    }

    @Test
    void shouldBeEmptyIfSingleTaskWasRemoved() {
        Task task1 = new Task("taskName1",
                "taskDescription1", Status.IN_PROGRESS,
                LocalDateTime.now().plusMinutes(60), Duration.ofMinutes(1));
        task1.setTaskId(1);
        historyManager.add(task1);
        historyManager.remove(task1.getTaskId());
        List<Task> historyList = historyManager.getHistory();

        assertEquals(0, historyList.size());
    }
}
