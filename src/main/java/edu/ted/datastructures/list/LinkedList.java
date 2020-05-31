package edu.ted.datastructures.list;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

public class LinkedList<T> extends AbstractList<T> {
    private Node<T> tail;
    private Node<T> head;

    @Override
    public Iterator<T> iterator() {
        return this.new LinkedListIterator();
    }

    @Override
    public Object[] toArray() {
        T[] arrayToReturn = (T[]) new Object[size];
        int counter = 0;
        for (T element : this) {
            arrayToReturn[counter++] = element;
        }
        return arrayToReturn;
    }

    @Override
    public boolean remove(T value) {
        if (size == 0) {
            return false;
        }
        Node<T> currentNode = head;
        while (currentNode != null) {
            if (Objects.equals(currentNode.value, value)) {
                break;
            }
            currentNode = currentNode.next;
        }
        return remove(currentNode);
    }

    @Override
    public T remove(int index) {
        validateIndex(index);
        Node<T> nodeToRemove = findNode(index);
        remove(nodeToRemove);
        return nodeToRemove != null ? nodeToRemove.value : null;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> collection) {
        validateIndexForAdd(index);
        Node<T> nodeToAddBefore = findNode(index);
        for (T element : collection) {
            addInternal(nodeToAddBefore, element);
        }
        return true;
    }

    @Override
    public void clear() {
        tail = null;
        head = null;
        size = 0;
    }

    @Override
    public T get(int index) {
        validateIndex(index);
        Node<T> node = findNode(index);
        return node.value;
    }

    @Override
    public T set(T value, int index) {
        validateIfEmpty("Can`t apply set on empty list");
        validateIndex(index);
        Node<T> nodeToSet = findNode(index);
        T oldValue = nodeToSet.value;
        nodeToSet.value = value;
        return oldValue;
    }


    @Override
    public void add(T value, int index) {
        validateIndexForAdd(index);
        addInternal(findNode(index), value);
    }

    @Override
    public int indexOf(T value) {
        int index = 0;
        for (T element : this) {
            if (Objects.equals(element, value)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(T value) {
        if (tail == null) {
            return -1;
        }
        Node<T> node = tail;
        int index = size - 1;
        while (!Objects.equals(node.value, value) && node.prev != null) {
            index--;
            node = node.prev;
        }
        return Objects.equals(node.value, value) ? index : -1;
    }

    private Node<T> findNode(int index) {
        Node<T> node;
        if (index <= size / 2) {
            node = tail;
            for (int i = size - 1; i >= 0; i--) {
                if (i == index) {
                    return node;
                }
                node = node.prev;
            }
        } else {
            node = head;
            for (int i = 0; i < size; i++) {
                if (i == index) {
                    return node;
                }
                node = node.next;
            }
        }
        return node;
    }

    private boolean remove(Node<T> nodeToBeRemoved) {
        if (nodeToBeRemoved == null) {
            return false;
        }
        if (size == 1) {
            tail = head = null;
        } else if (head == nodeToBeRemoved) {
            head = nodeToBeRemoved.next;
            head.prev = null;
        } else if (tail == nodeToBeRemoved) {
            tail = nodeToBeRemoved.prev;
            tail.next = null;
        } else {
            nodeToBeRemoved.prev.next = nodeToBeRemoved.next;
            nodeToBeRemoved.next.prev = nodeToBeRemoved.prev;
        }
        size--;
        return true;
    }

    private class LinkedListIterator implements Iterator<T> {

        private Node<T> current = head;
        private Node<T> lastReturned;

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException("The element was already removed");
            }
            LinkedList.this.remove(lastReturned);
            lastReturned = null;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("All elements were fetched in this iterator");
            }
            lastReturned = current;
            T value = current.value;
            current = current.next;
            return value;
        }
    }

    private void addToTail(T value) {
        Node<T> newNode = new Node<>(value);
        if (tail == null) {
            head = tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    private void addInternal(Node<T> nodeToAddBefore, T value) {
        if (nodeToAddBefore == null) {
            addToTail(value);
            return;
        }
        Node<T> newNode = new Node<>(value);
        if (head == nodeToAddBefore) {
            newNode.next = head;
            head = newNode;
        } else {
            newNode.next = nodeToAddBefore;
            newNode.prev = nodeToAddBefore.prev;
            nodeToAddBefore.prev.next = newNode;
        }
        nodeToAddBefore.prev = newNode;
        size++;
    }

    private static class Node<T> {
        private Node<T> next;
        private Node<T> prev;
        private T value;

        private Node(T value) {
            this.value = value;
        }
    }
}
