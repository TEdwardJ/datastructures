import java.util.*;

public class MyArrayList<T> implements List<T>, Iterable<T> {
    private int capacity = 16;
    private Object[] objArray = new Object[capacity];
    private int realSize = 0;

    @Override
    public int size() {
        return realSize;
    }

    @Override
    public boolean isEmpty() {
        return realSize == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) > -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayListIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] arrayToReturn = new Object[realSize];
        System.arraycopy(objArray, 0, arrayToReturn, 0, realSize);
        return arrayToReturn;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(T t) {
        checkCapacity(1);
        objArray[realSize] = t;
        realSize++;
        return true;
    }

    private void checkCapacity(int toBeAdded) {
        int newCapacity = capacity;
        if (realSize + toBeAdded > capacity) {
            do {
                newCapacity = newCapacity << 1 <= Integer.MAX_VALUE ? newCapacity << 1 : Integer.MAX_VALUE;
            } while (realSize + toBeAdded > newCapacity);
            Object[] newObjArray = new Object[newCapacity];

            System.arraycopy(objArray, 0, newObjArray, 0, realSize);
            objArray = newObjArray;
            capacity = newCapacity;
        }
    }

    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx != -1) {
            remove(idx);
            return true;
        }
        return false;
    }

    private void arrayShift(int pos, int shift) {
        if (realSize - pos == 0) {
            realSize += shift;
            return;
        }
        System.arraycopy(objArray, pos, objArray, pos + shift, realSize - pos);
        if (shift < 0) {
            objArray[realSize - 1] = null;
        }
        realSize += shift;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (indexOf(o) == -1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return addAll(realSize, c);
/*        boolean flag = false;
        for (T o : c) {
            flag = add(o);
        }
        return flag;*/
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        if (index != realSize) {
            checkIndex(index);
        }
        int colSize = c.size();
        Object[] newObjArray = new Object[colSize + capacity];

        checkCapacity(colSize);
        if (index < realSize) {
            System.arraycopy(objArray, 0, newObjArray, 0, index);
            System.arraycopy(c.toArray(), 0, newObjArray, index, colSize);
            System.arraycopy(objArray, index, newObjArray, index + colSize, realSize - index);
            objArray = newObjArray;
        } else {
            System.arraycopy(c.toArray(), 0, objArray, index, colSize);
        }
        realSize = realSize + colSize;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean flag = false;
        for (Object o : c) {
            int idx = indexOf(o);
            if (idx != -1) {
                remove(idx);
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        realSize = 0;
    }

    @Override
    public T get(int index) {
        checkIndex(index);
        return (T) objArray[index];
    }

    private void checkIndex(int index) {
        if (index >= realSize)
            throw new IndexOutOfBoundsException();
    }

    @Override
    public T set(int index, T element) {
        checkIndex(index);
        T oldElement = (T) objArray[index];
        objArray[index] = element;
        return oldElement;
    }

    @Override
    public void add(int index, T element) {
        checkIndex(index);
        arrayShift(index, 1);
        objArray[index] = element;
    }

    @Override
    public T remove(int index) {
        checkIndex(index);
        T element = (T) objArray[index];
        arrayShift(index + 1, -1);
        return element;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < realSize; i++) {
            if (Objects.equals(o, objArray[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = realSize - 1; i >= 0; i--) {
            if (Objects.equals(o, objArray[i])) {
                return i;
            }
        }
        return -1;
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

    public class ArrayListIterator implements Iterator<T> {

        private int pointer;

        public ArrayListIterator() {
            pointer = 0;
        }

        @Override
        public boolean hasNext() {
            return pointer < realSize;
        }

        @Override
        public T next() {
            if (hasNext()) {
                return (T) objArray[pointer++];
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
