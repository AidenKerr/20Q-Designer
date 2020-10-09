package model;


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
