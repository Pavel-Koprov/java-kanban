package test.HTTP;

import HTTP.HttpTaskServer;
import com.google.gson.Gson;
import dto.Status;
import dto.Task;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskServerTest {
    protected TaskManager manager = new InMemoryTaskManager();
    protected HttpTaskServer taskServer = new HttpTaskServer(manager);
    protected Gson gson = HttpTaskServer.getGson();
    protected HttpClient client = HttpClient.newHttpClient();

    protected HttpTaskServerTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
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
