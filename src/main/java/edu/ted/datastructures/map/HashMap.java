package edu.ted.datastructures.map;

import edu.ted.datastructures.list.ArrayList;
import edu.ted.datastructures.list.ExtendedList;
import edu.ted.datastructures.list.List;

import java.util.*;

public class HashMap<K, V> implements Map<K, V> {

    private static final double LOAD_RATIO = 0.75d;
    private static final int INITIAL_CAPACITY = 16;

    private ArrayList<Entry<K, V>>[] chunkList;

    private int currentSize;
    private int currentChunkSize;
    private double loadRatio;

    public HashMap() {
        currentSize = 0;
        chunkList = new ArrayList[INITIAL_CAPACITY];
        this.loadRatio = LOAD_RATIO;
    }

    public HashMap(int capacity, double loadRatio) {
        chunkList = new ArrayList[capacity];
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
        for (ArrayList<Entry<K, V>> chunk : chunkList) {
            if (chunk != null) {
                for (Entry<K, V> entry : chunk) {
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
        Entry<K, V> entry = getEntry(key);
        if (entry != null) {
            return entry.getValue();
        }
        return null;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        Entry<K, V> oldEntry = putIfAbsentOrReturnEntry(key, value);
        if (oldEntry != null) {
            return oldEntry.getValue();
        }
        return null;
    }

    private Entry<K, V> putIfAbsentOrReturnEntry(K key, V value) {
        Entry<K, V> oldEntry = getEntry(key);
        if (oldEntry != null) {
            return oldEntry;
        }
        ensureCapacity();
        putIntoChunk(new Entry<>(key, value));
        currentSize++;
        return null;
    }

    @Override
    public V put(K key, V value) {
        Entry<K, V> oldEntry = putIfAbsentOrReturnEntry(key, value);
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
        Entry<K, V> entry = getEntry(key);
        if (entry == null) {
            return null;
        }
        ExtendedList<Entry<K, V>> chunk = getChunk(key);
        chunk.remove(entry);
        if (chunk.size() == 0) {
            currentChunkSize--;
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
        for (ArrayList<Entry<K, V>> chunk : chunkList) {
            if (chunk != null) {
                chunk.clear();
            }
        }
        currentSize = 0;
        currentChunkSize = 0;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        Set<Entry<K, V>> entrySet = entrySet();
        for (Entry<K, V> entry : entrySet) {
            keySet.add(entry.getKey());
        }
        return keySet;
    }

    @Override
    public Collection<V> values() {
        Collection<V> valuesList = new java.util.ArrayList<>();
        Set<Entry<K, V>> entrySet = entrySet();
        for (Entry<K, V> entry : entrySet) {
            valuesList.add(entry.getValue());
        }
        return valuesList;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return this.new MapIterator();
    }

    private ExtendedList<Entry<K, V>> getChunkByKey(int chunkNum) {
        ExtendedList<Entry<K, V>> list;
        if (chunkList[chunkNum] == null) {
            chunkList[chunkNum] = new ArrayList<>(2);
        }
        list = chunkList[chunkNum];
        return list;
    }

    private ExtendedList<Entry<K, V>> getChunk(Object key) {
        int chunkNum = Math.abs(Objects.hashCode(key) % chunkList.length);
        return getChunkByKey(chunkNum);
    }

    private void putIntoChunk(Entry<K, V> entry) {
        List<Entry<K, V>> chunk = getChunk(entry.getKey());
        if (chunk.size() == 0) {
            currentChunkSize++;
        }
        chunk.add(entry);
    }

    private class MapIterator implements Iterator<Entry<K, V>> {
        private Set<K> keySet;
        private Iterator<K> internalIterator;
        private Entry<K, V> lastReturned;

        private MapIterator() {
            keySet = HashMap.this.keySet();
            internalIterator = keySet.iterator();
        }

        @Override
        public boolean hasNext() {
            return internalIterator.hasNext();
        }

        @Override
        public Entry<K, V> next() {
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
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entrySet = new HashSet<>();
        for (ArrayList<Entry<K, V>> chunk : chunkList) {
            if (chunk != null) {
                for (int j = 0; j < chunk.size(); j++) {
                    entrySet.add(chunk.get(j));
                }
            }
        }
        return entrySet;
    }

    private Entry<K, V> getEntry(Object key) {
        List<Entry<K, V>> chunk = getChunk(key);
        for (Entry<K, V> entry : chunk) {
            if (Objects.equals(entry.getKey(), key)) {
                return entry;
            }
        }
        return null;
    }

    private void ensureCapacity() {
        if (loadRatio * chunkList.length < (currentChunkSize + 1)) {
            Set<Entry<K, V>> set = entrySet();
            int newCapacity = chunkList.length * 2;

            currentChunkSize = 0;
            chunkList = new ArrayList[newCapacity];
            for (Entry<K, V> entry : set) {
                putIntoChunk(entry);
            }
            currentSize = set.size();
        }
    }
}
