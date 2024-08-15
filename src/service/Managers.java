package service;

import java.io.File;

public abstract class Managers {

    public static TaskManager getDefault() {
        return getInMemoryTaskManager();
    }

    public static InMemoryTaskManager getInMemoryTaskManager() {
        return FileBackedTaskManager.loadFromFile(new File("Memory/StoringTasks.csv"));
    }

    public static FileBackedTaskManager getFileBackedTaskManager() {
        return FileBackedTaskManager.loadFromFile(new File("Memory/StoringTasks.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}


