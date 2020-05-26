package edu.ted.datastructures.list;

import org.junit.jupiter.api.DisplayName;

@DisplayName("LinkedList Implementation Testing")
public class LinkedListTest extends AbstractListImplementationTest {
    @Override
    public List getList() {
        return new LinkedList<String>();
    }
}
