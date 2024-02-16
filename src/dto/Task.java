package dto;

import java.util.Objects;

public class Task {
    protected String taskName;
    protected String taskDescription;
    protected int taskId;
    protected Status taskStatus;

    public Task(String taskName, String taskDescription, int taskId) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskId = taskId;
        this.taskStatus = Status.NEW;
    }

    public Task(String taskName, String taskDescription, Status taskStatus) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
    }

    public Task(String taskName, String taskDescription, int taskId, Status taskStatus) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskId = taskId;
        this.taskStatus = taskStatus;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId && Objects.equals(taskName, task.taskName) &&
                Objects.equals(taskDescription, task.taskDescription) && taskStatus == task.taskStatus;
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
        return "Простая задача (" +
                "её номер: '" + taskId + '\'' +
                ", её название: '" + taskName + '\'' +
                ", её описание: '" + taskDescription + '\'' +
                ", её статус: " + taskStatusForPrint +
                ')';
    }
}
