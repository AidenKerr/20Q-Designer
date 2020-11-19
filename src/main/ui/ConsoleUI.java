package ui;

import model.Item;
import model.Node;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

// User interface code
// Inspired by code in the TellerApp example provided
public class ConsoleUI {

    Node root;
    Node currentNode;

    private Scanner scan;

    // MODIFIES: this
    // EFFECTS: set up and run the program
    public ConsoleUI() {
        LinkedList<Item> defaultList = new LinkedList<>();
        root = new Node(defaultList, null);
        currentNode = root;
        scan = new Scanner(System.in);

        runApp();
    }

    // MODIFIES: this
    // EFFECTS: display welcome message, take user input, and process command.
    private void runApp() {
        boolean running = true;
        String command;
        String param;

        welcomeMessage();

        while (running) {
            showMenu();
            String input = scan.nextLine();

            String[] splitInput = input.split(" ", 2);
            command = splitInput[0];
            param = splitInput.length > 1 ? splitInput[1] : "";

            if (command.equals("q")) {
                running = false;
            } else {
                try {
                    runCommand(command, param);
                } catch (IOException e) {
                    System.out.println("Error. Please try again.");
                }
            }
        }
    }

    // EFFECTS: Display an initial message onto your screen
    private void welcomeMessage() {
        System.out.println("\nWelcome to the 20 Questions Builder!");
        System.out.println("With this program, you can create your own game of 20 Questions");
        System.out.println("To start, set your yes/no question, create some items and sort them into yes and no");
    }

    // MODIFIES: this
    // EFFECTS: run appropriate method for given input. Otherwise print "Unknown Command"
    private void runCommand(String input, String param) throws IOException {
        switch (input) {
            case "help":
                showCommands();
                break;
            case "item":
                itemOptions(param);
                break;
            case "sort":
                sortItems();
                break;
            case "move":
                move();
                break;
            case "setQuestion":
                currentNode.setQuestion(param);
                break;
            case "file":
                saveLoad(param);
                break;
            default:
                System.out.println("Unknown Command");
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a new item with given name and add it to the current node's unsorted items
    private void newItem(String name) {
        Item newItem = new Item(name);
        currentNode.addUnsortedItem(newItem);
    }

    // EFFECTS: provides functionality for creating items and setting descriptions.
    private void itemOptions(String param) {

        String[] splitParam = param.split(" ", 2);
        String command = splitParam[0];
        if (command.equals("new") && splitParam.length != 2) {
            System.out.println("Error: Please enter item name");
            return;
        }
        String commandParam = splitParam.length > 1 ? splitParam[1] : "";

        switch (command) {
            case "new":
                newItem(commandParam);
                break;
            case "describe":
                setDescriptions();
                break;
            default:
                System.out.println("Unknown Command");
        }
    }


    // EFFECTS: provides functionality for saving or loading data.
    private void saveLoad(String param) throws IOException {

        String[] paramArray = param.split(" ", 2);
        if (paramArray.length != 2) {
            System.out.println("Please include file name");
            return;
        }
        String command = paramArray[0];
        String path = paramArray[1];

        switch (command) {
            case "save":
                saveFile(path);
                break;
            case "load":
                readFile(path);
                break;
            default:
                System.out.println("Unknown Command");
        }
    }

    // MODIFIES: this
    // EFFECTS: warns about overwriting data. If user is okay with that, load data from path and overwrite
    // current and root node
    private void readFile(String partialPath) throws IOException {
        System.out.println("WARNING: This will overwrite your current work. Continue?");

        String answer = scan.nextLine();

        if (answer.equals("yes") || answer.equals("y")) {
            JsonReader reader = new JsonReader(partialPath);
            root = reader.read();
            currentNode = root;
        } else {
            System.out.println("File loading cancelled");
        }

    }

    // EFFECTS: if path is a test file, throw exception. Else save file to given path.
    private void saveFile(String path) throws IOException {
        if (containsTestFiles(path)) {
            throw new IOException();
        }
        JsonWriter wr = new JsonWriter(path);
        wr.open();
        wr.write(root);
        wr.close();
        System.out.println("Saved file as " + path);
    }

    // EFFECTS: returns true if given path is equal to one of the test files.
    private boolean containsTestFiles(String path) {
        return path.equalsIgnoreCase("testReaderEmptyNode")
                || path.equalsIgnoreCase("testReaderGeneralNode")
                || path.equalsIgnoreCase("testWriterEmptyNode")
                || path.equalsIgnoreCase("testWriterGeneralNode");
    }

    // MODIFIES: this
    // EFFECTS: prompt user for input and then change node to their choice
    private void move() {
        System.out.println("Which node would you like to move to? (yes, no, parent, root)");

        String answer = scan.nextLine();

        switch (answer) {
            case "root":
                currentNode = root;
                break;
            case "parent":
                moveToParentNode();
                break;
            case "yes":
                moveToChildNode(true);
                break;
            case "no":
                moveToChildNode(false);
                break;
            default:
                System.out.println("Unknown Command");
        }

    }

    // MODIFIES: this
    // EFFECTS: move to parent node if it exists
    private void moveToParentNode() {
        Node parentNode = currentNode.getParentNode();
        if (parentNode != null) {
            currentNode = parentNode;
        }
    }

    // MODIFIES: this
    // EFFECTS: change current node to yesNode or noNode
    private void moveToChildNode(boolean isYesNode) {
        Node node = isYesNode ? currentNode.getYesNode() : currentNode.getNoNode();
        if (node != null) {
            currentNode = node;
        }
    }

    // MODIFIES: this
    // EFFECTS: loops through all items in the unsorted list and prompts user to set descriptions
    private void setDescriptions() {
        System.out.println("\nSetting Descriptions (leave empty to skip)");

        List<Item> list = currentNode.getUnsortedItems();

        for (Item item : list) {
            System.out.println("Item: " + item.getName());

            String desc = item.getDescription();

            if (! desc.equals(Item.NO_DESCRIPTION)) {
                System.out.println("Current description: " + desc);
            }

            System.out.println("New Description: ");
            String newDesc = scan.nextLine();
            item.setDescription(newDesc);
        }
    }

    // MODIFIES: this
    // EFFECTS: loops through all items in the list and prompts user to sort
    private void sortItems() {
        while (currentNode.getUnsortedItems().size() > 0) {

            Item itemToSort = currentNode.getUnsortedItems().get(0);

            System.out.println("Question: " + currentNode.getQuestion());
            System.out.println("Item: " + itemToSort.getName());
            System.out.print("Answer: ");

            String answer = scan.nextLine();
            if (answer.equals("yes") || answer.equals("y")) {
                currentNode.moveItemYes();
            } else if (answer.equals("no") || answer.equals("n")) {
                currentNode.moveItemNo();
            } else {
                System.out.println("Unknown Command");
            }
        }
    }

    // EFFECTS: shows the menu on the screen
    private void showMenu() {
        System.out.println("Type help for command list");
        System.out.println("Current Question: " + currentNode.getQuestion());

        System.out.println("\nEnter Your Command:");
    }

    // EFFECTS: prints a list of commands
    private void showCommands() {
        System.out.println("Commands:");
        System.out.println("q - quit");
        System.out.println("help - command list");
        System.out.println("setQuestion [question] - change current question");
        System.out.println("item [describe / new [name]] - describe and create items");
        System.out.println("sort - move items by yes or no");
        System.out.println("move - navigate through the question tree");
        System.out.println("file [save/load] [filename]\n");
    }
}
