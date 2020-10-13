package model;

/*
    Item represents an Item that can be used in the 20 Question game

    Users can create items, and optionally set their description (TODO)
 */


public class Item {

    private String name;

    public Item(String name) {
        this.name = name;
    }

    // EFFECTS: returns the items name
    public String getName() {
        return name;
    }
}
