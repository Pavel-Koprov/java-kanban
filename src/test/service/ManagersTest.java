package test.service;

import org.junit.jupiter.api.Test;
import service.FileBackedTaskManager;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManagersTest {
    @Test
    void initializeManagers() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
        FileBackedTaskManager fileBackedTaskManager = Managers.getFileBackedTaskManager();

        assertNotNull(inMemoryTaskManager, "Менеджер задач не создан.");
        assertNotNull(inMemoryHistoryManager, "Менеджер истории не создан.");
        assertNotNull(fileBackedTaskManager, "Менеджер работы с файлом не создан.");
    }

}
