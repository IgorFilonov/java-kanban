package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    // Узел двусвязного списка
    public static class Node<T> {
        T task;
        Node<T> next;
        Node<T> prev;

        public Node(Node<T> prev, T task, Node<T> next) {
            this.task = task;
            this.next = next;
            this.prev = prev;
        }
    }

    private Node<Task> head; // Голова списка
    private Node<Task> tail; // Хвост списка
    private final Map<Integer, Node<Task>> nodeMap = new HashMap<>();  // Мапа для  доступа к узлам по id

    // Добавление новой задачи
    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        Node<Task> node = nodeMap.get(task.getId());
        if (node != null) {
            removeNode(node);
        }
        linkLast(task);
    }

    // Удаление задачи по id
    @Override
    public void remove(int id) {
        Node<Task> node = nodeMap.get(id);
        if (node != null) {
            removeNode(node);
        }
    }

    // Преобразование списка в Лист для возвращения истории
    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node<Task> current = head;
        while (current != null) {
            history.add(current.task);
            current = current.next;
        }
        return history;
    }

    //  Метод для добавления задачи в конец списка
    private void linkLast(Task task) {
        Node<Task> newNode = new Node<>(tail, task, null);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        nodeMap.put(task.getId(), newNode);
    }

    //  Метод для удаления узла
    private void removeNode(Node<Task> node) {
        if (node == null) {
            return;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }

        nodeMap.remove(node.task.getId());
    }
}