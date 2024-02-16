package dto;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> SubtasksId = new ArrayList<>();

    public Epic(String taskName, String taskDescription, Status taskStatus) {
        super(taskName, taskDescription, taskStatus);
    }

    public Epic(String taskName, String taskDescription, int taskId) {
        super(taskName, taskDescription, taskId);
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
                ", его статус: " + epicStatusForPrint +
                ')';
    }

    public void addSubtaskId(Integer subtaskId) {
        SubtasksId.add(subtaskId);
    }

    public void removeSubtaskId(Integer subtaskId) {
        SubtasksId.remove(subtaskId);
    }

    public void removeAllSubtasksId() {
        SubtasksId.clear();
    }

    public ArrayList<Integer> getSubtasksId() {
        return SubtasksId;
    }
}
