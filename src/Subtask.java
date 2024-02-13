class Subtask extends Task {
    private Integer idOfEpic;


    public Subtask(String nameOfTask, String descriptionOfTask, int idOfTask, Status statusOfTask, Integer idOfEpic) {
        super(nameOfTask, descriptionOfTask, idOfTask, statusOfTask);
        this.idOfEpic = idOfEpic;
    }

    public Subtask(String nameOfTask, String descriptionOfTask, Status statusOfTask) {
        super(nameOfTask, descriptionOfTask, statusOfTask);
    }

    @Override
    public String toString() {
        String statusOfTaskForPrint = "";
        switch (statusOfTask) {
            case NEW:
                statusOfTaskForPrint = "новая";
                break;
            case IN_PROGRESS:
                statusOfTaskForPrint = "в процессе решения";
                break;
            case DONE:
                statusOfTaskForPrint = "выполнена";
        }
        return "    Подзадача (" +
                "её название: '" + nameOfTask + '\'' +
                ", её описание: '" + descriptionOfTask + '\'' +
                ", её статус: " + statusOfTaskForPrint +
                ')';
    }

    public Integer getIdOfEpic() {
        return idOfEpic;
    }
}
