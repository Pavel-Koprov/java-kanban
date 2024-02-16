import dto.Epic;
import dto.Status;
import dto.Subtask;
import dto.Task;
import service.TaskManager;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        System.out.println("Вас приветствует трекер задач!");

        // 1. Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.
        Task task1 = new Task("Оплатить курс",
                "Необходимо накопить денег для ежемесячной оплаты", Status.IN_PROGRESS);
        int idOfTask1 = taskManager.saveTask(task1);

        Task task2 = new Task("Забронировать дом",
                "Необходимо выбрать дом в Приозерске на выходные", Status.NEW);
        int idOfTask2 = taskManager.saveTask(task2);

        Epic epic1 = new Epic("Работа", "", Status.NEW);
        int idOfEpic1 = taskManager.saveEpic(epic1);

        Subtask subtask1 = new Subtask("График смен", "Составить график смен в феврале",
                Status.DONE, idOfEpic1);
        int idOfSubtask1 = taskManager.saveSubtask(subtask1);

        Subtask subtask2 = new Subtask("Расчёт зарплаты", "Рассчитать зарплату с учётом " +
                "количества смен и доплаты в утреннее время", Status.DONE, idOfEpic1);
        int idOfSubtask2 = taskManager.saveSubtask(subtask2);

        Epic epic2 = new Epic("Учёба", "", Status.NEW);
        int idOfEpic2 = taskManager.saveEpic(epic2);

        Subtask subtask3 = new Subtask("тз4", "Сдать тз4 хотя бы до 17 февраля",
                Status.IN_PROGRESS, idOfEpic2);
        int idOfSubtask3 = taskManager.saveSubtask(subtask3);

        // 2. Распечатайте списки эпиков, задач и подзадач
        printTasks(taskManager);
        printEpics(taskManager);
        printSubtasks(taskManager);
        printEpicSubtask(taskManager, idOfEpic1);
        printEpicSubtask(taskManager, idOfEpic2);

        // 3. Измените статусы созданных объектов, распечатайте их
        Task newTask1 = new Task("Оплатить курс",
                "Необходимо накопить денег для ежемесячной оплаты", idOfTask1, Status.NEW);
        taskManager.updateTask(newTask1);

        Task newTask2 = new Task("Забронировать дом",
                "Необходимо выбрать дом в Приозерске на выходные", idOfTask2, Status.DONE);
        taskManager.updateTask(newTask2);

        Epic newEpic1 = new Epic("Работа", "", idOfEpic1);
        taskManager.updateEpic(newEpic1);

        Subtask newSubtask1 = new Subtask("График смен", "Составить график смен в феврале",
                idOfSubtask1, Status.IN_PROGRESS, idOfEpic1);
        taskManager.updateSubtask(newSubtask1);

        Subtask newSubtask2 = new Subtask("Расчёт зарплаты", "Рассчитать зарплату с учётом " +
                "количества смен и доплаты в утреннее время", idOfSubtask2, Status.NEW, idOfEpic1);
        taskManager.updateSubtask(newSubtask2);

        Epic newEpic2 = new Epic("Учёба", "", idOfEpic2);
        taskManager.updateEpic(newEpic2);

        Subtask newSubtask3 = new Subtask("тз4", "Сдать тз4 хотя бы до 17 февраля",
                idOfSubtask3, Status.NEW, idOfEpic2);
        taskManager.updateSubtask(newSubtask3);

        printTasks(taskManager);
        printEpics(taskManager);
        printSubtasks(taskManager);
        printEpicSubtask(taskManager, idOfEpic1);
        printEpicSubtask(taskManager, idOfEpic2);

        //4. Удалите по одной из каждого типа задач
        taskManager.removeTask(idOfTask2);
        taskManager.removeEpic(idOfEpic2);
        taskManager.removeSubtask(idOfSubtask1);

        printTasks(taskManager);
        printEpics(taskManager);
        printSubtasks(taskManager);
        printEpicSubtask(taskManager, idOfEpic1);
        printEpicSubtask(taskManager, idOfEpic2);
    }

    static void printTasks(TaskManager taskManager) {
        ArrayList<Task> tasks = taskManager.getTasks();

        if (tasks != null) {
            for (Task task : tasks) {
                System.out.println(task.toString());
            }
        }
    }

    static void printEpics(TaskManager taskManager) {
        ArrayList<Epic> epics = taskManager.getEpics();

        if (epics != null) {
            for (Epic epic : epics) {
                System.out.println(epic.toString());
            }
        }
    }

    static void printSubtasks(TaskManager taskManager) {
        ArrayList<Subtask> subtasks = taskManager.getSubtask();

        if (subtasks != null) {
            for (Subtask subtask : subtasks) {
                System.out.println(subtask.toString());
            }
        }
    }

    static void printEpicSubtask(TaskManager taskManager, int epicId) {
        ArrayList<Subtask> epicSubtasks = taskManager.getEpicSubtasks(epicId);

        if (epicSubtasks != null) {
            for (Subtask subtask: epicSubtasks) {
                System.out.println(subtask.toString());
            }
        }
    }
}
