package test.service;

import org.junit.jupiter.api.Test;
import service.*;
import dto.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryTaskManagerTest {

    @Test
    void checkHistoryOfTasks() {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        Task task1 = new Task("Оплатить курс",
                "Необходимо накопить денег для ежемесячной оплаты", Status.IN_PROGRESS);
        int idOfTask1 = inMemoryTaskManager.saveTask(task1);
        Task savedTask1 = inMemoryTaskManager.findTask(idOfTask1);

        Task task2 = new Task("Забронировать дом",
                "Необходимо выбрать дом в Приозерске на выходные", Status.NEW);
        int idOfTask2 = inMemoryTaskManager.saveTask(task2);
        Task savedTask2 = inMemoryTaskManager.findTask(idOfTask2);

        Epic epic1 = new Epic("Работа", "");
        int idOfEpic1 = inMemoryTaskManager.saveEpic(epic1);
        Epic savedEpic1 = inMemoryTaskManager.findEpic(idOfEpic1);

        Subtask subtask1 = new Subtask("График смен", "Составить график смен в феврале",
                Status.DONE, idOfEpic1);
        int idOfSubtask1 = inMemoryTaskManager.saveSubtask(subtask1);

        Subtask subtask2 = new Subtask("Расчёт зарплаты", "Рассчитать зарплату с учётом " +
                "количества смен и доплаты в утреннее время", Status.DONE, idOfEpic1);
        int idOfSubtask2 = inMemoryTaskManager.saveSubtask(subtask2);

        Epic epic2 = new Epic("Учёба", "");
        int idOfEpic2 = inMemoryTaskManager.saveEpic(epic2);

        Subtask subtask3 = new Subtask("тз4", "Сдать тз4 хотя бы до 17 февраля",
                Status.IN_PROGRESS, idOfEpic2);
        int idOfSubtask3 = inMemoryTaskManager.saveSubtask(subtask3);

        List<Task> actualHistory1 = inMemoryTaskManager.getHistory();
        List<Task> history1 = Arrays.asList(task1, task2, epic1);

        Subtask savedSubtask3 = inMemoryTaskManager.findSubtask(idOfSubtask3);
        Epic savedEpic2 = inMemoryTaskManager.findEpic(idOfEpic2);
        Subtask savedSubtask2 = inMemoryTaskManager.findSubtask(idOfSubtask2);
        Subtask savedSubtask1 = inMemoryTaskManager.findSubtask(idOfSubtask1);

        List<Task> actualHistory2 = inMemoryTaskManager.getHistory();
        List<Task> history2 = Arrays.asList(task1, task2, epic1, subtask3, epic2, subtask2, subtask1);

        inMemoryTaskManager.removeEpic(idOfEpic2);
        inMemoryTaskManager.removeTask(idOfTask1);

        List<Task> actualHistory3 = inMemoryTaskManager.getHistory();
        List<Task> history3 = Arrays.asList(task2, epic1, subtask2, subtask1);

        Subtask NewTaskView = inMemoryTaskManager.findSubtask(idOfSubtask2);

        List<Task> actualHistory4 = inMemoryTaskManager.getHistory();
        List<Task> history4 = Arrays.asList(task2, epic1, subtask1, subtask2);

        assertNotNull(actualHistory1, "История просмотров задач пустая");
        assertEquals(history1, actualHistory1, "История просмотров задач неверная");
        assertEquals(history2, actualHistory2, "История просмотров задач неверная");
        assertEquals(history3, actualHistory3, "История просмотров задач неверная");
        assertEquals(history4, actualHistory4, "История просмотров задач неверная");

    }
}
