package edu.ted.datastructures.list;


import edu.ted.datastructures.list.interfaces.ExtendedList;
import edu.ted.datastructures.list.interfaces.List;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class LinkedList<T> implements List<T>, ExtendedList<T>, Iterable<T> {
    private Node<T> tail;
    private Node<T> head;
    private int size = 0;

    private static class Node<T> {
        private Node<T> next;
        private Node<T> prev;
        private T value;

        public Node(Node<T> next, Node<T> prev, T value) {
            this.next = next;
            this.prev = prev;
            this.value = value;
        }

        public Node(T value) {
            this.value = value;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] arrayToReturn = new Object[size];
        int counter = 0;
        for (T element : this) {
            arrayToReturn[counter++] = element;
        }
        return arrayToReturn;
    }

    private boolean addInternal(T t) {
        Node<T> newNode = new Node<T>(t);
        if (tail == null) {
            head = tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        size++;
        return true;
    }

    private void addInternal(Node<T> node, T t) {
        if (node == null) {
            addInternal(t);
            return;
        }
        Node<T> newNode = new Node<T>(node, node.prev, t);
        if (head == node) {
            head = newNode;
        } else {
            node.prev.next = newNode;
        }
        node.prev = newNode;
        size++;
    }


    @Override
    public void add(T t) {
        add(t, size);
    }

    private Node<T> findNode(T val) {
        if (head == null) {
            return null;
        }
        Node<T> node1 = head;
        int counter = 0;
        while (!Objects.equals(node1.value, val) && node1.next != null) {
            counter++;
            node1 = node1.next;
        }
        return Objects.equals(node1.value, val) ? node1 : null;
    }

    private Node<T> findNodeFromBegin(int idx) {
        Node<T> node = head;
        int counter = 0;
        while (counter < idx) {
            counter++;
            node = node.next;
        }
        return node;
    }

    private Node<T> findNodeFromEnd(int idx) {
        Node<T> node = tail;
        int counter = size - 1;
        while (counter > idx) {
            counter--;
            node = node.prev;
        }
        return node;
    }

    private Node<T> findNode(int idx) {
        if (idx <= size / 2) {
            return tail == null ? null : findNodeFromEnd(idx);
        } else {
            return head == null ? null : findNodeFromBegin(idx);
        }
    }

    private boolean remove(Node<T> node) {
        if (node == null) {
            return false;
        }
        Node<T> next = node.next == null ? tail : node.next;
        Node<T> prev = node.prev == null ? head : node.prev;
        prev.next = next;
        next.prev = prev;
        if (head == node) {
            head = next;
        }
        if (tail == node) {
            tail = prev;
        }
        size--;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (size == 0)
            return false;
        Node<T> node = findNode((T) o);
        return remove(node);
    }

    @Override
    public T remove(int index) {
        validateIndex(index);
        Node<T> node = findNode(index);
        remove(node);
        return node != null ? node.value : null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean flag = true;
        for (Object o : c) {
            flag = contains((T) o) && flag;
        }
        return flag;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return addAll(size, c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        validateIndexEnclosingEnd(index);
        Node<T> node = findNode(index);
        for (T element : c) {
            addInternal(node, element);
        }
        return true;
    }

    private String getIndexOutOfBoundMessage(int index) {
        return new StringBuilder("Index ")
                .append(index)
                .append("is out of bounds ")
                .append(0)
                .append(size)
                .toString();
    }

    private void validateIndexEnclosingEnd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(getIndexOutOfBoundMessage(index));
    }

    private void validateIndex(int index) {
        if (index >= size || index < 0)
            throw new IndexOutOfBoundsException(getIndexOutOfBoundMessage(index));
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean flag = true;
        for (Object element : c) {
            flag = remove((T) element) && flag;
        }
        return flag;
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


    private int findNodeIdx(T val) {
        if (head == null) {
            return -1;
        }
        Node<T> node = head;
        int counter = 0;
        for (T t : this) {
            if (Objects.equals(node.value, val)) {
                return counter;
            }
            counter++;
            node = node.next;
        }
        return -1;
    }

    private int findNodeLastIdx(T val) {
        if (tail == null) {
            return -1;
        }
        Node<T> node = tail;
        int counter = size - 1;
        while (!Objects.equals(node.value, val) && node.prev != null) {
            counter--;
            node = node.prev;
        }
        return Objects.equals(node.value, val) ? counter : -1;
    }

    @Override
    public T set(T element, int index) {
        validateIndex(index);
        Node<T> node = findNode(index);
        T value = node.value;
        node.value = element;
        return value;
    }

    @Override
    public void add(T element, int index) {
        validateIndexEnclosingEnd(index);
        addInternal(findNode(index), element);
    }

    @Override
    public int indexOf(Object o) {
        return findNodeIdx((T) o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return findNodeLastIdx((T) o);
    }

    public class LinkedListIterator implements Iterator<T> {
        private Node<T> current;
        private Node<T> lastReturned;

        public LinkedListIterator() {
            current = head;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
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
}
