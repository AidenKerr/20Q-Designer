package persistence;

import model.Node;
import org.json.JSONObject;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// code inspired by the JsonSerializationDemo example project
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
// This class is used for saving question node trees as a JSON object
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // MODIFIES: this
    // EFFECTS: sets file destination path
    public JsonWriter(String path) throws IOException {
        if (path.contains("./")) {
            throw new IOException();
        }
        destination = "./data/" + path + ".json";
    }

    // MODIFIES: this
    // EFFECTS: sets up the writer
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // EFFECTS: converts given node to json and then saves it
    public void write(Node node) {
        JSONObject json = node.toJson();
        saveToFile(json.toString(TAB));
    }

    // EFFECTS: saves given json string to file
    private void saveToFile(String json) {
        writer.print(json);
    }

    // MODIFIES: this
    // EFFECTS: closes the writer
    public void close() {
        writer.close();
    }
}
