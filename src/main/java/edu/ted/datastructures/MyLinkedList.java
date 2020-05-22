package edu.ted.datastructures;

import java.util.*;

public class MyLinkedList<T> implements List<T>, Iterable<T> {
    private Node tail;
    private Node head;
    private int size = 0;

    private static class Node<T> {
        private Node next;
        private Node prev;
        private T value;

        public Node(Node next, Node prev, T value) {
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
        Iterator iterator = this.iterator();
        int counter = 0;
        while (iterator.hasNext()) {
            arrayToReturn[counter++] = iterator.next();
        }
        return arrayToReturn;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
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

    private boolean addInternal(Node node, T t) {
        Node<T> newNode = new Node<T>(node, node.prev, t);
        if (head == node) {
            head = newNode;
        } else {
            node.prev.next = newNode;
        }
        node.prev = newNode;
        size++;
        return true;
    }

    @Override
    public boolean add(T t) {
        return addInternal(t);
    }

    private Node findNode(Node node, T val) {
        if (node.value.equals(val)) {
            return node;
        } else if (node.next != null) {
            return findNode(node.next, val);
        } else {
            return null;
        }
    }

    private Node findNode(Node node, int counter, int idx) {
        if (idx == counter) {
            return node;
        }
        if (node.next != null) {
            return findNode(node.next, ++counter, idx);
        } else {
            return null;
        }
    }

    private Node findNodeFromEnd(Node node, int counter, int idx) {
        if (idx == counter) {
            return node;
        }
        if (node.prev != null) {
            return findNode(node.prev, --counter, idx);
        } else {
            return null;
        }
    }

    private Node findNode(int idx) {
        int counter;
        if (idx > size >> 1) {
            counter = size - 1;
            return head == null ? null : findNodeFromEnd(tail, counter, idx);
        } else {
            counter = 0;
            return head == null ? null : findNode(head, counter, idx);
        }
    }

    private boolean remove(Node node) {
        if (node == null)
            return false;
        if (node == head) {
            head = node.next;
            if (size > 1)
                node.next.prev = null;
        } else {
            node.prev.next = node.next;
        }
        if (node == tail) {
            if (size > 1)
                tail = node.prev;
            tail.next = null;
        } else {
            node.next.prev = node.prev;
        }
        size--;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (size == 0)
            return false;
        Node node = findNode(head, (T) o);
        return remove(node);
    }

    @Override
    public T remove(int index) {
        checkIndex(index);
        boolean flag = false;
        Node node = findNode(index);
        remove(node);
        return node != null ? (T) node.value : null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c.size() == 0)
            return true;
        if (head == null)
            return false;
        boolean flag = true;
        for (Object o : c) {
            flag = flag && findNode(head, (T) o) != null;
        }
        return flag;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean flag = false;
        for (T t : c) {
            flag = addInternal(t);
        }
        return flag;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        boolean flag = false;
        boolean addToTheEnd = false;
        if (index != size) {
            checkIndex(index);
        } else {
            addToTheEnd = true;
        }
        if (!addToTheEnd) {
            Node node = findNode(index);
            for (T t : c) {
                flag = addInternal(node, t);
            }
        } else {
            for (T t : c) {
                flag = addInternal(t);
            }
        }
        return flag;
    }

    private void checkIndex(int index) {
        if (index >= size || index < 0)
            throw new IndexOutOfBoundsException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean flag = false;
        for (Object t : c) {
            Node node = findNode(head, (T) t);
            flag = remove(node);
        }
        return flag;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        tail = null;
        head = null;
        size = 0;
    }

    @Override
    public T get(int index) {
        checkIndex(index);
        Node node = findNode(index);
        return (T) node.value;
    }


    private int findNodeIdx(int counter, Node node, T val) {
        if (node == null)
            return -1;
        if (Objects.equals(node.value, val)) {
            return counter;
        } else if (node.next != null) {
            return findNodeIdx(++counter, node.next, val);
        } else {
            return -1;
        }
    }

    private int findNodeLastIdx(int counter, Node node, T val) {
        if (node == null)
            return -1;
        if (Objects.equals(node.value, val)) {
            return counter;
        } else if (node.prev != null) {
            return findNodeLastIdx(--counter, node.prev, val);
        } else {
            return -1;
        }
    }

    @Override
    public T set(int index, T element) {
        checkIndex(index);
        Node node = findNode(index);
        T value = (T) node.value;
        node.value = element;
        return value;
    }

    @Override
    public void add(int index, T element) {
        checkIndex(index);
        addInternal(findNode(index), element);
    }

    @Override
    public int indexOf(Object o) {
        return findNodeIdx(0, head, (T) o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return findNodeLastIdx(size - 1, tail, (T) o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return null;
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return null;
    }

    public class LinkedListIterator implements Iterator<T> {

        private Node pointer;

        public LinkedListIterator() {
            pointer = new Node(head, null, null);
        }

        @Override
        public boolean hasNext() {
            return pointer.next != null;
        }

        @Override
        public T next() {
            if (hasNext()) {
                pointer = pointer.next;
                return (T) pointer.value;
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
