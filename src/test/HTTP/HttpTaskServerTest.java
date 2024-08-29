package test.HTTP;

import http.HttpTaskServer;
import com.google.gson.Gson;
import service.InMemoryTaskManager;
import service.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.io.IOException;
import java.net.http.HttpClient;

public class HttpTaskServerTest {
    protected TaskManager manager;
    protected HttpTaskServer taskServer;
    protected Gson gson;
    protected HttpClient client;

    @BeforeEach
    void setUp() throws IOException {
        manager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(manager);
        gson = HttpTaskServer.getGson();
        client = HttpClient.newHttpClient();

        manager.removeAllTasks();
        manager.removeAllSubtasks();
        manager.removeAllEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }
}
