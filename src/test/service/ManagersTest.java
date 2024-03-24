package test.service;

import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManagersTest {
    @Test
    void initializeManagers() {
        TaskManager inMemoryTaskManager = Managers.getDefault();
        HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

        assertNotNull(inMemoryTaskManager, "Менеджер задач не создан.");
        assertNotNull(inMemoryHistoryManager, "Менеджер истории не создан.");
    }

}
