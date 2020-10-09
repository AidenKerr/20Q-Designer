package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    private Item item;

    @BeforeEach
    public void beforeEach() {
        item = new Item("Koala");
    }

    @Test
    public void getNameTest() {
        assertEquals("Koala", item.getName());
    }
}