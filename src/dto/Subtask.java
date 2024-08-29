package dto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.time.format.DateTimeFormatter;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String taskName, String taskDescription, Status taskStatus, int epicId,
                   LocalDateTime startTime, Duration duration) {
        super(taskName, taskDescription, taskStatus, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String taskName, String taskDescription, int taskId, Status taskStatus, int epicId,
                   LocalDateTime startTime, Duration duration) {
        super(taskName, taskDescription, taskId, taskStatus, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        String subtaskStatusForPrint = "";
        switch (taskStatus) {
            case NEW:
                subtaskStatusForPrint = "новая";
                break;
            case IN_PROGRESS:
                subtaskStatusForPrint = "в процессе решения";
                break;
            case DONE:
                subtaskStatusForPrint = "выполнена";
        }

        String startTimeToString = "";
        if (startTime != null) {
            startTimeToString = startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        return "    Подзадача (" +
                "её номер: '" + taskId + '\'' +
                ", её название: '" + taskName + '\'' +
                ", её описание: '" + taskDescription + '\'' +
                ", её статус: " + subtaskStatusForPrint + '\'' +
                ", её эпик: " + epicId +
                ", её время начала: " + startTimeToString + '\'' +
                ", её продолжительность: " + duration.toMinutes() + " мин" + '\'' +
                ", её время конца: " + getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + '\'' +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return taskId == subtask.taskId && Objects.equals(taskName, subtask.taskName) &&
                Objects.equals(taskDescription, subtask.taskDescription) && taskStatus == subtask.taskStatus
                && epicId == subtask.epicId && Objects.equals(startTime, subtask.startTime) &&
                Objects.equals(duration, subtask.duration);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (taskName != null) {
            hash = taskName.hashCode();
        }
        hash = hash * 31;

        if (taskDescription != null) {
            hash = hash + taskDescription.hashCode();
        }
        hash = hash * 31;

        if (taskStatus != null) {
            hash = hash + taskStatus.hashCode();
        }
        hash = hash + taskId;
        hash = hash + epicId;

        if (startTime != null) {
            hash = hash + startTime.hashCode();
        }
        hash = hash * 31;

        if (duration != null) {
            hash = hash + duration.hashCode();
        }
        hash = hash * 31;

        return hash;
    }
}
