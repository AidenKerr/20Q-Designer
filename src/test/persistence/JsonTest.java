package persistence;

import model.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;

// code inspired by the JsonSerializationDemo example project
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonTest {
    protected void checkItem(String name, String description, Item item) {
        assertEquals(name, item.getName());
        assertEquals(description, item.getDescription());
    }
}
