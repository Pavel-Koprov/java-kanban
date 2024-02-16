package service;

import dto.Epic;
import dto.Status;
import dto.Subtask;
import dto.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private int createId() {
        return id++;
    }

    private void updateEpicStatus(Epic epic) {

        if (epic.getSubtasksId().isEmpty()) {
            return;
        }
        int countInProgressSubtask = 0;
        int countDoneSubtask = 0;

        for (int id : epic.getSubtasksId()) {
            Status subtaskStatus = subtasks.get(id).getTaskStatus();

            if (subtaskStatus == Status.IN_PROGRESS) {
                countInProgressSubtask++;
            } else if (subtaskStatus == Status.DONE) {
                countDoneSubtask++;
            }
        }
        int lengthOfListSubtasks = epic.getSubtasksId().size();

        if (countDoneSubtask == lengthOfListSubtasks) {
            epic.setTaskStatus(Status.DONE);
        } else if (countDoneSubtask > 0 || countInProgressSubtask > 0) {
            epic.setTaskStatus(Status.IN_PROGRESS);
        } else {
            epic.setTaskStatus(Status.NEW);
        }
    }

    // 2a. Получение списка всех типов задач
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    //2b. Удаление всех типов задач.
    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void removeAllSubtasks() {
        subtasks.clear();

        for (Epic epic : epics.values()) {
            epic.removeAllSubtasksId();
        }
    }

    //2c. Получение всех типов задач по идентификатору
    public Task findTask(int taskId) {
        if (!tasks.containsKey(taskId)) {
            return null;
        }
        return tasks.get(taskId);
    }

    public Epic findEpic(int epicId) {
        if (!epics.containsKey(epicId)) {
            return null;
        }
        return epics.get(epicId);
    }

    public Subtask findSubtask(int subtaskId) {
        if (!subtasks.containsKey(subtaskId)) {
            return null;
        }
        return subtasks.get(subtaskId);
    }

    // 2d. Сохранение всех типов задач
    public int saveTask(Task task) {
        int taskId = createId();

        task.setTaskId(taskId);
        tasks.put(taskId, task);

        return taskId;
    }

    public int saveEpic(Epic epic) {
        int epicId = createId();

        epic.setTaskId(epicId);
        epics.put(epicId, epic);

        return epicId;
    }

    public int saveSubtask(Subtask subtask) {
        int subtaskId = createId();
        subtask.setTaskId(subtaskId);
        subtasks.put(subtaskId, subtask);

        Epic epic = epics.get(subtask.getEpicId());

        epic.addSubtaskId(subtaskId);
        updateEpicStatus(epic);

        return subtaskId;
    }

    //2e. Обновление всех типов задач
    public void updateTask(Task newTask) {
        if (tasks.containsKey(newTask.getTaskId())) {
            Task savedTask = tasks.get(newTask.getTaskId());

            savedTask.setTaskName(newTask.getTaskName());
            savedTask.setTaskDescription(newTask.getTaskDescription());
            savedTask.setTaskStatus(newTask.getTaskStatus());
        }
    }

    public void updateEpic(Epic newEpic) {
        if (epics.containsKey(newEpic.getTaskId())) {
            Epic savedEpic = epics.get(newEpic.getTaskId());

            savedEpic.setTaskName(newEpic.getTaskName());
            savedEpic.setTaskDescription(newEpic.getTaskDescription());
        }
    }

    public void updateSubtask(Subtask newSubtask) {
        if (subtasks.containsKey(newSubtask.getTaskId())) {
            Subtask savedSubtask = subtasks.get(newSubtask.getTaskId());

            savedSubtask.setTaskName(newSubtask.getTaskName());
            savedSubtask.setTaskDescription(newSubtask.getTaskDescription());

            Epic savedEpic = epics.get(savedSubtask.getEpicId());

            savedSubtask.setEpicId(newSubtask.getEpicId());

            savedEpic.removeSubtaskId(savedSubtask.getTaskId());
            updateEpicStatus(savedEpic);

            Epic newEpic = epics.get(savedSubtask.getEpicId());

            savedSubtask.setTaskStatus(newSubtask.getTaskStatus());

            newEpic.addSubtaskId(savedSubtask.getTaskId());
            updateEpicStatus(newEpic);
        }
    }

    //2f. Удаление всех типов задач по идентификатору
    public void removeTask(int taskId) {
        tasks.remove(taskId);
    }

    public void removeEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic removedEpic = epics.get(epicId);

            for (Integer subtaskId : removedEpic.getSubtasksId()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(epicId);
        }
    }

    public void removeSubtask(int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            Subtask removedSubtask = subtasks.get(subtaskId);
            Epic epic = epics.get(removedSubtask.getEpicId());

            epic.removeSubtaskId(subtaskId);
            updateEpicStatus(epic);

            subtasks.remove(subtaskId);
        }
    }

    // 3a. Получение списка всех подзадач эпика
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {

        if (!epics.containsKey(epicId)) {
            return null;
        }
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        ArrayList<Integer> subtasksIdOfEpic = epics.get(epicId).getSubtasksId();

        for (Integer id : subtasksIdOfEpic) {
            epicSubtasks.add(subtasks.get(id));
        }
        return epicSubtasks;
    }
}
