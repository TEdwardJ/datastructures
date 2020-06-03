package edu.ted.datastructures.map;

import edu.ted.datastructures.list.ArrayList;
import edu.ted.datastructures.list.ExtendedList;
import edu.ted.datastructures.list.List;

import java.util.*;

public class HashMap<K, V> implements Map<K, V> {

    private static final double DEFAULT_LOAD_FACTOR = 0.75d;
    private static final int INITIAL_CAPACITY = 16;
    private static final double GROWTH_FACTOR = 2;

    private ArrayList<Map.Entry<K, V>>[] buckets;

    private int size;
    private int currentBucketSize;
    private double loadFactor;

    public HashMap() {
        this(INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashMap(int capacity, double loadRatio) {
        buckets = (ArrayList<Map.Entry<K, V>>[]) new ArrayList[capacity];
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
        return getEntry(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (ArrayList<Map.Entry<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Map.Entry<? extends K, ? extends V> entry : bucket) {
                    if (Objects.equals(entry.getValue(), value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        Map.Entry<? extends K, ? extends V> entry = getEntry(key);
        if (entry != null) {
            return entry.getValue();
        }
        return null;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        Map.Entry<? extends K, ? extends V> oldEntry = putIfAbsentOrReturnEntry(key, value);
        if (oldEntry != null) {
            return oldEntry.getValue();
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        Map.Entry<K, V> oldEntry = putIfAbsentOrReturnEntry(key, value);
        if (oldEntry != null) {
            V oldValue = oldEntry.getValue();
            oldEntry.setValue(value);
            return oldValue;
        }
        return null;
    }

    @Override
    public V remove(Object key) {
        Map.Entry<K, V> entry = getEntry(key);
        if (entry == null) {
            return null;
        }
        ExtendedList<Map.Entry<K, V>> bucket = getBucket(key);
        bucket.remove(entry);
        if (bucket.size() == 0) {
            currentBucketSize--;
        }
        size--;
        return entry.getValue();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        Set<? extends Entry<? extends K, ? extends V>> entrySet = map.entrySet();
        for (Entry<? extends K, ? extends V> entry : entrySet) {
            putIntoBucket((Entry<K, V>) entry);
        }
    }

    @Override
    public void clear() {
        for (ArrayList<Map.Entry<K, V>> bucket : buckets) {
            if (bucket != null) {
                bucket.clear();
            }
        }
        size = currentBucketSize = 0;
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
        for (Map.Entry<K,  V> entry : entrySet) {
            valuesList.add(entry.getValue());
        }
        return valuesList;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new MapIterator();
    }

    private Map.Entry<K, V> putIfAbsentOrReturnEntry(K key, V value) {
        Map.Entry<K, V> oldEntry = getEntry(key);
        if (oldEntry != null) {
            return oldEntry;
        }
        ensureCapacity();
        putIntoBucket(new HashMapEntry<>(key, value));
        size++;
        return null;
    }

    private ExtendedList<Map.Entry<K, V>> getBucketByIndex(int bucketNumber) {
        if (buckets[bucketNumber] == null) {
            buckets[bucketNumber] = new ArrayList<>(1);
        }
        return buckets[bucketNumber];
    }

    private ExtendedList<Map.Entry<K, V>> getBucket(Object key) {
        int bucketIndex = Math.abs(Objects.hashCode(key) % buckets.length);
        return getBucketByIndex(bucketIndex);
    }

    private void putIntoBucket(Map.Entry<K, V> entry) {
        List<Map.Entry<K, V>> bucket = getBucket(entry.getKey());
        if (bucket.size() == 0) {
            currentBucketSize++;
        }
        bucket.add(entry);
    }

    private class MapIterator implements Iterator<Map.Entry<K, V>> {
        private Set<? extends K> keySet;
        private Iterator<? extends K> bucketIterator;
        private Map.Entry<K, V> lastReturned;

        private MapIterator() {
            keySet = HashMap.this.keySet();
            bucketIterator = keySet.iterator();
        }

        @Override
        public boolean hasNext() {
            return bucketIterator.hasNext();
        }

        @Override
        public Map.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException("All elements were fetched in this iterator");
            }
            lastReturned = getEntry(bucketIterator.next());
            if (lastReturned == null) {
                throw new ConcurrentModificationException("The element does not exist now");
            }
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
        for (List<Map.Entry<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    entrySet.add(entry);
                }
            }
        }
        return entrySet;
    }

    private Map.Entry<K, V> getEntry(Object key) {
        List<Map.Entry<K, V>> bucket = getBucket(key);
        for (Map.Entry<K, V> entry : bucket) {
            if (Objects.equals(entry.getKey(), key)) {
                return entry;
            }
        }
        return null;
    }

    private void ensureCapacity() {
        if (loadFactor * buckets.length < (size + 1)) {
            Set<Map.Entry<K, V>> set = entrySet();

            currentBucketSize = 0;
            buckets = (ArrayList<Map.Entry<K, V>>[]) new ArrayList[(int) (buckets.length * GROWTH_FACTOR)];
            for (Map.Entry<K, V> entry : set) {
                putIntoBucket(entry);
            }
        }
    }

    public static class HashMapEntry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;

        HashMapEntry(K key, V value) {
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
    }
}
