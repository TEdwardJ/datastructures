package edu.ted.datastructures.list;

import org.junit.jupiter.api.DisplayName;

@DisplayName("LinkedList Implementation Testing")
public class LinkedListTest extends AbstractListImplementationTest {
    @Override
    public ExtendedList<String> getList() {
        return new LinkedList<>();
    }
}
