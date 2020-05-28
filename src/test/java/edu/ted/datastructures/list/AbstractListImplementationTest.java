package edu.ted.datastructures.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class AbstractListImplementationTest {

    private ExtendedList<String> testList;

    abstract ExtendedList<String> getList();

    @BeforeEach
    void initList() {
        testList = getList();
        testList = (ExtendedList<String>) testList;
    }

    @Test
    void size() {
        assertEquals(0, testList.size());
        testList.add("One");
        assertNotEquals(0, testList.size());
        testList.add("One");
        testList.add("Two");
        assertEquals(3, testList.size());
    }

    @Test
    void isEmpty() {
        assertTrue(testList.isEmpty());
        testList.add("String");
        assertFalse(testList.isEmpty());
    }

    @Test
    void contains() {
        assertFalse(testList.contains("One"));
        assertFalse(testList.contains(""));
        assertFalse(testList.contains(null));
        testList.add("One");
        testList.add("Three");
        testList.add("Two");
        assertTrue(testList.contains("One"));
        assertFalse(testList.contains("OneOne"));
    }

    @Test
    void add64Items() {
        testList.add("Ten");
        assertTrue(testList.contains("Ten"));
        for (int i = 0; i < 64; i++) {
            testList.add("Ten" + i);
            assertTrue(testList.contains("Ten" + i));
        }
    }

    @Test
    void removeObject() {
        assertFalse(testList.remove("Empty"));
        for (int i = 0; i < 64; i++) {
            testList.add("Ten" + i);
        }
        String toRemove = testList.get(3);
        int size = testList.size();
        assertTrue(testList.remove(toRemove));
        assertFalse(testList.remove("Empty"));
        assertEquals(size - 1, testList.size());
    }

    @Test
    void containsAll() {
        ExtendedList<String> myList = (ExtendedList<String>) testList;
        assertTrue(myList.containsAll(Collections.emptyList()));
        assertFalse(myList.containsAll(Arrays.asList("Three", "One1", "Two")));
        testList.add("One");
        testList.add("Three");
        testList.add("Two");
        assertTrue(myList.containsAll(Arrays.asList("Three", "One", "Two")));
        assertFalse(myList.containsAll(Arrays.asList("Three", "One1", "Two")));
    }

    void addAll() {
        testList.addAll(Arrays.asList("Three", "One", "Two"));
    }

    @Test
    void removeAllDiffTypes() {
        addAll();
        java.util.ArrayList<Object> objList = new java.util.ArrayList<>();
        objList.add(new Object());
        objList.addAll(Arrays.asList("Ten", "Eleven", "Twelve"));
        testList.removeAll(objList);
    }

    @Test
    void addAllCollection() {
        testList.addAll(Arrays.asList("Three", "One", "Two"));
        assertTrue(testList.size() > 0);
        assertEquals(3, testList.size());
    }

    @Test
    void addAllWithIndex() {
        addAll();
        testList.addAll(1, Arrays.asList("Four", "Six", "Five"));
        assertEquals(0, testList.indexOf("Three"));
        assertEquals(1, testList.indexOf("Four"));
        assertEquals(4, testList.indexOf("One"));
        assertEquals(6, testList.size());
    }

    @Test
    void addAllWithIndexToEnd() {
        addAll();
        testList.addAll(testList.size(), Arrays.asList("Four", "Six", "Five"));
        assertEquals(0, testList.indexOf("Three"));
        assertEquals(3, testList.indexOf("Four"));
        assertEquals(1, testList.indexOf("One"));
        assertEquals(6, testList.size());
    }

    @Test
    void addAllOutOfBounds() {
        addAll();
        Throwable thrown = assertThrows(IndexOutOfBoundsException.class, () -> testList.addAll(11, Arrays.asList("Four", "Six", "Five")));
        assertEquals("Index 11 is out of bounds 0..3", thrown.getMessage());
    }

    @Test
    void removeAll() {
        addAll();
        assertEquals(3, testList.size());
        testList.addAll(1, Arrays.asList("Four", "Six", "Five"));
        assertEquals(6, testList.size());
        assertEquals("Two", testList.get(5));
        assertTrue(testList.removeAll(Arrays.asList("Three", "One", "Two")));
        assertFalse(testList.removeAll(Arrays.asList("Three1", "Six", "Two1")));
        assertEquals(2, testList.size());
        assertEquals(-1, testList.indexOf("One"));
        assertTrue(testList.indexOf("Five") > -1);
    }

    @Test
    void removeNothing() {
        addAll();
        assertEquals(3, testList.size());
        assertEquals("Two", testList.get(2));
        assertFalse(testList.removeAll(Arrays.asList("fff", "rrrr", "www")));
        assertEquals(3, testList.size());
    }

    @Test
    void clear() {
        addAll();
        assertEquals(3, testList.size());
        assertFalse(testList.isEmpty());
        testList.clear();
        assertTrue(testList.isEmpty());
    }

    @Test
    void get() {
        addAll();
        String str = testList.get(2);
        assertEquals("Two", str);
    }

    @Test
    void getElementThatIsNotExisting() {
        addAll();
        Throwable thrown = assertThrows(IndexOutOfBoundsException.class, () -> testList.get(15));
        assertEquals("Index 15 is out of bounds 0..2", thrown.getMessage());
    }

    @Test
    void addWithIndex() {
        addAll();
        testList.add("Zero", 0);
        assertEquals("Zero", testList.get(0));
        testList.add("aaaaa", 2);
        assertEquals("aaaaa", testList.get(2));
        assertEquals(5, testList.size());
    }

    @Test
    void addOutOfBound() {
        addAll();
        assertThrows(IndexOutOfBoundsException.class, () -> testList.add("Zero", 10));
    }

    @Test
    void removeOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> testList.remove(1));
    }

    @Test
    void removeOutOfBoundsLessZero() {
        assertThrows(IndexOutOfBoundsException.class, () -> testList.remove(-1));
    }

    @Test
    void removeWithIndex() {
        addAll();
        int size = testList.size();
        String str = testList.remove(2);
        assertEquals("Two", str);
        assertEquals(size - 1, testList.size());
        str = testList.remove(0);
        assertEquals("Three", str);
        assertEquals(size - 2, testList.size());
    }

    @Test
    void indexOf() {
        assertEquals(-1, testList.indexOf("fdfdfdf\""));
        addAll();
        assertEquals(2, testList.indexOf("Two"));
    }

    @Test
    void lastIndexOf() {
        addAll();
        testList.add("Three");
        testList.add("Three2");
        testList.add("Three3");
        assertEquals(3, testList.lastIndexOf("Three"));
        assertEquals(4, testList.lastIndexOf("Three2"));
    }

    @Test
    void addNull() {
        testList.add("Three");
        testList.add("Three2");
        testList.add("Three3");
        testList.add(null);
        testList.add("Three4");
        assertEquals(5, testList.size());
        assertEquals(3, testList.indexOf(null));
        assertEquals(3, testList.lastIndexOf(null));
    }

    @Test
    void setTest() {
        addAll();
        for (int i = 0; i < testList.size(); i++) {
            String str = testList.get(i);
            assertEquals(str, testList.set(str + "111", i));
            assertEquals(str + "111", testList.get(i));
        }
    }

    @Test
    void setOnEmptyTest() {
        Throwable thrown = assertThrows(IndexOutOfBoundsException.class, () -> testList.set("One", 0));
        assertEquals("Can`t apply set on empty list", thrown.getMessage());
    }

    @Test
    void setTestAboveSize() {
        addAll();
        assertThrows(IndexOutOfBoundsException.class, () -> testList.set("111", 25));
    }

    @Test
    void addAndRemoveBig() {
        for (int i = 0; i < 8192; i++) {
            testList.add("Item " + i);
        }
        assertEquals(8192, testList.size());
        for (int i = 0; i < 8192; i++) {
            testList.remove(0);
            assertEquals(8192 - i - 1, testList.size());
        }
        assertEquals(0, testList.size());
        assertTrue(testList.isEmpty());
    }

    @Test
    void addAndRemoveBigFromEnd() {
        for (int i = 0; i < 8192; i++) {
            testList.add("Item " + i);
        }
        assertEquals(8192, testList.size());
        for (int i = 0; i < 8192; i++) {
            String str = testList.get(testList.size() - 1);
            String strRemoved = testList.remove(testList.size() - 1);
            assertEquals(str, strRemoved);
            assertEquals(8192 - i - 1, testList.size());
        }
        assertEquals(0, testList.size());
        assertTrue(testList.isEmpty());
    }

    @Test
    void iteratorTest() {
        testList.add("value0");
        testList.add("value1");
        testList.add("value2");
        final Iterator<String> iter = testList.iterator();
        assertTrue(iter.hasNext());
        int counter = 0;
        while (iter.hasNext()) {
            String str = iter.next();
            counter++;
            assertTrue(str.contains("val"));
        }
        assertEquals(3, counter);
        assertFalse(iter.hasNext());
        Throwable thrown = assertThrows(NoSuchElementException.class, () -> iter.next());
        assertEquals("All elements were fetched in this iterator", thrown.getMessage());
    }

    @Test
    void iteratorRemoveTwice() {
        addAll();
        final Iterator<String> iter = testList.iterator();
        assertTrue(iter.hasNext());
        assertEquals("Three", iter.next());
        iter.remove();
        assertEquals(2, testList.size());
        assertFalse(testList.contains("Three"));
        assertTrue(testList.contains("One"));
        Throwable thrown = assertThrows(IllegalStateException.class, () -> iter.remove());
        assertEquals("The element was already removed", thrown.getMessage());
    }

    @Test
    void toArray() {
        addAll();
        Object[] arr = testList.toArray();
        assertNotNull(arr);
        assertEquals(3, arr.length);
        assertArrayEquals(new Object[]{"Three", "One", "Two"}, arr);
    }

}