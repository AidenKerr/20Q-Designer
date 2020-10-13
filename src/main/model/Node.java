package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
    A question node in the binary tree

    unsortedItems is a list of items for which the question has not been answered.
    Items can be sorted into yesList or noList.
    When all items are sorted, new question nodes can be created for the yes/no nodes, where their respective lists are
    now in the unsorted list.

    Some inspiration for the structure of binary trees was taken from
    https://www.geeksforgeeks.org/binary-search-tree-set-1-search-and-insertion/
 */
public class Node {

    private String question;
    private LinkedList<Item> unsortedItems;
    private LinkedList<Item> yesItems;
    private LinkedList<Item> noItems;
    private Node parentNode;
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

    // EFFECTS: returns the question
    public String getQuestion() {
        return question;
    }

    // MODIFIES: this
    // EFFECTS: modifies the question
    public void setQuestion(String question) {
        this.question = question;
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

    // EFFECTS: if noNode exists, return noNode. Else return null.
    public Node getNoNode() {
        return noNode;
    }

    // EFFECTS: if yesNode exists, return yesNode. Else return null.
    public Node getYesNode() {
        return yesNode;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public List<Item> getYesItems() {
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
}
