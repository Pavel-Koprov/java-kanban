package service;

import dto.Epic;
import dto.Status;
import dto.Subtask;
import dto.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected int id = 1;
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

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
    public List<Task> getTasks() {
        return tasks.values()
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public List<Epic> getEpics() {
        return epics.values()
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return subtasks.values()
                .stream()
                .collect(Collectors.toList());
    }

    //2b. Удаление всех типов задач.
    @Override
    public void removeAllTasks() {
        tasks.values()
                .stream()
                .map(Task::getTaskId)
                .forEach(inMemoryHistoryManager::remove);
        tasks.clear();
        updatePrioritizedTasks();
    }

    @Override
    public void removeAllEpics() {
        epics.values()
                .stream()
                .map(Epic::getTaskId)
                .forEach(inMemoryHistoryManager::remove);
        epics.clear();
        removeAllSubtasks();
        updatePrioritizedTasks();
    }

    @Override
    public void removeAllSubtasks() {
        subtasks.values()
                .stream()
                .map(Subtask::getTaskId)
                .forEach(inMemoryHistoryManager::remove);
        subtasks.clear();
        epics.values()
                .forEach(Epic::removeAllSubtasksId);
        updatePrioritizedTasks();
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
    public int saveTask(Task task) throws TimeConflictException {
        if (isTimeConflict(task)) {
            throw new TimeConflictException(String.format("%s%s%s", "В это время (",
                    task.getStartTime().format(DATE_TIME_FORMATTER), ") запланирована другая задача."));
        }
        int taskId = createId();

        task.setTaskId(taskId);
        tasks.put(taskId, task);
        updatePrioritizedTasks();

        return taskId;
    }

    @Override
    public int saveEpic(Epic epic) {
        int epicId = createId();

        epic.setTaskId(epicId);
        epics.put(epicId, epic);
        updatePrioritizedTasks();

        return epicId;
    }

    @Override
    public int saveSubtask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            return -1;
        }
        if (isTimeConflict(subtask)) {
            throw new TimeConflictException(String.format("%s%s%s", "В это время (",
                    subtask.getStartTime().format(DATE_TIME_FORMATTER), ") запланирована другая задача."));
        }
        int subtaskId = createId();
        subtask.setTaskId(subtaskId);
        subtasks.put(subtaskId, subtask);

        Epic epic = epics.get(subtask.getEpicId());

        epics.get(subtask.getEpicId()).addSubtaskId(subtaskId);
        updateEpicStatus(epic);

        if (epic.getStartTime() == null || epic.getStartTime().isAfter(subtask.getStartTime())) {
            epic.setStartTime(subtask.getStartTime());
        }
        if (epic.getEndTime() == null || epic.getEndTime().isBefore(subtask.getEndTime())) {
            epic.setEndTime(subtask.getEndTime());
        }
        epic.setDuration(epic.getDuration().plus(subtask.getDuration()));
        updatePrioritizedTasks();

        return subtaskId;
    }

    //2e. Обновление всех типов задач
    @Override
    public void updateTask(Task newTask) {
        if (tasks.containsKey(newTask.getTaskId())) {
            tasks.put(newTask.getTaskId(), newTask);
            updatePrioritizedTasks();
        }
    }

    @Override
    public void updateEpic(Epic newEpic) {
        if (epics.containsKey(newEpic.getTaskId())) {
            Epic savedEpic = epics.get(newEpic.getTaskId());

            savedEpic.setTaskName(newEpic.getTaskName());
            savedEpic.setTaskDescription(newEpic.getTaskDescription());

            updatePrioritizedTasks();
        }
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        if (subtasks.containsKey(newSubtask.getTaskId()) && epics.containsKey(newSubtask.getEpicId())) {
            subtasks.put(newSubtask.getTaskId(), newSubtask);
            updateEpicStatus(epics.get(newSubtask.getEpicId()));
            updatePrioritizedTasks();
        }
    }

    //2f. Удаление всех типов задач по идентификатору
    @Override
    public void removeTask(int taskId) {
        inMemoryHistoryManager.remove(taskId);
        tasks.remove(taskId);
        updatePrioritizedTasks();
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
            updatePrioritizedTasks();
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

            updateStartTimeAndEndTimeForEpic(epic.getTaskId());
            updatePrioritizedTasks();
        }
    }

    // 3a. Получение списка всех подзадач эпика
    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        if (!epics.containsKey(epicId)) {
            return null;
        }
        return epics.get(epicId).getSubtasksId()
                .stream()
                .map(subtasks::get)
                .collect(Collectors.toList());
    }

    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    private void updatePrioritizedTasks() {
        prioritizedTasks.clear();
        prioritizedTasks.addAll(tasks.values()
                .stream()
                .filter(task -> task.getStartTime().toLocalDate() != null)
                .collect(Collectors.toList()));
        prioritizedTasks.addAll(subtasks.values()
                .stream()
                .filter(subtask -> subtask.getStartTime().toLocalDate() != null)
                .collect(Collectors.toList()));
    }

    private void updateStartTimeAndEndTimeForEpic(int idEpic) {
        Epic epic = epics.get(idEpic);
        if (epic != null) {
            List<Subtask> subtaskIdList = epic.getSubtasksId()
                    .stream()
                    .map(subtasks::get)
                    .collect(Collectors.toList());
            LocalDateTime startTime = subtaskIdList
                    .stream()
                    .map(Subtask::getStartTime)
                    .filter(Objects::nonNull)
                    .min(LocalDateTime::compareTo)
                    .orElse(null);
            LocalDateTime endTime = subtaskIdList
                    .stream()
                    .map(subtask -> subtask.getStartTime().plus(subtask.getDuration()))
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
            epic.setStartTime(startTime);
            epic.setEndTime(endTime);
            Duration duration = subtaskIdList
                    .stream()
                    .map(Subtask::getDuration)
                    .filter(Objects::nonNull)
                    .reduce(Duration.ZERO, Duration::plus);
            epic.setDuration(duration);
        }
    }

    private boolean isTimeConflict(Task newTask) {
        return getPrioritizedTasks()
                .stream()
                .anyMatch(task -> !task.getEndTime().isBefore(newTask.getStartTime()) &&
                        !newTask.getEndTime().isBefore(task.getStartTime()));
    }

    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks
                .stream()
                .collect(Collectors.toList());
    }
 }
