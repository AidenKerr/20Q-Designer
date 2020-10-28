package ui;

import exceptions.IllegalPathException;
import exceptions.IncorrectParameterException;
import model.Item;
import model.Node;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

// User interface code
// Inspired by code in the TellerApp example provided
public class QuestionsApp {

    Node root;
    Node currentNode;

    private Scanner scan;

    public QuestionsApp() {
        LinkedList<Item> defaultList = new LinkedList<>();
        root = new Node(defaultList, null);
        currentNode = root;
        scan = new Scanner(System.in);

        runApp();
    }

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
                } catch (IncorrectParameterException e) {
                    System.out.println("Incorrect Parameters");
                } catch (IOException e) {
                    System.out.println("Invalid file name");
                }
            }
        }
    }

    private void welcomeMessage() {
        System.out.println("\nWelcome to the 20 Questions Builder!");
        System.out.println("With this program, you can create your own game of 20 Questions");
        System.out.println("To start, set your yes/no question, create some items and sort them into yes and no");
    }

    private void runCommand(String input, String param)
            throws IncorrectParameterException, IllegalPathException, IOException {
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

    private void newItem(String name) {
        Item newItem = new Item(name);
        currentNode.addUnsortedItem(newItem);
    }

    private void itemOptions(String param) throws IncorrectParameterException {

        String[] splitParam = param.split(" ", 2);
        String command = splitParam[0];
        if (command.equals("new") && splitParam.length != 2) {
            throw new IncorrectParameterException();
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


    private void saveLoad(String param)
            throws IncorrectParameterException, IllegalPathException, IOException {

        String[] paramArray = param.split(" ", 2);
        if (paramArray.length != 2) {
            throw new IncorrectParameterException();
        }
        String command = paramArray[0];
        String path = paramArray[1];

        if (path.contains("./")) {
            throw new IllegalPathException();
        }

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

    private void readFile(String path) throws IOException {
        System.out.println("WARNING: This will overwrite your current work. Continue?");

        String answer = scan.nextLine();

        if (answer.equals("yes") || answer.equals("y")) {
            String finalPath = "./data/" + path + ".json";
            JsonReader reader = new JsonReader(finalPath);
            root = reader.read();
            currentNode = root;
        } else {
            System.out.println("File loading cancelled");
        }

    }

    private void saveFile(String path) throws FileNotFoundException, IllegalPathException {
        if (containsTestFiles(path)) {
            throw new IllegalPathException();
        }
        String finalPath = "./data/" + path + ".json";
        JsonWriter wr = new JsonWriter(finalPath);
        wr.open();
        wr.write(root);
        wr.close();
        System.out.println("Saved file in " + finalPath);
    }

    private boolean containsTestFiles(String path) {
        return path.equals("testReaderEmptyNode")
                || path.equals("testReaderGeneralNode")
                || path.equals("testWriterEmptyNode")
                || path.equals("testWriterGeneralNode");
    }

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

    // move to parent node as long as
    private void moveToParentNode() {
        Node parentNode = currentNode.getParentNode();
        if (parentNode != null) {
            currentNode = parentNode;
        }
    }

    // change current node to yesNode or noNode
    private void moveToChildNode(boolean isYesNode) {
        Node node = isYesNode ? currentNode.getYesNode() : currentNode.getNoNode();
        if (node != null) {
            currentNode = node;
        }
    }

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

    private void showMenu() {
        System.out.println("Type help for command list");
        System.out.println("Current Question: " + currentNode.getQuestion());

        System.out.println("\nEnter Your Command:");
    }

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
