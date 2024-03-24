package service;

import dto.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> taskHistory = new ArrayList<>();

    public void add(Task task) {

        if (taskHistory.size() == 10) {
            taskHistory.remove(0);
        }
        Task copyTask = new Task(task.getTaskName(), task.getTaskDescription(), task.getTaskId(), task.getTaskStatus());
        taskHistory.add(copyTask);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return taskHistory;
    }
}
