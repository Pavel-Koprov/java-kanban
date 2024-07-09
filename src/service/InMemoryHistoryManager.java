package service;

import dto.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node> nodeMap = new HashMap<>();

    private Node head;
    private Node tail;

    public void add(Task task) {
        remove(task.getTaskId());
        Node newNode = linkLast(task);
        nodeMap.put(task.getTaskId(), newNode);

    }

    private Node linkLast(Task task) {
        Node newNode = new Node();

        if (this.head == null) {
            this.head = newNode;
            this.tail = newNode;
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
            this.head = node;
        } else if (node.next == null) {
            node.prev.next = null;
            this.tail = node;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> taskHistory = new ArrayList<>();
        if (head == null) {
            return taskHistory;
        }
        taskHistory.add(head.data);
        Node nextNode = head.next;

        while(nextNode != null) {
            taskHistory.add(nextNode.data);
        }
        return taskHistory;
    }

    private static class Node {
        Node prev;
        Node next;
        Task data;
    }
}
