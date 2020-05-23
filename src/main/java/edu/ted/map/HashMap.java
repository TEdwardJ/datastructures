package edu.ted.map;


import edu.ted.datastructures.list.ArrayList;
import edu.ted.datastructures.list.interfaces.ExtendedList;
import edu.ted.datastructures.list.interfaces.List;
import edu.ted.map.interfaces.MyMap;

import java.util.*;

public class HashMap<K, V> implements MyMap<K, V>, Iterable<HashMap.Entry<K, V>> {

    private static final double LOAD_RATIO = 0.75d;
    private static final int INITIAL_CAPACITY = 16;

    private ArrayList[] chunkList;

    private int currentCapacity;
    private int currentSize;

    public HashMap() {
        currentCapacity = INITIAL_CAPACITY;
        currentSize = 0;
        chunkList = new ArrayList[currentCapacity];
    }

    public HashMap(int currentCapacity, int startSize) {
        this.currentCapacity = currentCapacity;
        this.currentSize = currentSize;
        chunkList = new ArrayList[currentCapacity];
    }

    public static class Entry<K, V> {
        private K key;
        private V value;

        public Entry(K key, V value) {
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

    private void checkCapacity() {
        if (LOAD_RATIO * currentCapacity < (currentSize + 1)) {
            resize();
        }
    }

    private void resize() {
        Set<Entry<K, V>> set = entrySet();
        int newCapacity = currentCapacity << 1 <= Integer.MAX_VALUE ? currentCapacity << 1 : Integer.MAX_VALUE;
        edu.ted.datastructures.list.ArrayList[] newChunkList = new ArrayList[newCapacity];

        currentCapacity = newCapacity;
        for (Entry<K, V> entry : set) {
            chunkPutNonExisting(newChunkList, entry);
        }
        chunkList = newChunkList;
        currentSize = set.size();
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
        Entry<K, V> entry = getEntry(key);
        return entry != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (int i = 0; i < chunkList.length; i++) {
            if (chunkList[i] != null) {
                for (int j = 0; j < chunkList[i].size(); j++) {
                    if (Objects.equals(((Entry) chunkList[i].get(j)).getValue(), value))
                        return true;
                }
            }
        }
        return false;
    }


    private Entry<K, V> getEntry(Object key) {
        return getEntry(this.chunkList, key);
    }

    private Entry<K, V> getEntry(ArrayList[] chunkList, Object key) {
        List<Entry> list = getChunk((K) key);
        for (int i = 0; i < list.size(); i++) {
            if (((Entry) list.get(i)).getKey().equals(key)) {
                Entry old = ((Entry) list.get(i));
                return old;
            }
        }
        return null;
    }

    @Override
    public V get(K key) {
        Entry<K, V> entry = getEntry(key);
        if (entry != null) {
            return (V) entry.getValue();
        }
        return null;
    }

    private ArrayList<Entry> createChunk(ArrayList[] chunkList, int chunkNum) {
        chunkList[chunkNum] = new ArrayList<Entry>();
        return chunkList[chunkNum];
    }

    @Override
    public V putIfAbsent(K key, V value) {
        Entry oldEntry = getEntry(key);
        if (oldEntry == null) {
            checkCapacity();
            Entry entry = new Entry(key, value);
            chunkPutNonExisting(this.chunkList, entry);
            currentSize++;
            return value;
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        Entry oldEntry = getEntry(key);
        if (putIfAbsent(key, value) == null) {
            V oldValue = (V) oldEntry.getValue();
            if (!Objects.equals(value, oldEntry.getValue())) {
                oldEntry.setValue(oldEntry.getValue());
            }
            return oldValue;
        } else {
            return null;
        }
    }

    private int getChunkNum(Object o) {
        int hashcode = o.hashCode();
        int chunkNum = Math.abs(hashcode % currentCapacity);
        return chunkNum;
    }

    private List<Entry> getChunk(ArrayList[] chunkList, int chunkNum) {
        List<Entry> list;
        if (chunkList[chunkNum] == null) {
            list = createChunk(chunkList, chunkNum);
        } else {
            list = chunkList[chunkNum];
        }
        return list;
    }

    /*    private list<Entry> getChunk(int chunkNum) {
            return getChunk(this.chunkList, chunkNum);
        }*/
    private List<Entry> getChunk(K key) {
        int chunkNum = getChunkNum(key);
        return getChunk(this.chunkList, chunkNum);
    }

    private List<Entry> getChunk(ArrayList[] chunkList, K key) {
        int chunkNum = getChunkNum(key);
        return getChunk(chunkList, chunkNum);
    }

    private V chunkPutNonExisting(ArrayList[] chunkList, Entry entry) {
        List<Entry> list = getChunk(chunkList, (K) entry.getKey());
        list.add(entry);
        return null;
    }

    @Override
    public V remove(Object key) {
        List<Entry> list = getChunk((K) key);
        Entry entry = getEntry(key);
        if (entry != null) {
            currentSize--;
            ((ExtendedList<Entry>) list).remove(entry);
            return (V) entry.getValue();
        } else {
            return null;
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        Set<K> keySet = (Set<K>) m.keySet();
        for (K k : keySet) {
            this.put(k, m.get(k));
        }
    }

    @Override
    public void clear() {
        for (ArrayList chunk : chunkList) {
            if (chunk != null)
                chunk.clear();
        }
        currentSize = 0;

    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<K>();
        Set<Entry<K, V>> entrySet = entrySet();
        for (Entry<K, V> entry : entrySet) {
            keySet.add(entry.getKey());
        }
        return keySet;
    }

    @Override
    public Collection<V> values() {
        Collection<V> valuesList = new java.util.ArrayList();
        Set<Entry<K, V>> entrySet = entrySet();
        for (Entry<K, V> entry : entrySet) {
            valuesList.add(entry.getValue());
        }
        return valuesList;
    }

    @Override
    public HashMap.MapIterator iterator() {
        return this.new MapIterator();
    }

    public class MapIterator implements Iterator<Entry<K, V>> {
        private Set<K> keySet;
        private Iterator internalIterator;
        private Entry<K, V> lastReturned;

        public MapIterator() {
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
                throw new NoSuchElementException();
            }
            lastReturned = getEntry(internalIterator.next());
            return lastReturned;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            HashMap.this.remove(lastReturned.key);
            lastReturned = null;
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entrySet = new HashSet<Entry<K, V>>();
        for (int i = 0; i < chunkList.length; i++) {
            if (chunkList[i] != null) {
                for (int j = 0; j < chunkList[i].size(); j++) {
                    entrySet.add((Entry) chunkList[i].get(j));
                }
            }
        }
        return entrySet;
    }
}
