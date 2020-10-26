package persistence;

import model.Item;
import model.Node;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.stream.Stream;

// code inspired by the JsonSerializationDemo example project
public class JsonReader {
    private final String source;

    // MODIFIES: this
    // EFFECTS: sets source path
    public JsonReader(String path) {
        source = path;
    }

    // REQUIRES: given file is a valid JSON representation of a Node
    // EFFECTS: returns Node from given path to JSON file
    public Node read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseNode(jsonObject, null);
    }

    // EFFECTS: reads given file and returns a as String
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // REQUIRES: jsonObject is valid JSON representation of a Node, parent is parent Node or null
    // EFFECTS: returns Node from JSON representation, recursively including the yes and no Nodes.
    private Node parseNode(JSONObject jsonObject, Node parent) {
        String question = jsonObject.getString("question");
        LinkedList<Item> unsortedItems = makeItems(jsonObject, "unsortedItems");
        LinkedList<Item> yesItems = makeItems(jsonObject, "yesItems");
        LinkedList<Item> noItems = makeItems(jsonObject, "noItems");

        Node node = new Node(unsortedItems, parent);
        node.setQuestion(question);
        node.setYesItems(yesItems);
        node.setNoItems(noItems);

        Object yesObject = jsonObject.get("yesNode");
        Object noObject = jsonObject.get("noNode");

        // will return a ClassCastException when null - this is the base case
        try {
            Node yesNode = parseNode((JSONObject) yesObject, node);
            Node noNode = parseNode((JSONObject) noObject, node);

            node.setYesNode(yesNode);
            node.setNoNode(noNode);
        } catch (ClassCastException e) {
            // base case. yesNode and noNode remain as null
        }

        return node;
    }

    // REQUIRES: jsonObject is valid JSON representation of Node, key is "unsortedItems", "yesItems", or "noItems"
    // EFFECTS: returns list of Items for given key in jsonObject
    private LinkedList<Item> makeItems(JSONObject jsonObject, String key) {
        JSONArray jsonArray = jsonObject.getJSONArray(key);
        LinkedList<Item> list = new LinkedList<>();
        for (Object json : jsonArray) {
            JSONObject nextItem = (JSONObject) json;
            list.add(makeItem(nextItem));
        }
        return list;
    }

    // REQUIRES: item must be a valid JSON representation of an Item object
    // EFFECTS: returns Item object from given JSON representation
    private Item makeItem(JSONObject item) {
        String name = item.getString("name");
        String description = item.getString("description");
        Item newItem = new Item(name);
        newItem.setDescription(description);
        return newItem;
    }
}
