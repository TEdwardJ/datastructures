package edu.ted.datastructures.list.interfaces;

import java.util.Collection;

public interface ExtendedList<T> {

    boolean containsAll(Collection<?> c);

    boolean addAll(Collection<? extends T> c);

    boolean removeAll(Collection<?> c);

    boolean addAll(int index, Collection<? extends T> c);

    Object[] toArray();

    boolean remove(T element);
}
