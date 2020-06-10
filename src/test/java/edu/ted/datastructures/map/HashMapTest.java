package edu.ted.datastructures.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Map Implementation Testing")
public class HashMapTest {

    private Map<String, String> testMap = new HashMap<>();

    @Test
    public void givenEmtpyGetSizeThenPutAndGetSizeAgain() {
        assertEquals(0, testMap.size());
        testMap.put("key0", "value0");
        assertEquals(1, testMap.size());
    }

    @Test
    public void givenEmptyPutThenReplaceValueOfKeyAndGetOldValue() {
        assertNull(testMap.put("key1", "value1"));
        assertEquals("value1", testMap.put("key1", "value2"));
    }

    @Test
    public void givenEmptyPutIfAbsentThenPutIfAbsentWithExistingKey() {
        assertNull(testMap.putIfAbsent("key1", "value1"));
        assertEquals("value1", testMap.put("key1", "value2"));
        testMap.put("key2", "value2");
        assertEquals("value2", testMap.putIfAbsent("key2", "value1"));
    }

    @Test
    public void givenEmptyPut25ElementsTToForceResizeThenCheckEachKeyIfExistsThenCheckSize() {
        assertNull(testMap.put("key1", "value1"));
        for (int i = 0; i < 25; i++) {
            if (i == 1) {
                assertEquals("value1", testMap.put("key" + i, "value" + i));
            } else {
                assertNull(testMap.put("key" + i, "value" + i));
            }
        }
        for (int i = 0; i < testMap.size(); i++) {
            assertTrue(testMap.containsKey("key" + i));
        }
        assertEquals(25, testMap.size());
    }

    @Test
    public void givenEmptyGetNonExistingThenPutCheckSizeThenGetExisting() {
        assertNull(testMap.get("NonExisting"));
        assertEquals(0, testMap.size());
        testMap.put("key0", "value0");
        assertEquals(1, testMap.size());
        assertEquals("value0", testMap.get("key0"));
    }

    @Test
    public void giivenEmptyRemoveNonExistingThenPutThenRemoveBothNonAndExisting() {
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
    public void addCheckSizeThenClearThenCheckSizeAgain() {
        testMap.put("key0", "value0");
        assertEquals(1, testMap.size());
        testMap.put("key1", "value0");
        assertEquals(2, testMap.size());
        testMap.clear();
        assertEquals(0, testMap.size());
    }

    @Test
    public void addEntriesThenGetKeySetAndCheckExistanceOfDifferentKeys() {
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
    public void addThenGetValuesListAndCheckExistanceAndSizeAndIfContains() {
        testMap.put("key0", "value0");
        testMap.put("key1", "value1");
        testMap.put("key2", "value1");
        testMap.put("key3", "value3");
        List<String> valuesList = (List<String>) testMap.values();
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
    public void checkIfEmptyThenAddAndCheckAgain() {
        assertTrue(testMap.isEmpty());
        testMap.put("123", "321");
        assertFalse(testMap.isEmpty());
        testMap.remove("123");
    }

    @Test
    public void checkIffEmptyMapContainsKeyThenAddAndCheckAgain() {
        assertFalse(testMap.containsKey("Key0"));
        testMap.put("key0", "321");
        assertTrue(testMap.containsKey("key0"));
        testMap.remove("123");
        assertFalse(testMap.containsKey("Key0"));
    }

    @Test
    public void checkIfContainsAddedValueThenRemoveAndCheckAgain() {
        assertFalse(testMap.containsValue("Value0"));
        testMap.put("key0", "Value0");
        assertTrue(testMap.containsValue("Value0"));
        assertFalse(testMap.containsValue("123"));
        testMap.remove("key0");
        assertFalse(testMap.containsKey("Value0"));
    }

    @Test
    public void put3ByOneThenPutCollectionThenCheckKeysAndValuesIfExist() {
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

    @Test
    public void iterator_getAllElementsAndCatchExceptionAtLastRepeatedNext() {
        testMap.put("key0", "value0");
        testMap.put("key1", "value1");
        testMap.put("key2", "value2");
        final Iterator<HashMap.Entry<String, String>> iter = testMap.iterator();
        assertTrue(iter.hasNext());
        int counter = 0;
        while (iter.hasNext()) {
            HashMap.Entry<String, String> entry = iter.next();
            counter++;
            assertTrue(entry.getKey().contains("key"));
            assertTrue(entry.getValue().contains("value"));
        }
        assertEquals(3, counter);
        Throwable thrown = assertThrows(NoSuchElementException.class, iter::next);
        assertEquals("All elements were fetched in this iterator", thrown.getMessage());
    }

    @Test
    public void iterator_whenRemoveElementTwice() {
        testMap.put("key0", "value0");
        testMap.put("key1", "value1");
        testMap.put("key2", "value2");
        final Iterator<HashMap.Node<String, String>> iter = ((Iterable) testMap).iterator();
        assertTrue(iter.hasNext());
        assertEquals("key0", iter.next().getKey());
        iter.remove();
        assertEquals(2, testMap.size());
        assertFalse(testMap.containsKey("key0"));
        assertTrue(testMap.containsKey("key2"));
        Throwable thrown = assertThrows(IllegalStateException.class, iter::remove);
        assertEquals("The element was already removed", thrown.getMessage());
    }

/*    @Test
    public void givenIteratorThenRemoveFromMapDirectlyAndGetConcurrentModificationException() {
        testMap.put("key0", "value0");
        testMap.put("key1", "value1");
        testMap.put("key2", "value2");
        Set<String> keySet = testMap.keySet();
        Iterator<Map.Entry<String, String>> iterator = testMap.iterator();
        Map.Entry<String, String> entry;
        entry = iterator.next();
        keySet.remove(entry.getKey());
        entry = iterator.next();
        keySet.remove(entry.getKey());
        testMap.remove(keySet.iterator().next());
        Throwable thrown = assertThrows(ConcurrentModificationException.class, iterator::next);
        assertEquals("The element does not exist now", thrown.getMessage());
    }*/

    @Test
    public void givenEmptyPutNullValueThenPutNonNullValueCheckIfSuccess() {
        testMap.put("key0", null);
        testMap.put("key0", "value0");
        assertEquals("value0", testMap.get("key0"));
    }

    @Test
    public void givenNonExtendableMapPut2ElementsThenGetIteratorThenRemoveFirstThenTryToGetNext(){
        HashMap<String, String> map = new HashMap<>(1,2);
        map.put("key0","value0");
        map.put("key1","value1");
        Iterator<Map.Entry<String, String>> iterator = map.iterator();
        assertTrue(iterator.hasNext());
        iterator.next();
        iterator.remove();
        assertTrue(iterator.hasNext());
        assertEquals("key0",iterator.next().getKey());
    }
    @Test
    public void givenNonExtendableMapPut2ElementsThenGetIteratorThenRemoveLastThenTryToGetNext(){
        HashMap<String, String> map = new HashMap<>(1,2);
        map.put("key0","value0");
        map.put("key1","value1");
        Iterator<Map.Entry<String, String>> iterator = map.iterator();
        assertTrue(iterator.hasNext());
        iterator.next();
        iterator.next();
        iterator.remove();
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class,()->iterator.next().getKey());
    }

}