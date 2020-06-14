package edu.ted.datastructures.list;

import java.util.Collection;
import java.util.Objects;

public abstract class AbstractList<T> implements ExtendedList<T> {

    int size;

    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(T value) {
        return indexOf(value) != -1;
    }

    void validateIndexForAdd(int index) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException(getIndexOutOfBoundMessage(index, size));
        }
    }

    void validateIndex(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException(getIndexOutOfBoundMessage(index, size - 1));
        }
    }

    void validateIfEmpty(String errorMessage) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException(errorMessage);
        }
    }

    String getIndexOutOfBoundMessage(int index, int highBoundary) {
        return "Index " +
                index +
                " is out of bounds " +
                0 +
                ".." +
                highBoundary;
    }

    int findFirstEqualElementIndex(Object template) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(template, get(i))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        for (Object element : collection) {
            if (findFirstEqualElementIndex(element) == -1) {
                return false;
            }
        }
        return true;
    }

    public boolean removeAll(Collection<?> collection) {
        boolean flag = true;
        for (Object element : collection) {
            int index = findFirstEqualElementIndex(element);
            if (index != -1) {
                remove(index);
            }
            flag = index != -1 && flag;
        }
        return flag;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        return addAll(size, collection);
    }

    @Override
    public void add(T value) {
        add(value, size);
    }
}
