package service;

import dto.Epic;
import dto.Status;
import dto.Subtask;
import dto.Task;
import dto.TaskType;
import exceptions.ManagerException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    private static final String TYPE_OF_TASK_ENTRY = "id,type,name,status,description,startTime,duration,endTime," +
            "epic\n";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private void save() {
        try {
            if (!Files.exists(file.toPath())) {
                Files.createFile(file.toPath());
            }
        } catch (IOException e) {
            throw new ManagerException("Отсутствует файл для записи данных менеджера.");
        }

        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            writer.write(TYPE_OF_TASK_ENTRY);

            for (Task task : getTasks()) {
                String taskString = toString(task);

                if (taskString == null) {
                    throw new ManagerException("Ошибка при сохранении данных в файл.");
                }
                writer.write(taskString);
            }

            for (Epic epic : getEpics()) {
                String epicString = toString(epic);

                if (epicString == null) {
                    throw new ManagerException("Ошибка при сохранении данных в файл.");
                }
                writer.write(epicString);
            }

            for (Subtask subtask : getSubtasks()) {
                String subtaskString = toString(subtask);

                if (subtaskString == null) {
                    throw new ManagerException("Ошибка при сохранении данных в файл.");
                }
                writer.write(subtaskString);
            }
        } catch (IOException e) {
            throw new ManagerException("Ошибка при сохранении данных в файл.");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        int maxId = 0;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            bufferedReader.readLine();

            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (line.isEmpty()) {
                    break;
                }
                Task task = fromString(line);

                if (task == null) {
                    throw new ManagerException("Ошибка при считывании данных из файла.");
                }

                if (task.getTaskId() > maxId) {
                    maxId = task.getTaskId();
                }

                switch (task.getTaskType()) {
                    case TASK:
                        manager.tasks.put(task.getTaskId(), task);
                        manager.prioritizedTasks.add(task);
                        break;
                    case SUBTASK:
                        manager.subtasks.put(task.getTaskId(), (Subtask) task);
                        manager.prioritizedTasks.add(task);
                        break;
                    case EPIC:
                        manager.epics.put(task.getTaskId(), (Epic) task);
                        break;
                }
            }
        } catch (IOException e) {
            throw new ManagerException("Ошибка при считывании данных из файла.");
        }

        for (Subtask subtask : manager.subtasks.values()) {
            manager.epics.get(subtask.getEpicId()).addSubtaskId(subtask.getTaskId());
        }
        manager.id = (maxId);
        return manager;
    }

    private static String toString(Task task) {
        switch (task.getTaskType()) {
            case TASK:
                String[] taskString = {Integer.toString(task.getTaskId()),
                        task.getTaskType().toString(),
                        task.getTaskName(),
                        task.getTaskStatus().toString(),
                        task.getTaskDescription(),
                        task.getStartTime() != null ? task.getStartTime()
                                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "",
                        Long.toString(task.getDuration().toMinutes()),
                        task.getEndTime() != null ? task.getEndTime()
                                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "",
                        null};
                return (String.join(",", taskString) + "\n");
            case SUBTASK:
                Subtask subtask = (Subtask) task;
                String[] subtaskString = {Integer.toString(subtask.getTaskId()),
                        subtask.getTaskType().toString(),
                        subtask.getTaskName(),
                        subtask.getTaskStatus().toString(),
                        subtask.getTaskDescription(),
                        subtask.getStartTime() != null ? subtask.getStartTime()
                                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "",
                        Long.toString(subtask.getDuration().toMinutes()),
                        subtask.getEndTime() != null ? subtask.getEndTime()
                                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "",
                        Integer.toString(subtask.getEpicId())};
                return (String.join(",", subtaskString) + "\n");
            case EPIC:
                Epic epic = (Epic) task;
                String[] epicString = {Integer.toString(epic.getTaskId()),
                        epic.getTaskType().toString(),
                        epic.getTaskName(),
                        epic.getTaskStatus().toString(),
                        epic.getTaskDescription(),
                        epic.getStartTime() != null ? epic.getStartTime()
                                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "",
                        Long.toString(epic.getDuration().toMinutes()),
                        epic.getEndTime() != null ? epic.getEndTime()
                                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "",
                        null};
                return (String.join(",", epicString) + "\n");
            default:
                return null;
        }
    }

    private static Task fromString(String line) {
        String[] file = line.split(",");
        int id = Integer.parseInt(file[0]);
        TaskType taskType = TaskType.valueOf(file[1]);
        String taskName = file[2];
        Status taskStatus = Status.valueOf(file[3].toUpperCase());
        String taskDescription = file[4];
        LocalDateTime startTime = null;
        if (!file[5].isBlank()) {
            startTime = LocalDateTime.parse(file[5], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        Duration duration = Duration.ofMinutes(Integer.parseInt(file[6]));
        LocalDateTime endTime = null;
        if (!file[7].isBlank()) {
            endTime = LocalDateTime.parse(file[7], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

        Integer epicId = null;

        switch (taskType) {
            case SUBTASK:
                epicId = Integer.parseInt(file[8]);
                Subtask subtask = new Subtask(taskName, taskDescription, id, taskStatus, epicId, startTime, duration);
                subtask.setTaskId(id);
                return subtask;
            case EPIC:
                Epic epic = new Epic(taskName, taskDescription, id);
                epic.setTaskStatus(taskStatus);
                epic.setStartTime(startTime);
                epic.setDuration(duration);
                epic.setEndTime(endTime);
                return epic;
            case TASK:
                Task task = new Task(taskName, taskDescription, id, taskStatus, startTime, duration);
                task.setTaskId(id);
                return task;
            default:
                return null;
        }
    }


    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public int saveTask(Task task) {
        super.saveTask(task);
        save();
        return task.getTaskId();
    }

    @Override
    public int saveEpic(Epic epic) {
        super.saveEpic(epic);
        save();
        return epic.getTaskId();
    }

    @Override
    public int saveSubtask(Subtask subtask) {
        super.saveSubtask(subtask);
        save();
        return subtask.getTaskId();
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        super.updateSubtask(newSubtask);
        save();
    }

    @Override
    public void removeTask(int taskId) {
        super.removeTask(taskId);
        save();
    }

    @Override
    public void removeEpic(int epicId) {
        super.removeEpic(epicId);
        save();
    }

    @Override
    public void removeSubtask(int subtaskId) {
        super.removeSubtask(subtaskId);
        save();
    }
}
