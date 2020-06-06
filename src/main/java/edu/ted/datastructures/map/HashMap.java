package edu.ted.datastructures.map;

import java.util.*;

public class HashMap<K, V> implements Map<K, V> {

    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int INITIAL_CAPACITY = 16;
    private static final double GROWTH_FACTOR = 2;

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
        Node<K, V> currentNode;
        for (Node<K, V> bucket : buckets) {
            for (currentNode = bucket; currentNode != null; currentNode = currentNode.next) {
                if (Objects.equals(currentNode.value, value)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        final Node<K, V> node = findNode(key);
        if (node != null) {
            return node.value;
        }
        return null;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        final Node<K, V> node = putIfAbsentOrReturnNode(key, value);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    public Node<K, V> putIfAbsentOrReturnNode(K key, V value) {
        final Node<K, V> node = findNode(key);
        if (node == null) {
            int hash = Objects.hashCode(key);
            int bucketIndex = Math.abs(hash % buckets.length);
            Node<K, V> newNode = new Node(key, value);
            newNode.next = buckets[bucketIndex];
            buckets[bucketIndex] = newNode;
            return null;
        }
        return node;
    }

    @Override
    public V put(K key, V value) {
        Node<K, V> oldNode = removeInternal(key);
        if (oldNode == null){
            ensureCapacity();
            size++;
        }
        putIfAbsentOrReturnNode(key, value);

        if (oldNode == null) {
            return null;
        }
        return oldNode.value;
    }


    private Node<K, V> findNode(Object key) {
        Node<K, V> startNode = getBucket(key);
        if (startNode == null) {
            return null;
        }
        for (Node<K, V> node = startNode; node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                return node;
            }
        }
        return null;
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

    private Node<K, V> removeInternal(Object key) {
        int hash = Objects.hashCode(key);
        int bucketIndex = Math.abs(hash % buckets.length);
        Node<K, V> oldNode = null;
        for (Node<K, V> node = buckets[bucketIndex]; node != null; node = node.next) {
            if (node.hash == hash && Objects.equals(node.key, key)) {
                if (oldNode == null) {
                    buckets[bucketIndex] = node.next;
                } else {
                    oldNode.next = node.next;
                }return node;
            }
            oldNode = node;
        }
        return null;
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
        Set<Map.Entry<K, V>> entrySet = entrySet();
        for (Map.Entry<K, V> entry : entrySet) {
            valuesList.add(entry.getValue());
        }
        return valuesList;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new MapIterator();
    }


    private Node<K, V> getBucketByIndex(int bucketNumber) {
        return buckets[bucketNumber];
    }

    private Node<K, V> getBucket(Object key) {
        int bucketIndex = Math.abs(Objects.hashCode(key) % buckets.length);
        return getBucketByIndex(bucketIndex);
    }

/*    private void putIntoBucket(Map.Entry<K, V> entry) {
        Node<K, V> bucket = getBucket(entry.getKey());
        if (bucket.size() == 0) {
            currentBucketSize++;
        }
        bucket.add(entry);
    }*/

    private class MapIterator implements Iterator<Map.Entry<K, V>> {
        private int bucketIndex;
        private int returnedNumber;
        private Node<K, V> lastReturned;

        @Override
        public boolean hasNext() {
            return returnedNumber < size;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException("All elements were fetched in this iterator");
            }
            Node<K, V> nextNode = null;
            while (nextNode == null && bucketIndex < buckets.length) {
                if (lastReturned == null) {
                    nextNode = buckets[bucketIndex];
                    bucketIndex++;
                } else if (lastReturned.next != null) {
                    nextNode = lastReturned.next;
                } else {
                    nextNode = buckets[bucketIndex];
                    bucketIndex++;
                }
            }
            lastReturned = nextNode;
            returnedNumber++;
            return lastReturned;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException("The element was already removed");
            }
            HashMap.this.remove(lastReturned.getKey());
            lastReturned = null;
        }
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entrySet = new HashSet<>();
        for (Node<K, V> bucket : buckets) {
            for (Node<K, V> entry = bucket; entry != null; entry = entry.next) {
                entrySet.add(entry);
            }
        }
        return entrySet;
    }

    private void ensureCapacity() {
        if (loadFactor * buckets.length < (size + 1)) {
            Set<Map.Entry<K, V>> set = entrySet();
            buckets = (Node<K, V>[]) new Node[(int) (buckets.length * GROWTH_FACTOR)];
            for (Map.Entry<K, V> entry : set) {
                putIfAbsent(entry.getKey(), entry.getValue());
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
