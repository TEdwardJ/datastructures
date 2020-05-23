package edu.ted.datastructures.list;


import edu.ted.datastructures.list.interfaces.ExtendedList;
import edu.ted.datastructures.list.interfaces.List;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ArrayList<T> implements List<T>, Iterable<T>, ExtendedList<T> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private T[] array;
    private int size = 0;

    public ArrayList() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public ArrayList(int initialCapacity) {
        array = (T[]) new Object[initialCapacity];
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
        return indexOf(o) > -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayListIterator();
    }

    public Object[] toArray() {
        Object[] arrayToReturn = new Object[size];
        System.arraycopy(array, 0, arrayToReturn, 0, size);
        return arrayToReturn;
    }

    @Override
    public void add(T t) {
        add(t, size);
    }

    private void ensureCapacity(int countToBeAdded) {
        int newCapacity = array.length;
        if (size + countToBeAdded > array.length) {
            do {
                newCapacity = newCapacity * 2 <= Integer.MAX_VALUE ? newCapacity * 2 : Integer.MAX_VALUE;
            } while (size + countToBeAdded > newCapacity && newCapacity < Integer.MAX_VALUE);
            T[] newArray = (T[]) new Object[newCapacity];
            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
        }
    }

    public boolean remove(T element) {
        int idx = indexOf(element);
        if (idx != -1) {
            remove(idx);
            return true;
        }
        return false;
    }

    private void arrayShift(int position, int shift) {
        if (size == position) {
            size += shift;
            return;
        }
        System.arraycopy(array, position, array, position + shift, size - position);
        if (shift < 0) {
            array[size - 1] = null;
        }
        size += shift;
    }

    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (indexOf(element) == -1) {
                return false;
            }
        }
        return true;
    }

    public boolean addAll(Collection<? extends T> c) {
        return addAll(size, c);
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        validateIndexEnclosingEnd(index);
        int colSize = c.size();
        if (array.length < colSize + size) {
            ensureCapacity(colSize);
        }
        if (index < size) {
            System.arraycopy(array, index, array, index + colSize, size - index);
        }
        System.arraycopy(c.toArray(), 0, array, index, colSize);
        size = size + colSize;
        return true;
    }

    public boolean removeAll(Collection<?> c) {
        boolean flag = true;
        for (Object element : c) {
            flag = remove((T) element) && flag;
        }
        return flag;
    }

    @Override
    public void clear() {
        size = 0;
        array = (T[]) new Object[array.length];
    }

    @Override
    public T get(int index) {
        validateIndex(index);
        return (T) array[index];
    }

    private void validateIndexEnclosingEnd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(getIndexOutOfBoundMessage(index));
    }

    private void validateIndex(int index) {
        if (index >= size || index < 0)
            throw new IndexOutOfBoundsException(getIndexOutOfBoundMessage(index));
    }

    private String getIndexOutOfBoundMessage(int index) {
        return new StringBuilder("Index ")
                .append(index)
                .append("is out of bounds ")
                .append(0)
                .append(size)
                .toString();
    }

    @Override
    public T set(T element, int index) {
        validateIndexEnclosingEnd(index);
        T oldElement = (T) array[index];
        array[index] = element;
        return oldElement;
    }

    @Override
    public void add(T element, int index) {
        validateIndexEnclosingEnd(index);
        ensureCapacity(1);
        array[index] = element;
        size++;
    }

    @Override
    public T remove(int index) {
        validateIndex(index);
        T element = (T) array[index];
        arrayShift(index + 1, -1);
        return element;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, array[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (Objects.equals(o, array[i])) {
                return i;
            }
        }
        return -1;
    }

    public class ArrayListIterator implements Iterator<T> {
        private int pointer;
        private int indexToBeRemoved = -1;
        private int lastIndex;

        @Override
        public void remove() {
            if (indexToBeRemoved > -1 || pointer < 0) {
                throw new IllegalStateException("The element already was removed");
            }
            indexToBeRemoved = lastIndex;
            ArrayList.this.remove(indexToBeRemoved);
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
            indexToBeRemoved = -1;
            return array[lastIndex];
        }
    }
}
