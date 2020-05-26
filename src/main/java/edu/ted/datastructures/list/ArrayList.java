package edu.ted.datastructures.list;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ArrayList<T> extends AbstractList<T> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private T[] array;

    public ArrayList() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public ArrayList(int initialCapacity) {
        array = (T[]) new Object[initialCapacity];
    }

    @Override
    public Iterator<T> iterator() {
        return this.new ArrayListIterator();
    }

    public Object[] toArray() {
        Object[] arrayToReturn = new Object[size];
        System.arraycopy(array, 0, arrayToReturn, 0, size);
        return arrayToReturn;
    }

    private void ensureCapacity(){
        ensureCapacity(1);
    }

    private void ensureCapacity(int countToBeAdded) {
        if (size + countToBeAdded > array.length) {
            int newCapacity = (int) (array.length + countToBeAdded * 1.5) + 1;
            moveArray(newCapacity, size);
        }
    }

    private void moveArray(int newCapacity, int size) {
        @SuppressWarnings("unchecked")
        T[] newArray = (T[]) new Object[newCapacity];
        System.arraycopy(array, 0, newArray, 0, size);
        array = newArray;
    }

    public boolean remove(T value) {
        int indexToRemove = indexOf(value);
        if (indexToRemove != -1) {
            remove(indexToRemove);
            return true;
        }
        return false;
    }

    private void arrayShift(int position) {
        arrayShift(position, -1);
    }

    private void arrayShift(int position, int shift) {
        System.arraycopy(array, position, array, position + shift, size - position);
        if (shift < 0) {
            array[size - 1] = null;
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> collection) {
        validateIndexEnclosingEnd(index);
        int collectionSize = collection.size();
        if (array.length < collectionSize + size) {
            ensureCapacity(collectionSize);
        }
        if (index < size) {
            arrayShift(index, collectionSize);
        }
        System.arraycopy(collection.toArray(), 0, array, index, collectionSize);
        size += collectionSize;
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = 0;
    }

    public void trimToSize() {
        if (array.length > size) {
            moveArray(size, array.length);
        }
    }

    @Override
    public T get(int index) {
        validateIndex(index);
        return array[index];
    }

    @Override
    public T set(T value, int index) {
        validateIfEmpty();
        validateIndex(index);
        T oldElement = array[index];
        array[index] = value;
        return oldElement;
    }

    @Override
    public void add(T value, int index) {
        validateIndexEnclosingEnd(index);
        ensureCapacity();
        array[index] = value;
        size++;
    }

    @Override
    public T remove(int index) {
        validateIndex(index);
        T element = array[index];
        arrayShift(index + 1);
        size--;
        return element;
    }

    @Override
    public int indexOf(T value) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(value, array[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(T value) {
        for (int i = size - 1; i >= 0; i--) {
            if (Objects.equals(value, array[i])) {
                return i;
            }
        }
        return -1;
    }

    public class ArrayListIterator implements Iterator<T> {
        private int pointer;
        private int indexOfRemovedElement = -1;
        private int lastIndex;

        @Override
        public void remove() {
            if (indexOfRemovedElement > -1 || pointer < 0) {
                throw new IllegalStateException("The element was already removed");
            }
            ArrayList.this.remove(lastIndex);
            indexOfRemovedElement = lastIndex;
        }

        @Override
        public boolean hasNext() {
            return pointer < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("All elements were fetched in this iterator");
            }
            lastIndex = pointer;
            pointer++;
            indexOfRemovedElement = -1;
            return array[lastIndex];
        }
    }
}
