package edu.ted.datastructures;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface MyMap<K,V> {
    public int size();

    boolean isEmpty();

    boolean containsKey(Object key);

    boolean containsValue(Object value);

    V putIfAbsent (K key, V value);

    V get(K key);

    V put(K key, V value);

    V remove(Object key);

    void putAll(Map<? extends K, ? extends V> m);

    void clear();

    Set<K> keySet();

    Collection<V> values();
    MyHashMap.MapIterator iterator();

    Set<MyHashMap.Entry<K, V>> entrySet();
}
