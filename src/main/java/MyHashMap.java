import java.util.*;

public class MyHashMap<K, V> implements MyMap<K, V>, Iterable<MyHashMap.Entry<K, V>> {

    private static final double LOAD_RATIO = 0.75d;
    private static final int INITIAL_CAPACITY = 16;

    private MyArrayList[] chunkList;

    private int currentCapacity;
    private int currentSize;

    public MyHashMap() {
        currentCapacity = INITIAL_CAPACITY;
        currentSize = 0;
        chunkList = new MyArrayList[currentCapacity];
    }

    public MyHashMap(int currentCapacity, int startSize) {
        this.currentCapacity = currentCapacity;
        this.currentSize = currentSize;
        chunkList = new MyArrayList[currentCapacity];
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
        MyArrayList[] newChunkList = new MyArrayList[newCapacity];

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

    private Entry<K, V> getEntry(MyArrayList[] chunkList, Object key) {
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

    private MyArrayList<Entry> createChunk(MyArrayList[] chunkList, int chunkNum) {
        chunkList[chunkNum] = new MyArrayList<Entry>();
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
        if (putIfAbsent(key, value) == null)  {
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

    private List<Entry> getChunk(MyArrayList[] chunkList, int chunkNum) {
        List<Entry> list;
        if (chunkList[chunkNum] == null) {
            list = createChunk(chunkList, chunkNum);
        } else {
            list = chunkList[chunkNum];
        }
        return list;
    }

    /*    private List<Entry> getChunk(int chunkNum) {
            return getChunk(this.chunkList, chunkNum);
        }*/
    private List<Entry> getChunk(K key) {
        int chunkNum = getChunkNum(key);
        return getChunk(this.chunkList, chunkNum);
    }

    private List<Entry> getChunk(MyArrayList[] chunkList, K key) {
        int chunkNum = getChunkNum(key);
        return getChunk(chunkList, chunkNum);
    }

    private V chunkPutNonExisting(MyArrayList[] chunkList, Entry entry) {
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
            list.remove(entry);
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
        for (MyArrayList chunk : chunkList) {
            if (chunk != null)
                chunk.clear();
        }
        currentSize = 0;

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
        Collection<V> valuesList = new MyArrayList<>();
        Set<Entry<K, V>> entrySet = entrySet();
        for (Entry<K, V> entry : entrySet) {
            valuesList.add(entry.getValue());
        }
        return valuesList;
    }

    @Override
    public MyHashMap.MapIterator iterator() {
        return this.new MapIterator();
    }

    public class MapIterator implements Iterator<Entry<K, V>> {
        private Set<K> keySet;
        private Iterator internalIterator;

        public MapIterator() {
            keySet = MyHashMap.this.keySet();
            internalIterator = keySet.iterator();
        }

        @Override
        public boolean hasNext() {
            return internalIterator.hasNext();
        }

        @Override
        public Entry<K, V> next() {
            if (hasNext()) {
                return getEntry(internalIterator.next());
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entrySet = new HashSet<>();
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
