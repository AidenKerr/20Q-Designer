package persistence;

import model.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;

// code inspired by the JsonSerializationDemo example project
public class JsonTest {
    protected void checkItem(String name, String description, Item item) {
        assertEquals(name, item.getName());
        assertEquals(description, item.getDescription());
    }
}
