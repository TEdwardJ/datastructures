package edu.ted.datastructures.map;

import java.util.*;

public class HashMap<K, V> implements Map<K, V> {

    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final double GROWTH_FACTOR = 2;
    private static final int INITIAL_CAPACITY = 16;

    private Node<K, V>[] buckets;

    private int size;
    private double loadFactor;

    public HashMap() {
        this(INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashMap(int capacity, double loadRatio) {
        buckets = (Node<K, V>[]) new Node[capacity];
        this.loadFactor = loadRatio;
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
    public boolean containsKey(Object key) {
        return findNode(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Entry<K, V> currentNode : this) {
            if (Objects.equals(currentNode.getValue(), value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        final Node<K, V> node = findNode(key);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        final Node<K, V> node = putIfAbsentOrReturnNode(key, value);
        if (node == null) {
            size++;
            return null;
        }
        return node.value;
    }

    @Override
    public V put(K key, V value) {
        Node<K, V> oldNode = removeInternal(key);
        if (oldNode == null) {
            ensureCapacity();
            size++;
        }
        putIfAbsentOrReturnNode(key, value);

        if (oldNode == null) {
            return null;
        }
        return oldNode.value;
    }


    @Override
    public V remove(Object key) {
        Node<K, V> oldNode = removeInternal(key);
        if (oldNode == null) {
            return null;
        }
        size--;
        return oldNode.value;
    }



    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        Set<? extends Entry<? extends K, ? extends V>> entrySet = map.entrySet();
        for (Entry<? extends K, ? extends V> entry : entrySet) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        for (Node<K, V> bucket : buckets) {
            if (bucket != null) {
                bucket = null;
            }
        }
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        Set<Map.Entry<K, V>> entrySet = entrySet();
        for (Map.Entry<K, V> entry : entrySet) {
            keySet.add(entry.getKey());
        }
        return keySet;
    }

    @Override
    public Collection<V> values() {
        Collection<V> valuesList = new java.util.ArrayList<>();
        for (Entry<K, V> entry : this) {
            valuesList.add(entry.getValue());
        }
        return valuesList;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entrySet = new HashSet<>();
        for (Entry<K, V> node : this) {
            entrySet.add(node);
        }
        return entrySet;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new MapIterator();
    }

    private void putNew(Node<K, V> newNode) {
        int bucketIndex = getBucketIndex(newNode.key);
        newNode.next = buckets[bucketIndex];
        buckets[bucketIndex] = newNode;
    }

    private Node<K, V> putIfAbsentOrReturnNode(K key, V value) {
        final Node<K, V> existingNode = findNode(key);
        if (existingNode != null) {
            return existingNode;
        }
        putNew(new Node<>(key, value));
        return null;
    }


    private int getBucketIndex(Object key) {
        return Math.abs(Objects.hashCode(key) % buckets.length);
    }

    private Node<K, V> findNode(Object key) {
        if (size == 0) {
            return null;
        }
        int hash = Objects.hashCode(key);
        for (Node<K, V> node = buckets[getBucketIndex(key)]; node != null; node = node.next) {
            if (hash == node.hash && Objects.equals(node.key, key)) {
                return node;
            }
        }
        return null;
    }


    private Node<K, V> removeInternal(Object key) {
        if (size == 0) {
            return null;
        }
        int hash = Objects.hashCode(key);
        int bucketIndex = getBucketIndex(key);

        for (Node<K, V> node = buckets[bucketIndex], previousNode = null; node != null; node = node.next) {
            if (node.hash == hash && Objects.equals(node.key, key)) {
                if (node == buckets[bucketIndex]) {
                    buckets[bucketIndex] = node.next;
                } else {
                    previousNode.next = node.next;
                }
                return node;
            }
            previousNode = node;
        }
        return null;
    }

    private class MapIterator implements Iterator<Map.Entry<K, V>> {
        private int bucketIndex;
        private int nextIndex;
        private Node<K, V> lastReturned;
        private Node<K, V> lastRemoved;

        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException("All elements were fetched in this iterator");
            }
            if (lastReturned != null && lastReturned.next != null) {
                lastReturned = lastReturned.next;
            } else {
                while ((lastReturned = buckets[bucketIndex++]) == null);
            }
            nextIndex++;
            return lastReturned;
        }

        @Override
        public void remove() {
            if (lastReturned == lastRemoved) {
                throw new IllegalStateException("The element was already removed");
            }
            HashMap.this.remove(lastReturned.getKey());
            lastRemoved = lastReturned;
            nextIndex--;
        }
    }

    private void ensureCapacity() {
        if (loadFactor * buckets.length < (size + 1)) {
            Set<Map.Entry<K, V>> set = entrySet();
            buckets = (Node<K, V>[]) new Node[(int) (buckets.length * GROWTH_FACTOR)];
            for (Map.Entry<K, V> entry : set) {
                putNew((Node<K, V>)entry);
            }
        }
    }

    public static class Node<K, V> implements Map.Entry<K, V> {
        private final K key;
        private final V value;
        private final int hash;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = Objects.hashCode(key);
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
