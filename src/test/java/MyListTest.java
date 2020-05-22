import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class MyListTest {
    private List<String> al;

    public MyListTest(List<String> al) {
        this.al = al;
    }

    @Before
    public void prepareList() {
        al.clear();
    }

    @Test
    public void size() {
        assertEquals(0, al.size());
        al.add("One");
        assertNotEquals(0, al.size());
        al.add("One");
        al.add("Two");
        assertEquals(3, al.size());
    }

    @Test
    public void isEmpty() {
        assertTrue(al.isEmpty());
        al.add("String");
        assertFalse(al.isEmpty());
    }

    @Test
    public void contains() {
        assertFalse(al.contains("One"));
        assertFalse(al.contains(""));
        assertFalse(al.contains(null));
        al.add("One");
        al.add("Three");
        al.add("Two");
        assertTrue(al.contains("One"));
        assertFalse(al.contains("OneOne"));
    }

    @Test
    public void add64Items() {
        assertTrue(al.add("Ten"));
        for (int i = 0; i < 64; i++) {
            assertTrue(al.add("Ten" + i));
        }
    }

    @Test
    public void removeObject() {
        assertFalse(al.remove("Empty"));
        for (int i = 0; i < 64; i++) {
            assertTrue(al.add("Ten" + i));
        }
        String toRemove = al.get(3);
        int size = al.size();
        assertTrue(al.remove(toRemove));
        assertEquals(size - 1, al.size());
    }

    @Test
    public void containsAll() {
        assertTrue(al.containsAll(Arrays.asList(new String[]{})));
        assertFalse(al.containsAll(Arrays.asList("Three", "One1", "Two")));
        al.add("One");
        al.add("Three");
        al.add("Two");
        assertTrue(al.containsAll(Arrays.asList("Three", "One", "Two")));
        assertFalse(al.containsAll(Arrays.asList("Three", "One1", "Two")));
    }

    public void addAll() {
        al.addAll(Arrays.asList("Three", "One", "Two"));
    }

    @Test
    public void removeAllDiffTypes() {
        addAll();
        List<Object> objList = new ArrayList<>();
        objList.add(new Object());
        objList.addAll(Arrays.asList("Ten", "Eleven", "Twelve"));
        al.removeAll(objList);
    }

    @Test
    public void addAllCollection() {
        al.addAll(Arrays.asList("Three", "One", "Two"));
        assertTrue(al.size() > 0);
        assertEquals(3, al.size());
    }

    @Test
    public void addAllWithIndex() {
        addAll();
        al.addAll(1, Arrays.asList("Four", "Six", "Five"));
        assertEquals(0, al.indexOf("Three"));
        assertEquals(1, al.indexOf("Four"));
        assertEquals(4, al.indexOf("One"));
        assertEquals(6, al.size());
    }

    @Test
    public void addAllWithIndexToEnd() {
        addAll();
        al.addAll(al.size(), Arrays.asList("Four", "Six", "Five"));
        assertEquals(0, al.indexOf("Three"));
        assertEquals(3, al.indexOf("Four"));
        assertEquals(1, al.indexOf("One"));
        assertEquals(6, al.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void addAllOutOfBounds() {
        addAll();
        al.addAll(11, Arrays.asList("Four", "Six", "Five"));
    }

    @Test
    public void removeAll() {
        addAll();
        assertEquals(3, al.size());
        al.addAll(1, Arrays.asList("Four", "Six", "Five"));
        assertEquals(6, al.size());
        assertEquals("Two", al.get(5));
        al.removeAll(Arrays.asList("Three", "One", "Two"));
        assertEquals(3, al.size());
        assertTrue(al.indexOf("One") == -1);
        assertTrue(al.indexOf("Five") > -1);
    }

    @Test
    public void removeNothing() {
        addAll();
        assertEquals(3, al.size());
        assertEquals("Two", al.get(2));
        assertFalse(al.removeAll(Arrays.asList("fff", "rrrr", "www")));
        assertEquals(3, al.size());
    }

    @Test
    public void clear() {
        addAll();
        assertEquals(3, al.size());
        assertFalse(al.isEmpty());
        al.clear();
        assertTrue(al.isEmpty());
    }

    @Test
    public void get() {
        addAll();
        String str = al.get(2);
        assertEquals("Two", str);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getUnExisting() {
        addAll();
        String str = al.get(15);
    }

    @Test
    public void addWithIndex() {
        addAll();
        al.add(0, "Zero");
        assertEquals("Zero", al.get(0));
        al.add(2, "aaaaa");
        assertEquals("aaaaa", al.get(2));
        assertEquals(5, al.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void addOutOfBound() {
        addAll();
        al.add(10, "Zero");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeOutOfBounds() {
        al.remove(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeOutOfBoundsLessZero() {
        al.remove(-1);
    }

    @Test
    public void removeWithIndex() {
        addAll();
        int size = al.size();
        String str = al.remove(2);
        assertEquals("Two", str);
        assertEquals(size - 1, al.size());
        str = al.remove(0);
        assertEquals("Three", str);
        assertEquals(size - 2, al.size());
    }

    @Test
    public void indexOf() {
        assertEquals(-1, al.indexOf("fdfdfdf\""));
        addAll();
        assertEquals(2, al.indexOf("Two"));
    }

    @Test
    public void lastIndexOf() {
        addAll();
        al.add("Three");
        al.add("Three2");
        al.add("Three3");
        assertEquals(3, al.lastIndexOf("Three"));
        assertEquals(4, al.lastIndexOf("Three2"));
    }

    @Test
    public void addNull() {
        addAll();
        al.add("Three");
        al.add("Three2");
        al.add("Three3");
        al.add(null);
        al.add("Three4");
        assertEquals(8, al.size());
        assertEquals(6, al.indexOf(null));
        assertEquals(6, al.lastIndexOf(null));
    }

    @Test
    public void setTest() {
        addAll();
        for (int i = 0; i < al.size(); i++) {
            String str = al.get(i);
            assertEquals(str, al.set(i, str + "111"));
            assertEquals(str + "111", al.get(i));
        }
    }


    @Test(expected = IndexOutOfBoundsException.class)
    public void setTestAboveSize() {
        addAll();
        assertNull(al.set(25, "111"));
    }

    @Test
    public void addAndRemoveBig() {
        for (int i = 0; i < 8192; i++) {
            al.add("Item " + i);
        }
        assertEquals(8192, al.size());
        for (int i = 0; i < 8192; i++) {
            al.remove(0);
            assertEquals(8192 - i - 1, al.size());
        }
        assertEquals(0, al.size());
        assertTrue(al.isEmpty());
    }

    @Test
    public void addAndRemoveBigFromEnd() {
        for (int i = 0; i < 8192; i++) {
            al.add("Item " + i);
        }
        assertEquals(8192, al.size());
        for (int i = 0; i < 8192; i++) {
            String str = al.get(al.size() - 1);
            String strRemoved = al.remove(al.size() - 1);
            assertEquals(str, strRemoved);
            assertEquals(8192 - i - 1, al.size());
        }
        assertEquals(0, al.size());
        assertTrue(al.isEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void iterator() {
        al.add("value0");
        al.add("value1");
        al.add("value2");
        Iterator<String> iter = al.iterator();
        assertTrue(iter.hasNext());
        int counter = 0;
        while (iter.hasNext()) {
            String str = iter.next();
            counter++;
            assertTrue(str.contains("val"));
        }
        assertEquals(3, counter);
        assertFalse(iter.hasNext());
        iter.next();
    }

    @Parameterized.Parameters
    public static Collection differentListImplementations() {
        List<List<String>> list = new ArrayList<>();
        list.add(new MyArrayList<String>());
        //list.add(new ArrayList<String>());
        list.add(new MyLinkedList<>());
        ///list.add(new LinkedList<>());
        return list;
    }

    @Test
    public void toArray(){
        addAll();
        Object[] arr = al.toArray();
        assertNotNull(arr);
        assertEquals(3,arr.length);
        assertArrayEquals(new Object[]{"Three","One","Two"},arr);
    }

}