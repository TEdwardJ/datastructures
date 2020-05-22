package edu.ted.datastructures;

import edu.ted.datastructures.MyHashMap;
import edu.ted.datastructures.MyMap;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class MyHashMapTest {

    private MyMap<String, String> testMap = new MyHashMap<>();

    @Test
    public void size() {
        assertEquals(0, testMap.size());
        testMap.put("key0", "value0");
        assertEquals(1, testMap.size());
    }

    @Test
    public void put() {
        assertNull(testMap.put("key1", "value1"));
        assertEquals("value1", testMap.put("key1", "value2"));
    }

    @Test
    public void putIfAbsent() {
        assertEquals("value1",testMap.putIfAbsent("key1", "value1"));
        assertEquals("value1", testMap.put("key1", "value2"));
        testMap.put("key2","value2");
        assertNull(testMap.putIfAbsent("key2", "value1"));
    }

    @Test
    public void putWithResize() {
        assertNull(testMap.put("key1", "value1"));
        for (int i = 0; i < 25; i++) {
            if (i == 1) {
                assertEquals("value1", testMap.put("key" + i, "value" + i));
            } else {
                assertNull(testMap.put("key" + i, "value" + i));
            }
        }
        for (int i = 0; i < 25; i++) {
            assertTrue(testMap.containsKey("key" + i));
        }
        assertEquals(25, testMap.size());
    }

    @Test
    public void get() {
        assertNull(testMap.get("NonExisting"));
        assertEquals(0, testMap.size());
        testMap.put("key0", "value0");
        assertEquals(1, testMap.size());
        assertEquals("value0", testMap.get("key0"));
    }

    @Test
    public void remove() {
        assertNull(testMap.remove("NonExisting"));
        assertEquals(0, testMap.size());
        testMap.put("key0", "value0");
        assertEquals(1, testMap.size());
        assertNull(testMap.remove("NonExisting"));
        assertEquals("value0", testMap.remove("key0"));
        assertNull(testMap.get("key0"));
        assertEquals(0, testMap.size());
    }

    @Test
    public void clear() {
        testMap.put("key0", "value0");
        assertEquals(1, testMap.size());
        testMap.put("key1", "value0");
        assertEquals(2, testMap.size());
        testMap.clear();
        assertEquals(0, testMap.size());
    }

    @Test
    public void keySet() {
        testMap.put("key0", "value0");
        testMap.put("key1", "value0");
        testMap.put("key0", "value1");
        testMap.put("key3", "value0");
        Set<String> keySet = testMap.keySet();
        assertEquals(3, keySet.size());
        assertTrue(keySet.contains("key0"));
        assertTrue(keySet.contains("key1"));
        assertTrue(keySet.contains("key3"));
        assertFalse(keySet.contains("key33"));
    }

    @Test
    public void values() {
        testMap.put("key0", "value0");
        testMap.put("key1", "value1");
        testMap.put("key2", "value1");
        testMap.put("key3", "value3");
        List<String> valuesList = (List) testMap.values();
        assertEquals(4, testMap.size());
        assertTrue(valuesList.contains("value0"));
        assertTrue(valuesList.contains("value1"));
        assertTrue(valuesList.contains("value3"));
        assertNotEquals(valuesList.indexOf("value1"), valuesList.lastIndexOf("value1"));
        assertEquals(valuesList.indexOf("value3"), valuesList.lastIndexOf("value3"));
        assertTrue(valuesList.contains("value3"));
        assertFalse(valuesList.contains("value2"));
    }

    @Test
    public void isEmpty() {
        assertTrue(testMap.isEmpty());
        testMap.put("123", "321");
        assertFalse(testMap.isEmpty());
        testMap.remove("123");
    }

    @Test
    public void containsKey() {
        assertFalse(testMap.containsKey("Key0"));
        testMap.put("key0", "321");
        assertTrue(testMap.containsKey("key0"));
        testMap.remove("123");
        assertFalse(testMap.containsKey("Key0"));
    }

    @Test
    public void containsValue() {
        assertFalse(testMap.containsValue("Value0"));
        testMap.put("key0", "Value0");
        assertTrue(testMap.containsValue("Value0"));
        assertFalse(testMap.containsValue("123"));
        testMap.remove("key0");
        assertFalse(testMap.containsKey("Value0"));
    }

    @Test
    public void putAll() {
        testMap.put("key0", "value0");
        testMap.put("key1", "value1");
        testMap.put("key2", "value2");
        Map<String, String> mapToAdd = new HashMap<>();
        mapToAdd.put("Key0", "Value0");
        mapToAdd.put("Key1", "Value1");
        mapToAdd.put("Key2", "Value2");
        testMap.putAll(mapToAdd);
        assertTrue(testMap.containsKey("Key0"));
        assertTrue(testMap.containsValue("Value0"));
        assertTrue(testMap.containsKey("Key1"));
        assertTrue(testMap.containsValue("Value1"));
        assertTrue(testMap.containsKey("Key2"));
        assertTrue(testMap.containsValue("Value2"));
    }

    @Test(expected = NoSuchElementException.class)
    public void iterator() {
        testMap.put("key0", "value0");
        testMap.put("key1", "value1");
        testMap.put("key2", "value2");
        MyHashMap.MapIterator iter = testMap.iterator();
        assertTrue(iter.hasNext());
        int counter = 0;
        while (iter.hasNext()) {
            MyHashMap.Entry entry = iter.next();
            counter++;
            assertTrue(entry.getKey().toString().contains("key"));
            assertTrue(entry.getValue().toString().contains("value"));
        }
        assertEquals(3, counter);
        assertFalse(iter.hasNext());
        iter.next();

    }
}