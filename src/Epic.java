import java.util.ArrayList;

class Epic extends Task {
    private ArrayList<Integer> idOfSubtasks = new ArrayList<>();
    public Epic(String nameOfTask, String descriptionOfTask, int idOfTask, Status statusOfTask) {
        super(nameOfTask, descriptionOfTask, idOfTask, statusOfTask);
    }

    public Epic(String nameOfTask, String descriptionOfTask) {
        super(nameOfTask, descriptionOfTask);
    }

    @Override
    public String toString() {
        String statusOfTaskForPrint = "";
        switch (statusOfTask) {
            case NEW:
                statusOfTaskForPrint = "новый";
                break;
            case IN_PROGRESS:
                statusOfTaskForPrint = "в процессе решения";
                break;
            case DONE:
                statusOfTaskForPrint = "выполнен";
        }
        return "Эпик (" +
                "его название: '" + nameOfTask + '\'' +
                ", его описание: '" + descriptionOfTask + '\'' +
                ", его статус: " + statusOfTaskForPrint +
                ')';
    }

    public void addIdOfSubtasks(Integer idOfSubtask) {
        this.idOfSubtasks.add(idOfSubtask);
    }

    public void removeIdOfSubtasks(Integer idOfSubtask) { //проверить, удаляется индекс или значение
        int indexOfId = idOfSubtasks.indexOf(idOfSubtask);
        this.idOfSubtasks.remove(indexOfId);
    }

    public ArrayList<Integer> getIdOfSubtasks() {
        return idOfSubtasks;
    }
}
