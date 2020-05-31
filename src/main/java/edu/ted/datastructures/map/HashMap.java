package edu.ted.datastructures.map;

import edu.ted.datastructures.list.ArrayList;
import edu.ted.datastructures.list.ExtendedList;
import edu.ted.datastructures.list.List;

import java.util.*;

public class HashMap<K, V> implements Map<K, V> {

    private static final double DEFAULT_LOAD_RATIO = 0.75d;
    private static final int INITIAL_CAPACITY = 16;
    private static final double GROWTH_FACTOR = 2;

    private ArrayList<Map.Entry<K, V>>[] bucketList;

    private int currentSize;
    private int currentBucketSize;
    private double loadRatio;

    public HashMap() {
        this(INITIAL_CAPACITY, DEFAULT_LOAD_RATIO);
    }

    public HashMap(int capacity, double loadRatio) {
        bucketList = (ArrayList<Map.Entry<K, V>>[])new ArrayList[capacity];
        this.loadRatio = loadRatio;
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return getEntry(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (ArrayList<Map.Entry<K, V>> bucket : bucketList) {
            if (bucket != null) {
                for (Map.Entry<K, V> entry : bucket) {
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
        Map.Entry<K, V> entry = getEntry(key);
        if (entry != null) {
            return entry.getValue();
        }
        return null;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        Map.Entry<K, V> oldEntry = putIfAbsentOrReturnEntry(key, value);
        if (oldEntry != null) {
            return oldEntry.getValue();
        }
        return null;
    }

    private Map.Entry<K, V> putIfAbsentOrReturnEntry(K key, V value) {
        Map.Entry<K, V> oldEntry = getEntry(key);
        if (oldEntry != null) {
            return oldEntry;
        }
        ensureCapacity();
        putIntoBucket(new HashMapEntry<>(key, value));
        currentSize++;
        return null;
    }

    @Override
    public V put(K key, V value) {
        Map.Entry<K, V> oldEntry = putIfAbsentOrReturnEntry(key, value);
        if (oldEntry != null) {
            V oldValue = oldEntry.getValue();
            if (!Objects.equals(value, oldValue)) {
                oldEntry.setValue(value);
            }
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
        currentSize--;
        return entry.getValue();
    }

    @Override
    public void putAll(java.util.Map map) {
        Set<? extends K> keySet = map.keySet();
        for (K key : keySet) {
            this.put(key, (V) map.get(key));
        }
    }

    @Override
    public void clear() {
        for (ArrayList<Map.Entry<K, V>> bucket : bucketList) {
            if (bucket != null) {
                bucket.clear();
            }
        }
        currentSize = currentBucketSize = 0;
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
    public Iterator<Map.Entry<K, V>> iterator() {
        return new MapIterator();
    }

    private ExtendedList<Map.Entry<K, V>> getBucketByNumber(int bucketNumber) {
        ExtendedList<Map.Entry<K, V>> list;
        if (bucketList[bucketNumber] == null) {
            bucketList[bucketNumber] = new ArrayList<>(1);
        }
        list = bucketList[bucketNumber];
        return list;
    }

    private ExtendedList<Map.Entry<K, V>> getBucket(Object key) {
        int bucketNumber = Math.abs(Objects.hashCode(key) % bucketList.length);
        return getBucketByNumber(bucketNumber);
    }

    private void putIntoBucket(Map.Entry<K, V> entry) {
        List<Map.Entry<K, V>> bucket = getBucket(entry.getKey());
        if (bucket.size() == 0) {
            currentBucketSize++;
        }
        bucket.add(entry);
    }

    private class MapIterator implements Iterator<Map.Entry<K, V>> {
        private Set<K> keySet;
        private Iterator<K> internalIterator;
        private Map.Entry<K, V> lastReturned;

        private MapIterator() {
            keySet = HashMap.this.keySet();
            internalIterator = keySet.iterator();
        }

        @Override
        public boolean hasNext() {
            return internalIterator.hasNext();
        }

        @Override
        public Map.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException("All elements were fetched in this iterator");
            }
            lastReturned = getEntry(internalIterator.next());
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
        for (ArrayList<Map.Entry<K, V>> bucket : bucketList) {
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
        if (loadRatio * bucketList.length < (currentBucketSize + 1)) {
            Set<Map.Entry<K, V>> set = entrySet();

            currentBucketSize = 0;
            bucketList = (ArrayList<Map.Entry<K, V>>[])new ArrayList[(int)(bucketList.length * GROWTH_FACTOR)];
            for (Map.Entry<K, V> entry : set) {
                putIntoBucket(entry);
            }
        }
    }

    public static class HashMapEntry<K, V> implements Map.Entry<K, V> {
        private K key;
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
