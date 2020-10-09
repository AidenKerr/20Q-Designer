package model;

import java.util.ArrayList;
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
    private List<Item> unsortedItems;
    private List<Item> yesItems;
    private List<Item> noItems;
    private Node parentNode;
    private Node yesNode;
    private Node noNode;

    // MODIFIES: this
    // EFFECTS: initializes the question's fields
    public Node(String question, List<Item> items, Node parent) {
        this.question = question;
        unsortedItems = items;
        yesItems = new ArrayList<>();
        noItems = new ArrayList<>();
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

    // REQUIRES: name is in unsortedList
    // MODIFIES: this
    // EFFECTS: move item with given name into the no list
    public void moveItemNo(String itemName) {
        return; // stub
    }

    // REQUIRES: name is in unsortedList
    // MODIFIES: this
    // EFFECTS: move item with given name into the yes list
    public void moveItemYes(String itemName) {
        return; // stub
    }


    // EFFECTS: if noNode exists, return noNode. Else return null.
    public Node getNoNode() {
        return null; // stub
    }

    // EFFECTS: if yesNode exists, return yesNode. Else return null.
    public Node getYesNode() {
        return null; // stub
    }

    // EFFECTS: returns parent node
    public Node getParentNode() {
        return null;
    }

    // MODIFIES: this
    // EFFECTS: if yesItems and noItems each have one item, create new Questions for yesNode and noNode with each
    // initialized with yesItems or noItems respectively, with this object as parent and return true.
    // Otherwise return false.
    public boolean next() {
        return false; // stub
    }


}
