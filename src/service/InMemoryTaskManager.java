package service;

import dto.Epic;
import dto.Status;
import dto.Subtask;
import dto.Task;
import exceptions.TimeConflictException;

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
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    //2b. Удаление всех типов задач.
    @Override
    public void removeAllTasks() {
        tasks.values()
                .forEach(task -> {
                    inMemoryHistoryManager.remove(task.getTaskId());
                    prioritizedTasks.remove(task);
                });
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        epics.values()
                .forEach(epic -> {
                    inMemoryHistoryManager.remove(epic.getTaskId());

                    for (int subtaskId : epic.getSubtasksId()) {
                        prioritizedTasks.remove(subtasks.get(subtaskId));
                    }
                });
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void removeAllSubtasks() {
        subtasks.values()
                .forEach(subtask -> {
                    inMemoryHistoryManager.remove(subtask.getTaskId());
                    prioritizedTasks.remove(subtask);
                });
        subtasks.clear();
        epics.values()
                .forEach(epic -> {
                    epic.setStartTime(null);
                    epic.setEndTime(null);
                    epic.removeAllSubtasksId();
                });
    }

    //2c. Получение всех типов задач по идентификатору
    @Override
    public Task findTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            Task task = tasks.get(taskId);
            inMemoryHistoryManager.add(task);

            return task;
        }
        return null;
    }

    @Override
    public Epic findEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            inMemoryHistoryManager.add(epic);

            return epic;
        }
        return null;
    }

    @Override
    public Subtask findSubtask(int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            Subtask subtask = subtasks.get(subtaskId);
            inMemoryHistoryManager.add(subtask);

            return subtask;
        }
        return null;
    }

    // 2d. Сохранение всех типов задач
    @Override
    public int saveTask(Task task) throws TimeConflictException {
        if (isTimeConflict(task)) {
            throw new TimeConflictException(String.format("%s%s%s", "В это время (",
                    task.getStartTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), ") запланирована другая задача."));
        }
        int taskId = createId();

        task.setTaskId(taskId);
        tasks.put(taskId, task);
        prioritizedTasks.add(task);

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
        if (isTimeConflict(subtask)) {
            throw new TimeConflictException(String.format("%s%s%s", "В это время (",
                    subtask.getStartTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), ") запланирована другая задача."));
        }
        int subtaskId = createId();
        subtask.setTaskId(subtaskId);
        subtasks.put(subtaskId, subtask);
        prioritizedTasks.add(subtask);

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

        return subtaskId;
    }

    //2e. Обновление всех типов задач
    @Override
    public void updateTask(Task newTask) {
        if (tasks.containsKey(newTask.getTaskId()) && !isTimeConflict(newTask)) {
            prioritizedTasks.remove(tasks.get(newTask.getTaskId()));
            tasks.put(newTask.getTaskId(), newTask);
            prioritizedTasks.add(newTask);

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
        if (subtasks.containsKey(newSubtask.getTaskId()) && epics.containsKey(newSubtask.getEpicId())
            && !isTimeConflict(newSubtask)) {
            prioritizedTasks.remove(subtasks.get(newSubtask.getTaskId()));
            subtasks.put(newSubtask.getTaskId(), newSubtask);
            updateEpicStatus(epics.get(newSubtask.getEpicId()));
            updateStartTimeAndEndTimeForEpic(newSubtask.getEpicId());
            prioritizedTasks.add(newSubtask);
        }
    }

    //2f. Удаление всех типов задач по идентификатору
    @Override
    public void removeTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            prioritizedTasks.remove(tasks.get(taskId));
            tasks.remove(taskId);
            inMemoryHistoryManager.remove(taskId);
        }
    }

    @Override
    public void removeEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic removedEpic = epics.get(epicId);

            for (Integer subtaskId : removedEpic.getSubtasksId()) {
                prioritizedTasks.remove(subtasks.get(subtaskId));
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

            prioritizedTasks.remove(subtasks.get(subtaskId));
            subtasks.remove(subtaskId);
            inMemoryHistoryManager.remove(subtaskId);

            updateStartTimeAndEndTimeForEpic(epic.getTaskId());
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

    private void updateStartTimeAndEndTimeForEpic(int idEpic) {
        Epic epic = epics.get(idEpic);
        if (epic.getSubtasksId().isEmpty()) {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(Duration.ZERO);
        }
        if (epic != null) {
            LocalDateTime min = LocalDateTime.MAX;
            LocalDateTime max = LocalDateTime.MIN;
            Duration duration = Duration.ZERO;

            for (int subtaskId : epic.getSubtasksId()) {
                LocalDateTime startTime = subtasks.get(subtaskId).getStartTime();
                LocalDateTime endTime = subtasks.get(subtaskId).getEndTime();
                duration = duration.plus(subtasks.get(subtaskId).getDuration());

                if (startTime.isBefore(min)) {
                    min = startTime;
                } else if (endTime.isAfter(max)) {
                    max = endTime;
                }
            }
            epic.setStartTime(min);
            epic.setEndTime(max);
            epic.setDuration(duration);
        }
    }

    private boolean isTimeConflict(Task newTask) {
        return getPrioritizedTasks()
                .stream()
                .anyMatch(task -> (task.getStartTime().isEqual(newTask.getStartTime())
                        || task.getStartTime().isBefore(newTask.getStartTime()))
                        && task.getEndTime().isAfter(newTask.getEndTime())
                        || (newTask.getStartTime().isEqual(task.getStartTime())
                        || newTask.getStartTime().isBefore(task.getStartTime()))
                        && newTask.getEndTime().isAfter(task.getStartTime()));
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }
 }
