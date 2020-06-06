package edu.ted.datastructures.map;

import java.util.Collection;
import java.util.Set;

public interface Map<K, V> extends Iterable<Map.Entry<K, V>> {
    int size();

    boolean isEmpty();

    boolean containsKey(Object key);

    boolean containsValue(Object value);

    V putIfAbsent(K key, V value);

    V get(K key);

    V put(K key, V value);

    V remove(Object key);

    void putAll(Map<? extends K, ? extends V> map);

    void clear();

    Set<K> keySet();

    Collection<V> values();

    Set<Entry<K, V>> entrySet();

     interface Entry<K, V> {
        K getKey();

        V getValue();
    }
}
