package dto;

import java.util.Objects;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String taskName, String taskDescription, Status taskStatus, int epicId) {
        super(taskName, taskDescription, taskStatus);
        this.epicId = epicId;
    }

    public Subtask(String taskName, String taskDescription, int taskId, Status taskStatus, int epicId) {
        super(taskName, taskDescription, taskId, taskStatus);
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
        return "    Подзадача (" +
                "её номер: '" + taskId + '\'' +
                ", её название: '" + taskName + '\'' +
                ", её описание: '" + taskDescription + '\'' +
                ", её статус: " + subtaskStatusForPrint + '\'' +
                ", её эпик: " + epicId +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return taskId == subtask.taskId && Objects.equals(taskName, subtask.taskName) &&
                Objects.equals(taskDescription, subtask.taskDescription) && taskStatus == subtask.taskStatus
                && epicId == subtask.epicId;
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

        return hash;
    }
}
