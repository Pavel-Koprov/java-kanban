package dto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected String taskName;
    protected String taskDescription;
    protected int taskId;
    protected Status taskStatus;
    protected LocalDateTime startTime;
    protected Duration duration;

    public Task(String taskName, String taskDescription, int taskId, LocalDateTime startTime, Duration duration) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskId = taskId;
        this.taskStatus = Status.NEW;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String taskName, String taskDescription, Status taskStatus, LocalDateTime startTime, Duration duration) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String taskName, String taskDescription, int taskId, Status taskStatus,
                LocalDateTime startTime, Duration duration) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskId = taskId;
        this.taskStatus = taskStatus;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Status getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Status taskStatus) {
        this.taskStatus = taskStatus;
    }

    public TaskType getTaskType() {
        return TaskType.TASK;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId && Objects.equals(taskName, task.taskName) &&
                Objects.equals(taskDescription, task.taskDescription) && taskStatus == task.taskStatus &&
                Objects.equals(startTime, task.startTime) && Objects.equals(duration, task.duration);
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

    @Override
    public String toString() {
        String taskStatusForPrint = "";
        switch (taskStatus) {
            case NEW:
                taskStatusForPrint = "новая";
                break;
            case IN_PROGRESS:
                taskStatusForPrint = "в процессе решения";
                break;
            case DONE:
                taskStatusForPrint = "выполнена";
        }

        String startTimeToString = "";

        if (startTime != null) {
            startTimeToString = startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        return "Простая задача (" +
                "её номер: '" + taskId + '\'' +
                ", её название: '" + taskName + '\'' +
                ", её описание: '" + taskDescription + '\'' +
                ", её статус: " + taskStatusForPrint +
                ", её время начала: " + startTimeToString + '\'' +
                ", её продолжительность: " + duration.toMinutes() + " мин" + '\'' +
                ", её время конца: " + getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + '\'' +
                ')';
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
