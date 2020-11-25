package ui;

import model.Item;
import model.Node;
import persistence.JsonReader;

import javax.swing.*;
import java.io.IOException;
import java.util.LinkedList;


// GameGUI handles all the GUI for playing the game
public class GameGUI extends GUI {

    private Node currentNode;
    private LinkedList<Item> guessList;
    private LinkedList<Node> nextGuessNodes;

    private JLabel questionLabel;
    private JLabel fileLabel;
    private JTextField fileField;
    private JButton fileLoadButton;
    private JLabel answerLabel;
    private JButton questionYesButton;
    private JButton questionNoButton;
    private JButton guessYesButton;
    private JButton guessNoButton;
    private JLabel winningItemDescription;

    // EFFECTS: initialize fields and graphics
    GameGUI() {
        initializeFields();
        initializeGraphics();
    }

    // EFFECTS: creates forms, initialize components, set constraints
    @Override
    protected void createForms() {
        createFileForm();
        createQuestionForm();
        createGuessForm();
        initializeComponents();
        setConstraints();
        updateGUI();
    }

    // MODIFIES: this
    // EFFECTS: sets up the guess form
    private void createGuessForm() {
        guessYesButton = new JButton("Yes");
        guessNoButton = new JButton("No");

        guessYesButton.setVisible(false);
        guessNoButton.setVisible(false);

        addGuessButtonAction();
    }

    // MODIFIES: this
    // EFFECTS: sets the corresponding button action
    private void addGuessButtonAction() {
        guessYesButton.addActionListener(e -> {
            guessButtonAction(true);
            playSound();
        });
        guessNoButton.addActionListener(e -> {
            guessButtonAction(false);
            playSound();
        });
    }

    // EFFECTS: if correct guess is true, win game and return. Else if there are no guesses left, go to the next guess
    // node, if it exists. If it does not, lose the game. But if there are guesses left, then guess with the next item.
    private void guessButtonAction(boolean correctGuess) {
        // win game!
        if (correctGuess) {
            endGame(true);
            return;
        }

        // if there are no guesses left
        if (guessList.size() <= 1) {
            if (nextGuessNodes.size() > 0) {
                jumpNode(nextGuessNodes.poll());
            } else {
                // if we have exhausted all options, we fail!
                endGame(false);
            }
            return;
        }

        guessList.removeFirst();
        guess(guessList);
    }

    // MODIFIES: this
    // EFFECTS: shows appropriate win / lose screen
    private void endGame(boolean win) {
        clearScreen();
        if (win) {
            answerLabel.setText("Yes! I win!!!");
            winningItemDescription.setText(guessList.get(0) + ": " + guessList.get(0).getDescription());
        } else {
            questionLabel.setText("");
            answerLabel.setText("Oof, you stumped me! Play Again!");
        }
    }

    // EFFECTS: sets visibility to false for both question and guess buttons
    private void clearScreen() {
        setQuestionVisibility(false);
        setGuessVisibility(false);
    }

    // MODIFIES: this
    // EFFECTS: sets up question form
    private void createQuestionForm() {
        questionLabel = new JLabel();
        answerLabel = new JLabel("Answer:");
        questionYesButton = new JButton("Yes");
        questionNoButton = new JButton("No");
        winningItemDescription = new JLabel("");

        questionYesButton.setVisible(false);
        questionNoButton.setVisible(false);

        addAnswerButtonAction();
    }

    // MODIFIES: this
    // EFFECTS: add corresponding answer functions to buttons
    private void addAnswerButtonAction() {
        questionYesButton.addActionListener(e -> {
            answerYes();
            playSound();
        });
        questionNoButton.addActionListener(e -> {
            answerNo();
            playSound();
        });
    }

    // if yesNode exists, move to it. Else guess with the yes items
    private void answerYes() {
        if (currentNode.getYesNode() != null) {
            changeNode(true);
        } else {
            guess(currentNode.getYesItems());
        }
    }

    // if noNode exists, move to it. Else guess with the no items
    private void answerNo() {
        if (currentNode.getNoNode() != null) {
            changeNode(false);
        } else {
            guess(currentNode.getNoItems());
        }
    }

    // MODIFIES: this
    // EFFECTS: called when nextGuessNode is used for backtracking purposes. Fixes button visibility, sets the node to
    // given node, updates GUI, and check if node is at the end of a branch (for guessing purposes)
    private void jumpNode(Node node) {
        setQuestionVisibility(true);
        setGuessVisibility(false);
        currentNode = node;
        updateGUI();
        guessItems();
    }

    // MODIFIES: this
    // EFFECTS: if visibility, sets guess buttons to visible. else sets to invisible
    private void setGuessVisibility(boolean visibility) {
        guessYesButton.setVisible(visibility);
        guessNoButton.setVisible(visibility);
    }

    // MODIFIES: this
    // EFFECTS: if isYesNode, change to yesNode and add noNode to the front of nextGuessNodes. Else change to noNode and
    // add yesNode to front of nextGuessNodes. Then update GUI and check if at the end of branch (for guessing reasons)
    private void changeNode(boolean isYesNode) {
        Node yesNode = currentNode.getYesNode();
        Node noNode = currentNode.getNoNode();

        if (isYesNode) {
            currentNode = yesNode;
            nextGuessNodes.addFirst(noNode);
        } else {
            currentNode = noNode;
            nextGuessNodes.addFirst(yesNode);
        }
        updateGUI();
        guessItems();
    }

    // EFFECTS: if guessItems is the final node in a branch, add together all the items and call guess()
    private void guessItems() {
        if (currentNode.getYesNode() == null) {
            // Credit: https://stackoverflow.com/questions/189559/how-do-i-join-two-lists-in-java/34090554
            LinkedList<Item> guessItems = new LinkedList<>(currentNode.getUnsortedItems());
            guessItems.addAll(currentNode.getYesItems());
            guessItems.addAll(currentNode.getNoItems());
            guess(guessItems);
        }
    }

    // MODIFIES: this
    // EFFECTS: sets guessList to guesses, then displays the guess, shows guess buttons, and hides question form
    private void guess(LinkedList<Item> guesses) {
        guessList = guesses;
        questionLabel.setText("I got it! It's " + guessList.get(0));
        setQuestionVisibility(false);
        setGuessVisibility(true);
    }

    // MODIFIES: this
    // EFFECTS: if visibility is true, make question buttons visible. otherwise make invisible
    private void setQuestionVisibility(boolean visibility) {
        questionYesButton.setVisible(visibility);
        questionNoButton.setVisible(visibility);
    }

    // EFFECTS: set all constraints for each form
    @Override
    protected void setConstraints() {
        setFileConstraint();
        setQuestionConstraints();
        setGuessConstraints();
    }

    // EFFECTS: set constraints for guess form
    private void setGuessConstraints() {
        setConstraint(guessYesButton, INDENT, frame, SPACING_Y, answerLabel);
        setConstraint(guessNoButton, SPACING_X, guessYesButton, SPACING_Y, answerLabel);
    }

    // EFFECTS: set constraints for question form
    private void setQuestionConstraints() {
        setConstraint(questionLabel, INDENT, frame, SPACING_Y, fileLabel);
        setConstraint(winningItemDescription, INDENT, frame, SPACING_Y, questionLabel);
        setConstraint(answerLabel, INDENT, frame, SPACING_Y * 2, questionLabel);
        setConstraint(questionYesButton, INDENT, frame, SPACING_Y, answerLabel);
        setConstraint(questionNoButton, SPACING_X, questionYesButton, SPACING_Y, answerLabel);
    }

    // EFFECTS: set constraints for file form
    private void setFileConstraint() {
        setConstraint(fileLabel, INDENT, frame, INDENT, frame);
        setConstraint(fileField, SPACING_X, fileLabel, INDENT, frame);
        setConstraint(fileLoadButton, SPACING_X, fileField, INDENT, frame);
    }

    // EFFECTS: initializes all components
    @Override
    protected void initializeComponents() {
        initializeComponent(fileLabel);
        initializeComponent(fileField);
        initializeComponent(fileLoadButton);
        initializeComponent(questionLabel);
        initializeComponent(answerLabel);
        initializeComponent(questionYesButton);
        initializeComponent(questionNoButton);
        initializeComponent(guessYesButton);
        initializeComponent(guessNoButton);
        initializeComponent(winningItemDescription);
    }

    // MODIFIES: this
    // EFFECTS: initializes the file form elements
    private void createFileForm() {
        fileLabel = new JLabel("File:");
        fileField = new JTextField(FIELD_WIDTH);
        fileLoadButton = new JButton("Load");

        addFileLoadButtonAction();
    }

    // MODIFIES: this
    // EFFECTS: creates action listener that loads a file from the filesystem and shows warning messages when needed.
    private void addFileLoadButtonAction() {
        fileLoadButton.addActionListener(e -> {
            playSound();
            String path = fileField.getText();
            try {
                JLabel warningMessage = new JLabel("This will overwrite your current work. Continue?");
                warningMessage.setFont(FONT);
                int warningResult = JOptionPane.showConfirmDialog(frame,
                        warningMessage, "WARNING", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, scaledIcon);
                if (warningResult == 0) {
                    readFile(path);
                }
            } catch (IOException ex) {
                showErrorMessage("An error has occurred.", "File Load Error", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    // EFFECTS: read a file from the filesystem and update the current and root nodes to it.
    private void readFile(String partialPath) throws IOException {
        JsonReader reader = new JsonReader(partialPath);
        currentNode = reader.read();
        resetGame();
    }

    // MODIFIES: this
    // EFFECTS: sets all fields back to their initial settings so that a game can be started as new
    private void resetGame() {
        setQuestionVisibility(true);
        answerLabel.setText("Answer:");
        winningItemDescription.setText("");
        guessList.clear();
        nextGuessNodes.clear();
        updateGUI();
    }

    // MODIFIES: this
    // EFFECTS: update all elements of the GUI
    @Override
    protected void updateGUI() {
        questionLabel.setText(currentNode.getQuestion());
    }

    // MODIFIES: this
    // EFFECTS: initializes fields to their default values
    @Override
    protected void initializeFields() {
        frame = new JFrame("20 Questions Game");
        currentNode = new Node(new LinkedList<>(), null);
        guessList = new LinkedList<>();
        nextGuessNodes = new LinkedList<>();
        layout = new SpringLayout();
    }
}
