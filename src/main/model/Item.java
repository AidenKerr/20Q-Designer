package model;

/*
    Item represents an Item that can be used in the 20 Question game

    Users can create items, and optionally set their description
 */

import org.json.JSONObject;

public class Item implements Writeable {

    public static final String NO_DESCRIPTION = "No Description Provided";

    private String name;
    private String description;

    public Item(String name) {
        this.name = name;
        this.description = NO_DESCRIPTION;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // MODIFIES: this
    // EFFECT: if desc is a valid description (not null or empty), change description to desc
    public void setDescription(String desc) {
        // space check provided by:
        // https://stackoverflow.com/questions/3247067/how-do-i-check-that-a-java-string-is-not-all-whitespaces
        if (desc == null || desc.trim().length() == 0) {
            description = NO_DESCRIPTION;
        } else {
            description = desc;
        }
    }

    @Override
    // EFFECTS: returns the Item as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("description", description);
        return json;
    }

    // EFFECTS: returns name of item
    @Override
    public String toString() {
        return name;
    }
}
