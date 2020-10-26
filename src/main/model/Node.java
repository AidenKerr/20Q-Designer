package model;

import java.util.LinkedList;
import java.util.List;

/*
    A question node in the binary tree

    This class represents the question and all it's associated items.

    ITEMS:
    * unsortedItems is a list of items for which the question has not yet been answered.
    * yesList is a list of items that have been sorted yes
    * noList is a list of items that have been sorted no

    yesList and noList are temporary lists for when the child nodes are not yet created.
    When they each have at least one item, the next nodes are automatically generated and the items are move over.

    Some inspiration for the structure of binary trees was taken from
    https://www.geeksforgeeks.org/binary-search-tree-set-1-search-and-insertion/
 */
public class Node {

    private String question;
    private LinkedList<Item> unsortedItems;
    private LinkedList<Item> yesItems;
    private LinkedList<Item> noItems;
    private final Node parentNode;
    private Node yesNode;
    private Node noNode;

    // REQUIRES: parent is the parent node of this.
    // MODIFIES: this
    // EFFECTS: initializes the question's fields
    public Node(LinkedList<Item> items, Node parent) {
        this.question = "";
        unsortedItems = items;
        yesItems = new LinkedList<>();
        noItems = new LinkedList<>();
        parentNode = parent;
    }

    // MODIFIES: this
    // EFFECTS: if noNode exists, add first item in unsorted list directly to noNode.
    // Otherwise, if item exists, add to no list and attempt to make noNode.
    public void moveItemNo() {

        Item item = unsortedItems.poll();

        if (item != null) {
            if (noNode != null) {
                noNode.addUnsortedItem(item);
                noItems = new LinkedList<>();
            } else {
                noItems.add(item);
                createNextNodes();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: if yesNode exists, add first item in unsorted list directly to yesNode.
    // Otherwise, if item exists, add to yes list and attempt to make yesNode.
    public void moveItemYes() {
        Item item = unsortedItems.poll();

        if (item != null) {
            if (yesNode != null) {
                yesNode.addUnsortedItem(item);
            } else {
                yesItems.add(item);
                createNextNodes();
            }
        }
    }

    /*
        MODIFIES: this
        EFFECTS: if yesList/noList both have 1 or more item, create new Nodes for yesNode and noNode with each
        initialized with yesItems or noItems respectively, with this object as parent, and empty old lists.
     */
    private void createNextNodes() {

        boolean noListHasItems = noItems.size() >= 1;
        boolean yesListHasItems = yesItems.size() >= 1;

        if (noListHasItems & yesListHasItems) {
            Node yesNode = new Node(yesItems, this);
            Node noNode = new Node(noItems, this);

            this.yesNode = yesNode;
            this.noNode = noNode;

            // reset item lists
            yesItems = new LinkedList<>();
            noItems = new LinkedList<>();
        }
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Node getNoNode() {
        return noNode;
    }

    public Node getYesNode() {
        return yesNode;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public LinkedList<Item> getYesItems() {
        return yesItems;
    }

    public LinkedList<Item> getNoItems() {
        return noItems;
    }

    public LinkedList<Item> getUnsortedItems() {
        return unsortedItems;
    }

    public void addUnsortedItem(Item item) {
        unsortedItems.add(item);
    }

    // REQUIRES: items are being set during file reading, not while using program normally
    // MODIFIES: this
    // EFFECTS: set noItems as given items list
    public void setNoItems(LinkedList<Item> items) {
        noItems = items;
    }

    // REQUIRES: items are being set during file reading, not while using program normally
    // MODIFIES: this
    // EFFECTS: set yesItems as given items list
    public void setYesItems(LinkedList<Item> items) {
        yesItems = items;
    }

    // REQUIRES: node is being set during file reading, not while using program normally
    // MODIFIES: this
    // EFFECTS: set yesNode to given node
    public void setYesNode(Node yesNode) {
        this.yesNode = yesNode;
    }

    // REQUIRES: node is being set during file reading, not while using program normally
    // MODIFIES: this
    // EFFECTS: set noNode to given node
    public void setNoNode(Node noNode) {
        this.noNode = noNode;
    }
}
