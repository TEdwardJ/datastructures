package edu.ted.datastructures.list;

import org.junit.jupiter.api.DisplayName;



public class ArrayListTest extends AbstractListImplementationTest {
    @Override
    public List getList() {
        return new ArrayList<String>();
    }
}
