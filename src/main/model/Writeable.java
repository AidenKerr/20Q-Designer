package model;

import org.json.JSONObject;

// code inspired by the JsonSerializationDemo example project
public interface Writeable {
    // Effects: returns this as a JSON Object
    JSONObject toJson();
}
