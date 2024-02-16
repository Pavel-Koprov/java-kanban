import dto.Epic;
import dto.Status;
import dto.Subtask;
import dto.Task;
import service.TaskManager;

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

        Epic epic1 = new Epic("Работа", "");
        int idOfEpic1 = taskManager.saveEpic(epic1);

        Subtask subtask1 = new Subtask("График смен", "Составить график смен в феврале",
                Status.DONE, idOfEpic1);
        int idOfSubtask1 = taskManager.saveSubtask(subtask1);

        Subtask subtask2 = new Subtask("Расчёт зарплаты", "Рассчитать зарплату с учётом " +
                "количества смен и доплаты в утреннее время", Status.DONE, idOfEpic1);
        int idOfSubtask2 = taskManager.saveSubtask(subtask2);

        Epic epic2 = new Epic("Учёба", "");
        int idOfEpic2 = taskManager.saveEpic(epic2);

        Subtask subtask3 = new Subtask("тз4", "Сдать тз4 хотя бы до 17 февраля",
                Status.IN_PROGRESS, idOfEpic2);
        int idOfSubtask3 = taskManager.saveSubtask(subtask3);

        // 2. Распечатайте списки эпиков, задач и подзадач
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getEpicSubtasks(idOfEpic1));
        System.out.println(taskManager.getEpicSubtasks(idOfEpic2));

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

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getEpicSubtasks(idOfEpic1));
        System.out.println(taskManager.getEpicSubtasks(idOfEpic2));

        //4. Удалите по одной из каждого типа задач
        taskManager.removeTask(idOfTask2);
        taskManager.removeEpic(idOfEpic2);
        taskManager.removeSubtask(idOfSubtask1);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getEpicSubtasks(idOfEpic1));
        System.out.println(taskManager.getEpicSubtasks(idOfEpic2));
    }
}
