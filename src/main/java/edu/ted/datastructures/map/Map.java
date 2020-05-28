package edu.ted.datastructures.map;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
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

    void putAll(java.util.Map<? extends K, ? extends V> map);

    void clear();

    Set<K> keySet();

    Collection<V> values();

    Iterator<Entry<K, V>> iterator();

    Set<Entry<K, V>> entrySet();

    static class Entry<K, V> {
        private K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry<?, ?> entry = (Entry<?, ?>) o;
            return getKey().equals(entry.getKey()) &&
                    Objects.equals(getValue(), entry.getValue());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getKey(), getValue());
        }
    }
}
