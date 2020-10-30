package persistence;

import model.Item;
import model.Node;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

// code inspired by the JsonSerializationDemo example project
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonWriterTest extends JsonTest {
    @Test
    void WriterInvalidFile() {
        try {
            Node node = new Node(new LinkedList<>(), null);
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void WriteEmptyNodeTest() {
        try {
            Node node = new Node(new LinkedList<>(), null);
            JsonWriter writer = new JsonWriter("testWriterEmptyNode");
            writer.open();
            writer.write(node);
            writer.close();

            JsonReader reader = new JsonReader("testWriterEmptyNode");
            node = reader.read();
            assertEquals("", node.getQuestion());
            assertEquals(0, node.getUnsortedItems().size());
            assertEquals(0, node.getYesItems().size());
            assertEquals(0, node.getNoItems().size());
            assertNull(node.getParentNode());
            assertNull(node.getYesNode());
            assertNull(node.getNoNode());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    void WriteGeneralNodeTest() {
        try {
            Item virus1 = new Item("virus");
            virus1.setDescription("idk if this is alive or not so i wont sort it");
            LinkedList<Item> unsortedRoot = new LinkedList<>();
            unsortedRoot.add(virus1);
            Node node = new Node(unsortedRoot, null);
            node.setQuestion("Is your item alive?");

            Item bear1 = new Item("bear");
            Item duck1 = new Item("duck");
            duck1.setDescription("quack");
            LinkedList<Item> unsortedRootYes = new LinkedList<>();
            unsortedRootYes.add(bear1);
            unsortedRootYes.add(duck1);
            Item aiden1 = new Item("Aiden Kerr");
            Item jesus1 = new Item("Jesus");
            aiden1.setDescription("it's me!");
            LinkedList<Item> yesItemsRootYes = new LinkedList<>();
            yesItemsRootYes.add(aiden1);
            yesItemsRootYes.add(jesus1);
            Node yesNode = new Node(unsortedRootYes, node);
            yesNode.setYesItems(yesItemsRootYes);
            yesNode.setQuestion("Is your item a human?");
            node.setYesNode(yesNode);

            Node noNode = new Node(new LinkedList<>(), node);
            noNode.setQuestion("does your item use electricity?");
            node.setNoNode(noNode);

            LinkedList<Item> yesItemsRootNoYes = new LinkedList<>();
            Item computer = new Item("computer");
            computer.setDescription("use it to write CPSC 210 projects!");
            yesItemsRootNoYes.add(computer);
            LinkedList<Item> noItemsRootNoYes = new LinkedList<>();
            Item hairDryer = new Item("hair dryer");
            Item fan = new Item("fan");
            hairDryer.setDescription("my hair is kinda wet");
            fan.setDescription("to cool my room and also for white noise");
            noItemsRootNoYes.add(hairDryer);
            noItemsRootNoYes.add(fan);
            Node noYesNode = new Node(new LinkedList<>(), noNode);
            noYesNode.setQuestion("does your item use the internet?");
            noYesNode.setYesItems(yesItemsRootNoYes);
            noYesNode.setNoItems(noItemsRootNoYes);

            LinkedList<Item> yesItemsNoNo = new LinkedList<>();
            LinkedList<Item> noItemsNoNo = new LinkedList<>();
            Item car = new Item("car");
            Item stapler = new Item("stapler");
            Item pen = new Item("pen");
            pen.setDescription("use it to write");
            stapler.setDescription("staples are used to attach papers");
            car.setDescription("gas car go brr");
            yesItemsNoNo.add(car);
            noItemsNoNo.add(stapler);
            noItemsNoNo.add(pen);
            Node noNoNode = new Node(new LinkedList<>(), noNode);
            noNoNode.setYesItems(yesItemsNoNo);
            noNoNode.setNoItems(noItemsNoNo);
            noNoNode.setQuestion("is your item powered in some other way?");

            noNode.setYesNode(noYesNode);
            noNode.setNoNode(noNoNode);



            JsonWriter writer = new JsonWriter("testWriterGeneralNode");
            writer.open();
            writer.write(node);
            writer.close();

            JsonReader reader = new JsonReader("testWriterGeneralNode");
            node = reader.read();

            // check root
            assertEquals("Is your item alive?", node.getQuestion());
            assertEquals(1, node.getUnsortedItems().size());
            Item virus = node.getUnsortedItems().get(0);
            checkItem("virus", "idk if this is alive or not so i wont sort it", virus);
            assertEquals(0, node.getYesItems().size());
            assertEquals(0, node.getNoItems().size());

            // check child nodes
            Node rootYesNode = node.getYesNode();
            Node rootNoNode = node.getNoNode();
            assertNotNull(rootYesNode);
            assertNotNull(rootNoNode);
            assertEquals(node, rootYesNode.getParentNode());
            assertEquals(node, rootNoNode.getParentNode());

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
            checkItem("stapler", "staples are used to attach papers", rootNoNoNoItems.get(0));
            checkItem("pen", "use it to write", rootNoNoNoItems.get(1));
            assertNull(rootNoNoNode.getYesNode());
            assertNull(rootNoNoNode.getNoNode());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
}
