package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NodeTest {

    Node node;

    @BeforeEach
    public void beforeEach() {
        Item item1 = new Item("Han Solo");
        Item item2 = new Item("Ada Lovelace");
        Item item3 = new Item("John Lennon");
        LinkedList<Item> items = new LinkedList<>();
        items.add(item1);
        items.add(item2);
        items.add(item3);

        System.out.println("test");

        node = new Node(items, null);
    }

    @Test
    public void ConstructorTest() {
        LinkedList<Item> items = node.getUnsortedItems();
        assertNull(node.getParentNode());

        assertEquals("Han Solo", items.poll().getName());
        assertEquals("Ada Lovelace", items.poll().getName());
        assertEquals("John Lennon", items.poll().getName());
    }

    @Test
    public void getSetQuestionTest() {
        node.setQuestion("Is your item a real human?");
        String answer = node.getQuestion();

        assertEquals("Is your item a real human?", answer);
    }

    @Test
    public void moveOneItemNoTest() {
        node.moveItemNo();

        LinkedList<Item> noList = node.getNoItems();

        assertEquals(1, noList.size());

        List<Item> unsorted = node.getUnsortedItems();
        assertEquals(2, unsorted.size());

        Item item = noList.get(0);
        assertEquals("Han Solo", item.getName());
    }

    @Test
    public void moveManyItemNoTest() {

        List<Item> unsorted;

        for (int i = 1; i <= 3; i++) {
            node.moveItemNo();
            unsorted = node.getUnsortedItems();
            assertEquals(3 - i, unsorted.size());
        }

        List<Item> noList = node.getNoItems();

        assertEquals(3, noList.size());

        Item item1 = noList.get(0);
        Item item2 = noList.get(1);
        Item item3 = noList.get(2);

        assertEquals("Han Solo", item1.getName());
        assertEquals("Ada Lovelace", item2.getName());
        assertEquals("John Lennon", item3.getName());
    }

    @Test
    public void moveOneItemYesTest() {
        node.moveItemYes();

        List<Item> yesList = node.getYesItems();

        assertEquals(1, yesList.size());

        List<Item> unsorted = node.getUnsortedItems();
        assertEquals(2, unsorted.size());

        Item item = yesList.get(0);
        assertEquals("Han Solo", item.getName());
    }

    @Test
    public void moveManyItemYesTest() {

        List<Item> unsorted;

        for (int i = 1; i <= 3; i++) {
            node.moveItemYes();
            unsorted = node.getUnsortedItems();
            assertEquals(3 - i, unsorted.size());
        }

        List<Item> yesList = node.getYesItems();

        assertEquals(3, yesList.size());

        Item item1 = yesList.get(0);
        Item item2 = yesList.get(1);
        Item item3 = yesList.get(2);

        assertEquals("Han Solo", item1.getName());
        assertEquals("Ada Lovelace", item2.getName());
        assertEquals("John Lennon", item3.getName());
    }

    @Test
    public void createNextNodesFailUnsortedTest() {
        assertFalse(node.createNextNodes());

        node.moveItemYes();

        assertFalse(node.createNextNodes());

        node.moveItemYes();

        assertFalse(node.createNextNodes());

        node.moveItemNo();
    }

    @Test
    public void createNextNodesNotEnoughYesTest() {
        LinkedList<Item> items = new LinkedList<>();
        items.add(new Item("Charles Babbage"));

        Node testNode = new Node(items, null);
        testNode.moveItemNo();

        assertFalse(testNode.createNextNodes());
    }

    @Test
    public void createNextNodesNotEnoughNoTest() {
        LinkedList<Item> items = new LinkedList<>();
        items.add(new Item("Charles Babbage"));

        Node testNode = new Node(items, null);
        testNode.moveItemYes();

        assertFalse(testNode.createNextNodes());
    }

    @Test
    public void createNextNodesPassTest() {
        node.moveItemYes();
        node.moveItemNo();
        node.moveItemYes();

        // set question is used to check parent nodes later
        node.setQuestion("createNextNodesPassTest question");

        assertTrue(node.createNextNodes());

        Node yesNode = node.getYesNode();
        Node noNode = node.getNoNode();

        Node yesParent = yesNode.getParentNode();
        Node noParent = noNode.getParentNode();

        // check children's parent is original node
        assertEquals("createNextNodesPassTest question", yesParent.getQuestion());
        assertEquals("createNextNodesPassTest question", noParent.getQuestion());

        List<Item> noList = noNode.getUnsortedItems();
        List<Item> yesList = yesNode.getUnsortedItems();

        assertEquals(1, noList.size());
        assertEquals(2, yesList.size());

        Item noItem = noList.get(0);
        Item yesItem1 = yesList.get(0);
        Item yesItem2 = yesList.get(1);

        assertEquals("Ada Lovelace", noItem.getName());
        assertEquals("Han Solo", yesItem1.getName());
        assertEquals("John Lennon", yesItem2.getName());
    }
}
