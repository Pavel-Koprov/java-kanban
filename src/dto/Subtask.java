package dto;

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
}
