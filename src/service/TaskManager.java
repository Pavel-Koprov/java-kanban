package service;

import dto.Epic;
import dto.Subtask;
import dto.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    // 2a. Получение списка всех типов задач
    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    //2b. Удаление всех типов задач.
    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    //2c. Получение всех типов задач по идентификатору
    Task findTask(int taskId);

    Epic findEpic(int epicId);

    Subtask findSubtask(int subtaskId);

    // 2d. Сохранение всех типов задач
    int saveTask(Task task);

    int saveEpic(Epic epic);

    int saveSubtask(Subtask subtask);

    //2e. Обновление всех типов задач
    void updateTask(Task newTask);

    void updateEpic(Epic newEpic);

    void updateSubtask(Subtask newSubtask);

    //2f. Удаление всех типов задач по идентификатору
    void removeTask(int taskId);

    void removeEpic(int epicId);

    void removeSubtask(int subtaskId);

    // 3a. Получение списка всех подзадач эпика
    List<Subtask> getEpicSubtasks(int epicId);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}
