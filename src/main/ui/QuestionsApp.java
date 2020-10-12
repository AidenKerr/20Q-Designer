package ui;

import model.Item;
import model.Node;

import java.util.LinkedList;
import java.util.Scanner;

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

        while (running) {
            showMenu();
            String input = scan.nextLine();

            String[] splitInput = input.split(" ", 2);
            command = splitInput[0];
            param = splitInput.length > 1 ? splitInput[1] : "";

            if (command.equals("q")) {
                running = false;
            } else {
                runCommand(command, param);
            }
        }
    }

    private void runCommand(String input, String param) {
        switch (input) {
            case "help":
                showCommands();
                break;
            case "newItem":
                Item newItem = new Item(param);
                currentNode.addUnsortedItem(newItem);
                break;
            case "yes": // move item into yesList
                currentNode.moveItemYes();
                break;
            case "no": // move item into noList
                currentNode.moveItemNo();
                break;
            case "yesNode": // change current node to yesNode
                changeNode(true);
                break;
            case "noNode": // change current node to noNode
                changeNode(false);
                break;
            case "setQuestion":
                currentNode.setQuestion(param);
                break;
            default:
                System.out.println("Unknown Command");
                break;
        }
    }

    // change current node to yesNode or noNode
    private void changeNode(boolean isYesNode) {
        Node node = isYesNode ? currentNode.getYesNode() : currentNode.getYesNode();
        if (node != null) {
            currentNode = node;
        }
    }

    private void showMenu() {
        System.out.println("Type help for command list");
        System.out.println("Current Question: " + currentNode.getQuestion());

        System.out.println("Items: ");
        for (Item item : currentNode.getUnsortedItems()) {
            System.out.println(item.getName());
        }

        System.out.println("\nEnter Your Command:");
    }

    private void showCommands() {
        System.out.println("Commands:");
        System.out.println("q - quit");
        System.out.println("setQuestion [question] - change current question");
        System.out.println("yes - move item to yes list");
        System.out.println("no - move item to yes list");
        System.out.println("yesNode - move to yes node");
        System.out.println("noNode - move to yes node\n");
    }
}
