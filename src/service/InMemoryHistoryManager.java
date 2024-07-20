package service;

import dto.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node> nodeMap = new HashMap<>();

    private Node head;
    private Node tail;

    public void add(Task task) {
        Node newNode = linkLast(task);
        remove(task.getTaskId());
        nodeMap.put(task.getTaskId(), newNode);
    }

    private Node linkLast(Task task) {
        Node newNode = new Node();

        if (this.head == null) {
            this.head = newNode;
            this.tail = newNode;
            newNode.data = task;
            newNode.prev = null;
            newNode.next = null;
        } else {
            newNode.prev = tail;
            newNode.data = task;
            newNode.next = null;

            newNode.prev.next = newNode;
            this.tail = newNode;
        }
        return newNode;
    }

    public void remove(int id) {
        Node node = nodeMap.remove(id);

        if (node == null) {
            return;
        }

        removeNode(node);
    }

    private void removeNode(Node node) {
        if (node.prev == null && node.next == null) {
            this.head = null;
            this.tail = null;
        } else if (node.prev == null) {
            node.next.prev = null;
            this.head = node.next;
        } else if (node.next == null) {
            node.prev.next = null;
            this.tail = node.prev;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = head;

        while (node != null) {
            tasks.add(node.data);
            node = node.next;
        }
        return tasks;
    }

    private static class Node {
        Node prev;
        Node next;
        Task data;
    }
}
