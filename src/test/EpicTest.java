package test;

import dto.Epic;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void addNewEpic() {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        final int epicId = inMemoryTaskManager.saveEpic(epic);

        final Epic savedEpic = inMemoryTaskManager.findEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final ArrayList<Epic> epics = inMemoryTaskManager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }


}