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

    @Test
    public void getDescriptionTest() {
        String expected = Item.NO_DESCRIPTION;
        assertEquals(expected, item.getDescription());
    }

    @Test
    public void setDescriptionSuccessTest() {
        item.setDescription("An animal native to Australia");
        assertEquals("An animal native to Australia", item.getDescription());
    }

    @Test
    public void setDescriptionNullTest() {
        // set it first to ensure that this test isn't just testing the default string
        item.setDescription("An animal native to Australia");
        assertEquals("An animal native to Australia", item.getDescription());

        String expected = Item.NO_DESCRIPTION;
        item.setDescription(null);
        assertEquals(expected, item.getDescription());
    }

    @Test
    public void setDescriptionEmptyStringTest() {
        // set it first to ensure that this test isn't just testing the default string
        item.setDescription("An animal native to Australia");
        assertEquals("An animal native to Australia", item.getDescription());

        String expected = Item.NO_DESCRIPTION;
        item.setDescription("");
        assertEquals(expected, item.getDescription());
    }

    @Test
    public void setDescriptionSpacesTest() {
        // set it first to ensure that this test isn't just testing the default string
        item.setDescription("An animal native to Australia");
        assertEquals("An animal native to Australia", item.getDescription());

        String expected = Item.NO_DESCRIPTION;
        item.setDescription(" ");
        assertEquals(expected, item.getDescription());

        expected = Item.NO_DESCRIPTION;
        item.setDescription("  ");
        assertEquals(expected, item.getDescription());

        expected = Item.NO_DESCRIPTION;
        item.setDescription("   ");
        assertEquals(expected, item.getDescription());
    }

    @Test
    void toStringTest() {
        assertEquals("Koala", item.toString());
    }
}