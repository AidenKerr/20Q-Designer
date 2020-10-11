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

    // REQUIRES: item list is not empty
    // MODIFIES: this
    // EFFECTS: move first item in unsorted list into the no list
    public void moveItemNo() {
        Item item = unsortedItems.poll();
        noItems.add(item);
    }

    // REQUIRES: item list is not empty
    // MODIFIES: this
    // EFFECTS: move first item in unsorted list into the yes list
    public void moveItemYes() {
        Item item = unsortedItems.poll();
        yesItems.add(item);
    }

    /*
        MODIFIES: this
        EFFECTS: if unsorted list is empty, and yesList/noList both have 1 or more item, create new Nodes for
        yesNode and noNode with each initialized with yesItems or noItems respectively, with this object as parent
        and return true. Otherwise return false.
     */
    public boolean createNextNodes() {

        boolean unsortedListEmpty = unsortedItems.size() == 0;
        boolean noListHasItems = noItems.size() >= 1;
        boolean yesListHasItems = yesItems.size() >= 1;

        if (unsortedListEmpty & noListHasItems & yesListHasItems) {
            Node yesNode = new Node(yesItems, this);
            Node noNode = new Node(noItems, this);

            this.yesNode = yesNode;
            this.noNode = noNode;

            return true;
        }

        return false;
    }

    // EFFECTS: if noNode exists, return noNode. Else return null.
    public Node getNoNode() {
        return noNode; // stub
    }

    // EFFECTS: if yesNode exists, return yesNode. Else return null.
    public Node getYesNode() {
        return yesNode; // stub
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
}
