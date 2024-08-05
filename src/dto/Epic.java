package dto;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription, Status.NEW);
    }

    public Epic(String taskName, String taskDescription, int taskId) {
        super(taskName, taskDescription, taskId);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
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
        return "Эпик (" +
                "его номер: '" + taskId + '\'' +
                ", его название: '" + taskName + '\'' +
                ", его описание: '" + taskDescription + '\'' +
                ", его статус: " + epicStatusForPrint + '\'' +
                ", его список подзадач: " + subtasksId +
                ')';
    }

    public void addSubtaskId(Integer subtaskId) {
        subtasksId.add(subtaskId);
    }

    public void removeSubtaskId(Integer subtaskId) {
        subtasksId.remove(subtaskId);
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
                && Objects.equals(subtasksId, epic.subtasksId);
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

        if (subtasksId != null) {
            hash = hash + subtasksId.hashCode();
        }

        return hash;
    }
}
