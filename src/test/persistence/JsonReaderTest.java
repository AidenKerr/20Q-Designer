package persistence;

import model.Item;
import model.Node;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

// code inspired by the JsonSerializationDemo example project
public class JsonReaderTest extends JsonTest {
    @Test
    public void testReaderNoFile() {
        JsonReader reader = new JsonReader("./data/fakeFile.json");
        try {
            Node root = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testReaderEmptyNode() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyNode.json");
        try {
            Node root = reader.read();
            assertEquals("", root.getQuestion());
            assertEquals(0, root.getUnsortedItems().size());
            assertEquals(0, root.getYesItems().size());
            assertEquals(0, root.getNoItems().size());
            assertNull(root.getYesNode());
            assertNull(root.getNoNode());
        } catch (IOException e) {
            fail("Couldn't read file");
        }
    }

    @Test
    public void testReaderGeneralNode() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralNode.json");
        try {
            Node root = reader.read();

            // check root
            assertEquals("Is your item alive?", root.getQuestion());
            assertEquals(1, root.getUnsortedItems().size());
            Item virus = root.getUnsortedItems().get(0);
            checkItem("virus", "idk if this is alive or not so i wont sort it", virus);
            assertEquals(0, root.getYesItems().size());
            assertEquals(0, root.getNoItems().size());

            // check child nodes
            Node rootYesNode = root.getYesNode();
            Node rootNoNode = root.getNoNode();
            assertNotNull(rootYesNode);
            assertNotNull(rootNoNode);
            assertEquals(root, rootYesNode.getParentNode());
            assertEquals(root, rootNoNode.getParentNode());

            // yes
            assertEquals("Is your item a human?", rootYesNode.getQuestion());
            LinkedList<Item> rootYesUnsortedItems = rootYesNode.getUnsortedItems();
            assertEquals(2, rootYesUnsortedItems.size());
            Item bear = rootYesUnsortedItems.get(0);
            Item duck = rootYesUnsortedItems.get(1);
            checkItem("bear", "No Description Provided", bear);
            checkItem("duck", "quack", duck);
            LinkedList<Item> rootYesYesItems = rootYesNode.getYesItems();
            assertEquals(2, rootYesYesItems.size());
            checkItem("Aiden Kerr", "it's me!", rootYesYesItems.get(0));
            checkItem("Jesus", "No Description Provided", rootYesYesItems.get(1));
            assertEquals(0, rootYesNode.getNoItems().size());

            // no
            assertEquals("does your item use electricity?", rootNoNode.getQuestion());
            assertEquals(0, rootNoNode.getUnsortedItems().size());
            assertEquals(0, rootNoNode.getYesItems().size());
            assertEquals(0, rootNoNode.getNoItems().size());

            // check child's child nodes
            Node rootNoYesNode = rootNoNode.getYesNode();
            Node rootNoNoNode = rootNoNode.getNoNode();
            assertNotNull(rootNoYesNode);
            assertNotNull(rootNoNoNode);
            assertEquals(rootNoNode, rootNoYesNode.getParentNode());
            assertEquals(rootNoNode, rootNoNoNode.getParentNode());

            // yes
            assertEquals("does your item use the internet?", rootNoYesNode.getQuestion());
            assertEquals(0, rootNoYesNode.getUnsortedItems().size());
            assertEquals(1, rootNoYesNode.getYesItems().size());
            checkItem("computer", "use it to write CPSC 210 projects!", rootNoYesNode.getYesItems().get(0));
            LinkedList<Item> rootNoYesNoItems = rootNoYesNode.getNoItems();
            assertEquals(2, rootNoYesNoItems.size());
            checkItem("hair dryer", "my hair is kinda wet", rootNoYesNoItems.get(0));
            checkItem("fan", "to cool my room and also for white noise", rootNoYesNoItems.get(1));
            assertNull(rootNoYesNode.getYesNode());
            assertNull(rootNoYesNode.getNoNode());

            // no
            assertEquals("is your item powered in some other way?", rootNoNoNode.getQuestion());
            assertEquals(0, rootNoNoNode.getUnsortedItems().size());
            assertEquals(1, rootNoNoNode.getYesItems().size());
            checkItem("car", "gas car go brr", rootNoNoNode.getYesItems().get(0));
            LinkedList<Item> rootNoNoNoItems = rootNoNoNode.getNoItems();
            assertEquals(2, rootNoNoNode.getNoItems().size());
            checkItem("stapler","staples are used to attach papers", rootNoNoNoItems.get(0));
            checkItem("pen","use it to write", rootNoNoNoItems.get(1));
            assertNull(rootNoNoNode.getYesNode());
            assertNull(rootNoNoNode.getNoNode());
        } catch (IOException e) {
            fail("Couldn't read file");
        }
    }
}
