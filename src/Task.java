class Task {
    protected String nameOfTask;
    protected String descriptionOfTask;
    protected Integer idOfTask;
    protected Status statusOfTask;

    public Task(String nameOfTask, String descriptionOfTask, int idOfTask, Status statusOfTask) {
        this.nameOfTask = nameOfTask;
        this.descriptionOfTask = descriptionOfTask;
        this.idOfTask = idOfTask;
        this.statusOfTask = statusOfTask;
    }

    public Task(String nameOfTask, String descriptionOfTask, Status statusOfTask) {
        this.nameOfTask = nameOfTask;
        this.descriptionOfTask = descriptionOfTask;
        this.statusOfTask = statusOfTask;
    }

    public Task(String nameOfTask, String descriptionOfTask) {
        this.nameOfTask = nameOfTask;
        this.descriptionOfTask = descriptionOfTask;
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
        return "Простая задача (" +
                "её название: '" + nameOfTask + '\'' +
                ", её описание: '" + descriptionOfTask + '\'' +
                ", её статус: " + statusOfTaskForPrint +
                ')';
    }

    public String getNameOfTask() {
        return nameOfTask;
    }

    public Integer getIdOfTask() {
        return idOfTask;
    }

    public void setStatusOfTask(Status statusOfTask) {
        this.statusOfTask = statusOfTask;
    }

    public Status getStatusOfTask() {
        return statusOfTask;
    }
}
