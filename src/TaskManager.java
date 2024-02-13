import java.util.ArrayList;
import java.util.HashMap;

class TaskManager {
    private int id = 1;
    HashMap<Integer, Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, Subtask> subtasks;

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
    }

    private Integer createId() {
        id++;
        return id;
    }

    void getListOfEpics() {
        for (Epic epic : epics.values()) {
            System.out.println(epic.getIdOfTask() + " - " + epic.getNameOfTask());
        }
    }

    void getListOfTasks() {
        for (Task task : tasks.values()) {
            System.out.println(task.getIdOfTask() + " - " + task.getNameOfTask());
        }
    }

    void getListOfEpicsAndSubtasks() {
        for (Epic epic : epics.values()) {
            System.out.println(epic.getNameOfTask());

            for (Integer id : epic.getIdOfSubtasks()) {
                System.out.println("  " + subtasks.get(id).getIdOfTask() + " - " + subtasks.get(id).getNameOfTask());
            }
        }
    }
    void saveTask(Task task) {
        Integer idOfTask = createId();
        tasks.put(idOfTask, new Task(task.nameOfTask, task.descriptionOfTask, idOfTask, task.statusOfTask));
    }

    void saveEpic(Epic epic) {
        Integer idOfEpic = createId();
        epics.put(idOfEpic, new Epic(epic.nameOfTask, epic.descriptionOfTask, idOfEpic, Status.NEW));
    }

    void saveSubtask(Subtask subtask, String epicId) {

        for (Epic epic : epics.values()) {
            String idOfEpic = epic.getIdOfTask().toString();

            if (epicId.equals(idOfEpic)) {
                Integer idOfSubtask = createId();
                subtasks.put(idOfSubtask, new Subtask(subtask.nameOfTask, subtask.descriptionOfTask, idOfSubtask,
                        subtask.statusOfTask, epic.idOfTask));
                epic.addIdOfSubtasks(idOfSubtask);
                updateEpicStatus(subtasks.get(idOfSubtask).getIdOfEpic());
            }
        }
    }

    int findEpic(String epicId) {
        for (Epic epic : epics.values()) {

            if (epicId.equals(epic.getIdOfTask().toString())) {
                return epic.getIdOfTask();
            }
        }
        return 0;
    }

    void updateEpicStatus(Integer idOfEpic) {
        Status status = Status.NEW;
        int countDoneSubtask = 0;

        for (Integer id : epics.get(idOfEpic).getIdOfSubtasks()) {
            if (subtasks.get(id).getStatusOfTask() == Status.IN_PROGRESS) {
                status = Status.IN_PROGRESS;
            } else if (subtasks.get(id).getStatusOfTask() == Status.DONE) {
                countDoneSubtask++;
            }
        }

        if (countDoneSubtask == epics.get(idOfEpic).getIdOfSubtasks().size()) {
            status = Status.DONE;
        } else if (countDoneSubtask < epics.get(idOfEpic).getIdOfSubtasks().size() && countDoneSubtask > 0) {
            status = Status.IN_PROGRESS;
        }
        epics.get(idOfEpic).setStatusOfTask(status);
    }

    void printEpicAndSubtasks(Integer epicId) {
        ArrayList<Integer> subtasksId = epics.get(epicId).getIdOfSubtasks();
        System.out.println(epics.get(epicId).toString());
        for (Integer idOfSubtask : subtasksId) {
            System.out.println(subtasks.get(idOfSubtask).toString());
        }
    }

    void printTasks() {
        for (Task task : tasks.values()) {
            System.out.println(task.toString());
        }
    }

    void removeAllTasks() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        id = 1;
    }

    boolean removeTask(String idOfTask) {
        boolean findIdForTask = false;
        int taskId = 0;

        for (Integer id : tasks.keySet()) {
            if (id.toString().equals(idOfTask)) {
                findIdForTask = true;
                taskId = id;
            }
        }

        if (findIdForTask) {
            tasks.remove(taskId);
        }
        return findIdForTask;
    }

    boolean removeEpic(String idOfEpic) {
        boolean findIdForEpic = false;
        int epicId = 0;

        for (Integer id : epics.keySet()) {
            if (id.toString().equals(idOfEpic)) {
                findIdForEpic = true;
                epicId = id;
            }
        }
        if (findIdForEpic) {
            for (int i = 0; i < epics.get(epicId).getIdOfSubtasks().size(); i++) {
                subtasks.remove(epics.get(epicId).getIdOfSubtasks().get(i));
            }
            epics.remove(epicId);
        }
        return findIdForEpic;
    }

    boolean removeSubtask(String idOfSubtask) {
        boolean findIdForSubtask = false;
        int subtaskId = 0;

        for (Integer id : subtasks.keySet()) {
            if (id.toString().equals(idOfSubtask)) {
                findIdForSubtask = true;
                subtaskId = id;
            }
        }
        if (findIdForSubtask) {
            Epic epic = epics.get(subtasks.get(subtaskId).getIdOfEpic());
            epic.removeIdOfSubtasks(Integer.parseInt(idOfSubtask));
            subtasks.remove(subtaskId);
            updateEpicStatus(epic.getIdOfTask());
        }
        return findIdForSubtask;
    }

    boolean updateTask(String idOfTask, Task task) {
        boolean findIdForTask = false;

        for (Integer id : tasks.keySet()) {
            if (id.toString().equals(idOfTask)) {
                findIdForTask = true;
                tasks.put(id, new Task(task.nameOfTask, task.descriptionOfTask, id, task.statusOfTask));
            }
        }
        return findIdForTask;
    }

    boolean updateEpic(String idOfEpic, Epic epic) {
        boolean findIdForEpic = false;

        for (Integer id : epics.keySet()) {
            if (id.toString().equals(idOfEpic)) {
                findIdForEpic = true;
                epics.put(id, new Epic(epic.nameOfTask, epic.descriptionOfTask, id, epic.statusOfTask));

                for (Subtask subtask : subtasks.values()) {
                    if (id == subtask.getIdOfEpic()) {
                        epics.get(id).addIdOfSubtasks(subtask.idOfTask);
                    }
                }
                updateEpicStatus(epics.get(id).getIdOfTask());
            }
        }
        return findIdForEpic;
    }

    boolean updateSubtask(String idOfSubtask, Subtask subtask) {
        boolean findIdForSubtask = false;

        for (Integer id : subtasks.keySet()) {
            if (id.toString().equals(idOfSubtask)) {
                findIdForSubtask = true;
                int idOfEpic = subtasks.get(id).getIdOfEpic();
                subtasks.put(id, new Subtask(subtask.nameOfTask, subtask.descriptionOfTask, id, subtask.statusOfTask,
                        idOfEpic));
                updateEpicStatus(idOfEpic);
            }
        }
        return findIdForSubtask;
    }

    boolean updateTaskStatus(String idOfTask, Status newStatusOfTask) {
        boolean findIdForTask = false;

        for (Integer id : tasks.keySet()) {
            if (id.toString().equals(idOfTask)) {
                findIdForTask = true;
                tasks.get(id).setStatusOfTask(newStatusOfTask);
            }
        }
        return findIdForTask;
    }

    boolean updateSubtaskStatus(String idOfSubtask, Status newStatusOfSubtask) {
        boolean findIdForSubtask = false;

        for (Integer id : subtasks.keySet()) {
            if (id.toString().equals(idOfSubtask)) {
                findIdForSubtask = true;
                subtasks.get(id).setStatusOfTask(newStatusOfSubtask);
                updateEpicStatus(epics.get(subtasks.get(id).getIdOfEpic()).getIdOfTask());
            }
        }
        return findIdForSubtask;
    }
}
