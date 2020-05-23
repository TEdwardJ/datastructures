package edu.ted.datastructures.list;

import edu.ted.datastructures.list.interfaces.ExtendedList;
import edu.ted.datastructures.list.interfaces.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("List Implementations Testing")
public class ListImplementationsTest {

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void size(List<String> testList) {
        assertEquals(0, testList.size());
        testList.add("One");
        assertNotEquals(0, testList.size());
        testList.add("One");
        testList.add("Two");
        assertEquals(3, testList.size());
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void isEmpty(List<String> testList) {
        assertTrue(testList.isEmpty());
        testList.add("String");
        assertFalse(testList.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void contains(List<String> testList) {
        assertFalse(testList.contains("One"));
        assertFalse(testList.contains(""));
        assertFalse(testList.contains(null));
        testList.add("One");
        testList.add("Three");
        testList.add("Two");
        assertTrue(testList.contains("One"));
        assertFalse(testList.contains("OneOne"));
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void add64Items(List<String> testList) {
        testList.add("Ten");
        assertTrue(testList.contains("Ten"));
        for (int i = 0; i < 64; i++) {
            testList.add("Ten" + i);
            assertTrue(testList.contains("Ten" + i));
        }
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void removeObject(List<String> testList) {
        assertFalse(((ExtendedList) testList).remove("Empty"));
        for (int i = 0; i < 64; i++) {
            testList.add("Ten" + i);
        }
        String toRemove = testList.get(3);
        int size = testList.size();
        assertTrue(((ExtendedList) testList).remove(toRemove));
        assertFalse(((ExtendedList) testList).remove("Empty"));
        assertEquals(size - 1, testList.size());
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void containsAll(List<String> testList) {
        ExtendedList<String> myList = (ExtendedList<String>) testList;
        assertTrue(myList.containsAll(Arrays.asList(new String[]{})));
        assertFalse(myList.containsAll(Arrays.asList("Three", "One1", "Two")));
        testList.add("One");
        testList.add("Three");
        testList.add("Two");
        assertTrue(myList.containsAll(Arrays.asList("Three", "One", "Two")));
        assertFalse(myList.containsAll(Arrays.asList("Three", "One1", "Two")));
    }

    public void addAll(List<String> testList) {
        ((ExtendedList<String>) testList).addAll(Arrays.asList("Three", "One", "Two"));
    }

    public void addAll(ExtendedList<String> testList) {
        ((ExtendedList<String>) testList).addAll(Arrays.asList("Three", "One", "Two"));
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void removeAllDiffTypes(List<String> testList) {
        addAll(testList);
        java.util.ArrayList<Object> objList = new java.util.ArrayList<Object>();
        objList.add(new Object());
        objList.addAll(Arrays.asList("Ten", "Eleven", "Twelve"));
        ((ExtendedList<String>) testList).removeAll(objList);
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void addAllCollection(List<String> testList) {
        ((ExtendedList<String>) testList).addAll(Arrays.asList("Three", "One", "Two"));
        assertTrue(testList.size() > 0);
        assertEquals(3, testList.size());
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void addAllWithIndex(List<String> testList) {
        addAll(testList);
        ((ExtendedList<String>) testList).addAll(1, Arrays.asList("Four", "Six", "Five"));
        assertEquals(0, testList.indexOf("Three"));
        assertEquals(1, testList.indexOf("Four"));
        assertEquals(4, testList.indexOf("One"));
        assertEquals(6, testList.size());
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void addAllWithIndexToEnd(List<String> testList) {
        addAll(testList);
        ((ExtendedList<String>) testList).addAll(testList.size(), Arrays.asList("Four", "Six", "Five"));
        assertEquals(0, testList.indexOf("Three"));
        assertEquals(3, testList.indexOf("Four"));
        assertEquals(1, testList.indexOf("One"));
        assertEquals(6, testList.size());
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void addAllOutOfBounds(final ExtendedList<String> testList) {
        addAll(testList);
        assertThrows(IndexOutOfBoundsException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                testList.addAll(11, Arrays.asList("Four", "Six", "Five"));
            }
        });
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void removeAll(List<String> testList) {
        addAll(testList);
        assertEquals(3, testList.size());
        ((ExtendedList<String>) testList).addAll(1, Arrays.asList("Four", "Six", "Five"));
        assertEquals(6, testList.size());
        assertEquals("Two", testList.get(5));
        assertTrue(((ExtendedList<String>) testList).removeAll(Arrays.asList("Three", "One", "Two")));
        assertFalse(((ExtendedList<String>) testList).removeAll(Arrays.asList("Three1", "Six", "Two1")));
        assertEquals(2, testList.size());
        assertTrue(testList.indexOf("One") == -1);
        assertTrue(testList.indexOf("Five") > -1);
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void removeNothing(List<String> testList) {
        addAll(testList);
        assertEquals(3, testList.size());
        assertEquals("Two", testList.get(2));
        assertFalse(((ExtendedList<String>) testList).removeAll(Arrays.asList("fff", "rrrr", "www")));
        assertEquals(3, testList.size());
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void clear(List<String> testList) {
        addAll(testList);
        assertEquals(3, testList.size());
        assertFalse(testList.isEmpty());
        testList.clear();
        assertTrue(testList.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void get(List<String> testList) {
        addAll(testList);
        String str = testList.get(2);
        assertEquals("Two", str);
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void getUnExisting(final List<String> testList) {
        addAll(testList);
        assertThrows(IndexOutOfBoundsException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                String str = testList.get(15);
            }
        });
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void addWithIndex(List<String> testList) {
        addAll(testList);
        testList.add("Zero", 0);
        assertEquals("Zero", testList.get(0));
        testList.add("aaaaa", 2);
        assertEquals("aaaaa", testList.get(2));
        assertEquals(5, testList.size());
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void addOutOfBound(final List<String> testList) {
        addAll(testList);
        assertThrows(IndexOutOfBoundsException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                testList.add("Zero", 10);
            }
        });
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void removeOutOfBounds(final List<String> testList) {
        assertThrows(IndexOutOfBoundsException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                testList.remove(1);
            }
        });
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void removeOutOfBoundsLessZero(final List<String> testList) {
        assertThrows(IndexOutOfBoundsException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                testList.remove(-1);
            }
        });
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void removeWithIndex(final List<String> testList) {
        addAll(testList);
        int size = testList.size();
        String str = testList.remove(2);
        assertEquals("Two", str);
        assertEquals(size - 1, testList.size());
        str = testList.remove(0);
        assertEquals("Three", str);
        assertEquals(size - 2, testList.size());
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void indexOf(final List<String> testList) {
        assertEquals(-1, testList.indexOf("fdfdfdf\""));
        addAll(testList);
        assertEquals(2, testList.indexOf("Two"));
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void lastIndexOf(final List<String> testList) {
        addAll(testList);
        testList.add("Three");
        testList.add("Three2");
        testList.add("Three3");
        assertEquals(3, testList.lastIndexOf("Three"));
        assertEquals(4, testList.lastIndexOf("Three2"));
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void addNull(final List<String> testList) {
        testList.add("Three");
        testList.add("Three2");
        testList.add("Three3");
        testList.add(null);
        testList.add("Three4");
        assertEquals(5, testList.size());
        assertEquals(3, testList.indexOf(null));
        assertEquals(3, testList.lastIndexOf(null));
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void setTest(final List<String> testList) {
        addAll(testList);
        for (int i = 0; i < testList.size(); i++) {
            String str = testList.get(i);
            assertEquals(str, testList.set(str + "111", i));
            assertEquals(str + "111", testList.get(i));
        }
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void setTestAboveSize(final List<String> testList) {
        addAll(testList);
        assertThrows(IndexOutOfBoundsException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                assertNull(testList.set("111", 25));
            }
        });
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void addAndRemoveBig(final List<String> testList) {
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

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void addAndRemoveBigFromEnd(final List<String> testList) {
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

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void iteratorTest(final List<String> testList) {
        testList.add("value0");
        testList.add("value1");
        testList.add("value2");
        final Iterator<String> iter = ((Iterable) testList).iterator();
        assertTrue(iter.hasNext());
        int counter = 0;
        while (iter.hasNext()) {
            String str = iter.next();
            counter++;
            assertTrue(str.contains("val"));
        }
        assertEquals(3, counter);
        assertFalse(iter.hasNext());
        assertThrows(NoSuchElementException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                iter.next();
            }
        });
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void iteratorRemoveTwice(final List<String> testList) {
        addAll(testList);
        final Iterator<String> iter = ((Iterable) testList).iterator();
        assertTrue(iter.hasNext());
        assertEquals("Three", iter.next());
        iter.remove();
        assertEquals(2, testList.size());
        assertFalse(testList.contains("Three"));
        assertTrue(testList.contains("One"));
        assertThrows(IllegalStateException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                iter.remove();
            }
        });
    }

    @ParameterizedTest
    @MethodSource("differentListImplementations")
    public void toArray(final List<String> testList) {
        addAll(testList);
        Object[] arr = ((ExtendedList<String>) testList).toArray();
        assertNotNull(arr);
        assertEquals(3, arr.length);
        assertArrayEquals(new Object[]{"Three", "One", "Two"}, arr);
    }

    public static Collection differentListImplementations() {
        java.util.ArrayList<List<String>> list = new java.util.ArrayList<List<String>>();
        list.add(new edu.ted.datastructures.list.ArrayList<String>());
        list.add(new LinkedList<String>());
        return list;
    }

}