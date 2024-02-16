package dto;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtasksId = new ArrayList<>();

    // Удалил возможность передачи эпику статуса извне
    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription, Status.NEW);
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
}
