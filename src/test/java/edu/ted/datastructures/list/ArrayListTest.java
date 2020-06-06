package edu.ted.datastructures.list;

import org.junit.jupiter.api.DisplayName;

@DisplayName("ArrayList Implementation Testing")
public class ArrayListTest extends AbstractListImplementationTest {
    @Override
    public ExtendedList<String> getList() {
        return new ArrayList<>();
    }
}
