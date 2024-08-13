package dto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final ArrayList<Integer> subtasksId = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription, Status.NEW, null, Duration.ofMinutes(0));
    }

    public Epic(String taskName, String taskDescription, int taskId) {
        super(taskName, taskDescription, taskId, null, Duration.ofMinutes(0));
        endTime = null;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    @Override
    public String toString() {
        String epicStatusForPrint = "";
        switch (taskStatus) {
            case NEW:
                epicStatusForPrint = "новый";
                break;
            case IN_PROGRESS:
                epicStatusForPrint = "в процессе решения";
                break;
            case DONE:
                epicStatusForPrint = "выполнен";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        String startTimeToString = "";
        String endTimeToString = "";

        if (startTime != null) {
            startTimeToString = startTime.format(formatter);
        }
        if (endTime != null) {
            endTimeToString = endTime.format(formatter);
        }
        return "Эпик (" +
                "его номер: '" + taskId + '\'' +
                ", его название: '" + taskName + '\'' +
                ", его описание: '" + taskDescription + '\'' +
                ", его статус: " + epicStatusForPrint + '\'' +
                ", его список подзадач: " + subtasksId + '\'' +
                ", его время начала: " + startTimeToString + '\'' +
                ", его продолжительность: " + duration.toMinutes() + " мин" + '\'' +
                ", его время конца: " + endTimeToString + '\'' +
                ')';
    }

    public void addSubtaskId(Integer subtaskId) {
        subtasksId.add(subtaskId);
    }

    public void removeSubtaskId(Integer subtaskId) {
        subtasksId.remove(subtaskId);
        setStartTime(null);
        setDuration(Duration.ofMinutes(0));
        setEndTime(null);
    }

    public void removeAllSubtasksId() {
        subtasksId.clear();
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return taskId == epic.taskId && Objects.equals(taskName, epic.taskName) &&
                Objects.equals(taskDescription, epic.taskDescription) && taskStatus == epic.taskStatus
                && Objects.equals(subtasksId, epic.subtasksId) && Objects.equals(startTime, epic.startTime) &&
                Objects.equals(duration, epic.duration);
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

        hash = hash + subtasksId.hashCode();

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
