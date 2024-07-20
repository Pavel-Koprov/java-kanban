import dto.Epic;
import dto.Status;
import dto.Subtask;
import dto.Task;

import service.Managers;
import service.TaskManager;

import java.sql.SQLOutput;

public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        System.out.println("Вас приветствует трекер задач!");

        // 1. Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.
        Task task1 = new Task("Оплатить курс",
                "Необходимо накопить денег для ежемесячной оплаты", Status.IN_PROGRESS);
        int idOfTask1 = inMemoryTaskManager.saveTask(task1);

        Task task2 = new Task("Забронировать дом",
                "Необходимо выбрать дом в Приозерске на выходные", Status.NEW);
        int idOfTask2 = inMemoryTaskManager.saveTask(task2);

        Epic epic1 = new Epic("Работа", "");
        int idOfEpic1 = inMemoryTaskManager.saveEpic(epic1);

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

        // 2. Распечатайте списки эпиков, задач и подзадач
        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println(inMemoryTaskManager.getSubtasks());
        System.out.println(inMemoryTaskManager.getEpicSubtasks(idOfEpic1));
        System.out.println(inMemoryTaskManager.getEpicSubtasks(idOfEpic2));

        /*// 3. Измените статусы созданных объектов, распечатайте их
        Task newTask1 = new Task("Оплатить курс",
                "Необходимо накопить денег для ежемесячной оплаты", idOfTask1, Status.NEW);
        inMemoryTaskManager.updateTask(newTask1);

        Task newTask2 = new Task("Забронировать дом",
                "Необходимо выбрать дом в Приозерске на выходные", idOfTask2, Status.DONE);
        inMemoryTaskManager.updateTask(newTask2);

        Epic newEpic1 = new Epic("Работа", "", idOfEpic1);
        inMemoryTaskManager.updateEpic(newEpic1);

        Subtask newSubtask1 = new Subtask("График смен", "Составить график смен в феврале",
                idOfSubtask1, Status.IN_PROGRESS, idOfEpic1);
        inMemoryTaskManager.updateSubtask(newSubtask1);

        Subtask newSubtask2 = new Subtask("Расчёт зарплаты", "Рассчитать зарплату с учётом " +
                "количества смен и доплаты в утреннее время", idOfSubtask2, Status.NEW, idOfEpic1);
        inMemoryTaskManager.updateSubtask(newSubtask2);

        Epic newEpic2 = new Epic("Учёба", "", idOfEpic2);
        inMemoryTaskManager.updateEpic(newEpic2);

        Subtask newSubtask3 = new Subtask("тз4", "Сдать тз4 хотя бы до 17 февраля",
                idOfSubtask3, Status.NEW, idOfEpic2);
        inMemoryTaskManager.updateSubtask(newSubtask3);

        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println(inMemoryTaskManager.getSubtasks());
        System.out.println(inMemoryTaskManager.getEpicSubtasks(idOfEpic1));
        System.out.println(inMemoryTaskManager.getEpicSubtasks(idOfEpic2));

        //4. Удалите по одной из каждого типа задач
        inMemoryTaskManager.removeTask(idOfTask2);
        inMemoryTaskManager.removeEpic(idOfEpic2);
        inMemoryTaskManager.removeSubtask(idOfSubtask1);

        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println(inMemoryTaskManager.getSubtasks());
        System.out.println(inMemoryTaskManager.getEpicSubtasks(idOfEpic1));
        System.out.println(inMemoryTaskManager.getEpicSubtasks(idOfEpic2));*/
    }
}
