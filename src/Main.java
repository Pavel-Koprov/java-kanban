import dto.Epic;
import dto.Status;
import dto.Subtask;
import dto.Task;

import service.Managers;
import service.TaskManager;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefault();

        System.out.println("Вас приветствует трекер задач!");

        // 1. Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.
        Task task1 = new Task("taskName1",
                "taskDescription1", Status.IN_PROGRESS,
                LocalDateTime.now(), Duration.ofMinutes(1));
        int idOfTask1 = inMemoryTaskManager.saveTask(task1);

        Task task2 = new Task("taskName2",
                "taskDescription2", Status.NEW,
                LocalDateTime.now().plusMinutes(2), Duration.ofMinutes(1));
        int idOfTask2 = inMemoryTaskManager.saveTask(task2);

        Epic epic1 = new Epic("epicName1", "epicDescription1");
        int idOfEpic1 = inMemoryTaskManager.saveEpic(epic1);

        Subtask subtask1 = new Subtask("subtaskName1", "subtaskDescription1", Status.DONE,
                idOfEpic1, LocalDateTime.now().plusMinutes(4), Duration.ofMinutes(1));
        int idOfSubtask1 = inMemoryTaskManager.saveSubtask(subtask1);

        Subtask subtask2 = new Subtask("subtaskName2", "subtaskDescription2", Status.DONE,
                idOfEpic1, LocalDateTime.now().plusMinutes(6), Duration.ofMinutes(1));
        int idOfSubtask2 = inMemoryTaskManager.saveSubtask(subtask2);

        Epic epic2 = new Epic("epicName2", "epicDescription2");
        int idOfEpic2 = inMemoryTaskManager.saveEpic(epic2);

        Subtask subtask3 = new Subtask("subtaskName3", "subtaskDescription3",
                Status.IN_PROGRESS, idOfEpic2, LocalDateTime.now().plusMinutes(8), Duration.ofMinutes(1));
        int idOfSubtask3 = inMemoryTaskManager.saveSubtask(subtask3);

        // 2. Распечатайте списки эпиков, задач и подзадач
        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println(inMemoryTaskManager.getSubtasks());
        System.out.println(inMemoryTaskManager.getEpicSubtasks(idOfEpic1));
        System.out.println(inMemoryTaskManager.getEpicSubtasks(idOfEpic2));

        // 3. Измените статусы созданных объектов, распечатайте их
        Task newTask1 = new Task("newTaskName1", "newTaskDescription1", idOfTask1, Status.NEW,
                LocalDateTime.now().plusMinutes(10), Duration.ofMinutes(1));
        inMemoryTaskManager.updateTask(newTask1);

        Task newTask2 = new Task("newTaskName2", "newTaskDescription2", idOfTask2, Status.DONE,
                LocalDateTime.now().plusMinutes(12), Duration.ofMinutes(1));
        inMemoryTaskManager.updateTask(newTask2);

        Epic newEpic1 = new Epic("newEpicName1", "newEpicDescription1", idOfEpic1);
        inMemoryTaskManager.updateEpic(newEpic1);

        Subtask newSubtask1 = new Subtask("newSubtaskName1", "newSubtaskDescription1",
                idOfSubtask1, Status.IN_PROGRESS, idOfEpic1, LocalDateTime.now().plusMinutes(14), Duration.ofMinutes(1));
        inMemoryTaskManager.updateSubtask(newSubtask1);

        Subtask newSubtask2 = new Subtask("newSubtaskName2", "newSubtaskDescription2",
                idOfSubtask2, Status.NEW, idOfEpic1, LocalDateTime.now().plusMinutes(16), Duration.ofMinutes(1));
        inMemoryTaskManager.updateSubtask(newSubtask2);

        Epic newEpic2 = new Epic("newEpicName2", "newEpicDescription2", idOfEpic2);
        inMemoryTaskManager.updateEpic(newEpic2);

        Subtask newSubtask3 = new Subtask("newSubtaskName3", "newSubtaskDescription3",
                idOfSubtask3, Status.NEW, idOfEpic2, LocalDateTime.now().plusMinutes(18), Duration.ofMinutes(1));
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
        System.out.println(inMemoryTaskManager.getEpicSubtasks(idOfEpic2));
    }
}
