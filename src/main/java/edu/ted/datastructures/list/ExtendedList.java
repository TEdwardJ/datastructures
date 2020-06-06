package edu.ted.datastructures.list;

import java.util.Collection;

public interface ExtendedList<T> extends List<T> {

    boolean containsAll(Collection<?> collection);

    boolean addAll(Collection<? extends T> collection);

    boolean removeAll(Collection<?> collection);

    boolean addAll(int index, Collection<? extends T> collection);

    Object[] toArray();

    boolean remove(T element);
}
