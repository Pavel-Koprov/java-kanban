package test.dto;

import dto.Epic;
import dto.Status;
import dto.Subtask;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Test
    void addEpicWithSubtasksAndRemoveSubtask() {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        Epic epic1 = new Epic("Работа", "");
        int idOfEpic1 = inMemoryTaskManager.saveEpic(epic1);

        Subtask subtask1 = new Subtask("График смен", "Составить график смен в феврале",
                Status.DONE, idOfEpic1);
        int idOfSubtask1 = inMemoryTaskManager.saveSubtask(subtask1);

        Subtask subtask2 = new Subtask("Расчёт зарплаты", "Рассчитать зарплату с учётом " +
                "количества смен и доплаты в утреннее время", Status.DONE, idOfEpic1);
        int idOfSubtask2 = inMemoryTaskManager.saveSubtask(subtask2);

        List<Subtask> listOfSubtasks1 = Arrays.asList(subtask1, subtask2);
        List<Subtask> actualListOfSubtasks1 = inMemoryTaskManager.getEpicSubtasks(idOfEpic1);

        inMemoryTaskManager.removeSubtask(idOfSubtask2);

        List<Subtask> listOfSubtasks2 = Arrays.asList(subtask1);
        List<Subtask> actualListOfSubtasks2 = inMemoryTaskManager.getEpicSubtasks(idOfEpic1);

        inMemoryTaskManager.removeAllSubtasks();

        List<Subtask> listOfSubtasks3 = Arrays.asList();
        List<Subtask> actualListOfSubtasks3 = inMemoryTaskManager.getEpicSubtasks(idOfEpic1);

        assertEquals(listOfSubtasks1, actualListOfSubtasks1, "Список подзадач неверный");
        assertEquals(listOfSubtasks2, actualListOfSubtasks2, "Список подзадач неверный");
        assertEquals(listOfSubtasks3, actualListOfSubtasks3, "Список подзадач неверный");
    }


}