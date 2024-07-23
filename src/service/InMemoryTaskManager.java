package service;

import dto.Epic;
import dto.Status;
import dto.Subtask;
import dto.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    private int createId() {
        return id++;
    }

    private void updateEpicStatus(Epic epic) {

        if (epic.getSubtasksId().isEmpty()) {
            epic.setTaskStatus(Status.NEW);
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
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    //2b. Удаление всех типов задач.
    @Override
    public void removeAllTasks() {
        for (Integer taskId : tasks.keySet()) {
            inMemoryHistoryManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {

        for (Integer epicId : epics.keySet()) {
            inMemoryHistoryManager.remove(epicId);
        }

        for (Integer subtaskId : subtasks.keySet()) {
            inMemoryHistoryManager.remove(subtaskId);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        for (Integer subtaskId : subtasks.keySet()) {
            inMemoryHistoryManager.remove(subtaskId);
        }
        subtasks.clear();

        for (Epic epic : epics.values()) {
            epic.removeAllSubtasksId();
            epic.setTaskStatus(Status.NEW);
        }
    }

    //2c. Получение всех типов задач по идентификатору
    @Override
    public Task findTask(int taskId) {
        Task task = tasks.get(taskId);
        inMemoryHistoryManager.add(task);

        return task;
    }

    @Override
    public Epic findEpic(int epicId) {
        Epic epic = epics.get(epicId);
        inMemoryHistoryManager.add(epic);

        return epic;
    }

    @Override
    public Subtask findSubtask(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        inMemoryHistoryManager.add(subtask);

        return subtask;
    }

    // 2d. Сохранение всех типов задач
    @Override
    public int saveTask(Task task) {
        int taskId = createId();

        task.setTaskId(taskId);
        tasks.put(taskId, task);

        return taskId;
    }

    @Override
    public int saveEpic(Epic epic) {
        int epicId = createId();

        epic.setTaskId(epicId);
        epics.put(epicId, epic);

        return epicId;
    }

    @Override
    public int saveSubtask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            return -1;
        }
        int subtaskId = createId();
        subtask.setTaskId(subtaskId);
        subtasks.put(subtaskId, subtask);

        Epic epic = epics.get(subtask.getEpicId());

        epics.get(subtask.getEpicId()).addSubtaskId(subtaskId);
        updateEpicStatus(epic);

        return subtaskId;
    }

    //2e. Обновление всех типов задач
    @Override
    public void updateTask(Task newTask) {
        if (tasks.containsKey(newTask.getTaskId())) {
            tasks.put(newTask.getTaskId(), newTask);
        }
    }

    @Override
    public void updateEpic(Epic newEpic) {
        if (epics.containsKey(newEpic.getTaskId())) {
            Epic savedEpic = epics.get(newEpic.getTaskId());

            savedEpic.setTaskName(newEpic.getTaskName());
            savedEpic.setTaskDescription(newEpic.getTaskDescription());
        }
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        if (subtasks.containsKey(newSubtask.getTaskId()) && epics.containsKey(newSubtask.getEpicId())) {
            subtasks.put(newSubtask.getTaskId(), newSubtask);
            updateEpicStatus(epics.get(newSubtask.getEpicId()));
        }
    }

    //2f. Удаление всех типов задач по идентификатору
    @Override
    public void removeTask(int taskId) {
        inMemoryHistoryManager.remove(taskId);
        tasks.remove(taskId);
    }

    @Override
    public void removeEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic removedEpic = epics.get(epicId);

            for (Integer subtaskId : removedEpic.getSubtasksId()) {
                subtasks.remove(subtaskId);
                inMemoryHistoryManager.remove(subtaskId);
            }
            epics.remove(epicId);
            inMemoryHistoryManager.remove(epicId);
        }
    }

    @Override
    public void removeSubtask(int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            Subtask removedSubtask = subtasks.get(subtaskId);
            Epic epic = epics.get(removedSubtask.getEpicId());

            epic.removeSubtaskId(subtaskId);
            updateEpicStatus(epic);

            subtasks.remove(subtaskId);
            inMemoryHistoryManager.remove(subtaskId);
        }
    }

    // 3a. Получение списка всех подзадач эпика
    @Override
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

    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }
 }
