package dto;

public class Subtask extends Task {
    private int epicId;

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

    public void setEpicId(int epicId) {
        this.epicId = epicId;
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
                ", её статус: " + subtaskStatusForPrint +
                ')';
    }
}
