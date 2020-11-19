package ui;

import model.Item;
import model.Node;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.io.IOException;
import java.util.LinkedList;

// references:
// SimpleDrawingPlayer example program
// and
// https://www.geeksforgeeks.org/java-swing-simple-user-registration-form/
public class EditorGUI extends GUI {
    DefaultListModel<Item> itemsListModel;
    private Node root;
    private Node currentNode;
    private JLabel questionLabel;
    private JTextField questionField;
    private JButton questionButton;
    private JLabel itemsLabel;
    private JList<Item> itemsList;
    private JLabel newItemLabel;
    private JTextField newItemField;
    private JButton newItemButton;
    private JLabel sortLabel;
    private JButton yesButton;
    private JButton noButton;
    private JLabel moveLabel;
    private JButton moveYes;
    private JButton moveNo;
    private JButton moveParent;
    private JButton moveRoot;
    private JLabel descriptionLabel;
    private JTextField descriptionField;
    private JButton descriptionButton;
    private JLabel fileLabel;
    private JTextField fileField;
    private JButton fileSaveButton;
    private JButton fileLoadButton;

    // MODIFIES: this
    // EFFECTS: calls super to set frame title. Then initializes fields and graphics
    EditorGUI() {
        initializeFields();
        initializeGraphics();
    }

    // EFFECTS: run functions to set up the forms for the game editor
    @Override
    protected void createForms() {
        createQuestionForm();
        createItemForm();
        createMoveButtons();
        createFileForm();
        initializeComponents();
        setConstraints();
        updateGUI();
    }

    // EFFECTS: initializes all components
    @Override
    protected void initializeComponents() {
        initializeComponent(fileLabel);
        initializeComponent(fileField);
        initializeComponent(fileSaveButton);
        initializeComponent(fileLoadButton);

        initializeComponent(newItemLabel);
        initializeComponent(newItemField);
        initializeComponent(newItemButton);
        initializeComponent(itemsLabel);
        initializeComponent(itemsList);
        initializeComponent(sortLabel);
        initializeComponent(yesButton);
        initializeComponent(noButton);
        initializeComponent(descriptionLabel);
        initializeComponent(descriptionField);
        initializeComponent(descriptionButton);

        initializeComponent(questionLabel);
        initializeComponent(questionField);
        initializeComponent(questionButton);

        initializeComponent(moveLabel);
        initializeComponent(moveYes);
        initializeComponent(moveNo);
        initializeComponent(moveParent);
        initializeComponent(moveRoot);

    }

    // MODIFIES: this
    // EFFECTS: initializes the file form elements
    private void createFileForm() {
        fileLabel = new JLabel("File:");
        fileField = new JTextField(FIELD_WIDTH);
        fileSaveButton = new JButton("Save");
        fileLoadButton = new JButton("Load");

        addFileSaveButtonAction();
        addFileLoadButtonAction();
    }

    // MODIFIES: this
    // EFFECTS: creates action listener that loads a file from the filesystem and shows marning messages when needed.
    private void addFileLoadButtonAction() {
        fileLoadButton.addActionListener(e -> {
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
        root = reader.read();
        currentNode = root;
        updateGUI();
    }

    // MODIFIES: this
    // EFFECTS: creates action listener to save a file and show warning messages when appropriate
    private void addFileSaveButtonAction() {
        fileSaveButton.addActionListener(e -> {
            String path = fileField.getText();
            if (containsTestFiles(path)) {
                showErrorMessage("Please choose another file name.", "File Save Error", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    saveFile(path);
                    showErrorMessage("File saved successfully!", "File Saved!", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    showErrorMessage("An error has occurred.", "File Save Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    // EFFECTS: saves the current question tree as a JSON file
    private void saveFile(String path) throws IOException {
        JsonWriter wr = new JsonWriter(path);
        wr.open();
        wr.write(root);
        wr.close();
    }

    // EFFECTS: returns true if path is one of the test files
    private boolean containsTestFiles(String path) {
        return path.equalsIgnoreCase("testReaderEmptyNode")
                || path.equalsIgnoreCase("testReaderGeneralNode")
                || path.equalsIgnoreCase("testWriterEmptyNode")
                || path.equalsIgnoreCase("testWriterGeneralNode");
    }

    // MODIFIES: this
    // EFFECTS: initializes the elements of the item form.
    private void createItemForm() {
        newItemLabel = new JLabel("New Item:");
        newItemField = new JTextField(FIELD_WIDTH);
        newItemButton = new JButton("Add");
        itemsLabel = new JLabel("Items:");
        itemsList = new JList<>(itemsListModel);
        sortLabel = new JLabel("Sort:");
        yesButton = new JButton("Yes");
        noButton = new JButton("No");
        descriptionLabel = new JLabel("Description:");
        descriptionField = new JTextField(FIELD_WIDTH);
        descriptionButton = new JButton("Set");

        itemsList.setSelectionModel(new DisabledItemSelectionModel());

        addItemButtonAction();
        sortItemButtonAction();
        setDescriptionButtonAction();
    }

    // MODIFIES: this
    // EFFECTS: creates action listener that sets the first item's description to the description field text.
    private void setDescriptionButtonAction() {
        descriptionButton.addActionListener(e -> {
            LinkedList<Item> items = currentNode.getUnsortedItems();
            if (items.size() > 0) {
                Item item = items.get(0);
                String newDescription = descriptionField.getText();
                item.setDescription(newDescription);
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: add action listeners for yes and no button than moves the item respectively. Both also update the GUI
    private void sortItemButtonAction() {
        yesButton.addActionListener(e -> {
            currentNode.moveItemYes();
            updateGUI();
            playSound();
        });
        noButton.addActionListener(e -> {
            currentNode.moveItemNo();
            updateGUI();
            playSound();
        });
    }

    // MODIFIES: this
    // EFFECTS: set functionality of the add item button. Creates a new items with item name given by item field text
    // then adds the item, resets the item text field, and resets the selected index.
    private void addItemButtonAction() {
        newItemButton.addActionListener(e -> {
            String name = newItemField.getText();
            Item newItem = new Item(name);
            addItem(newItem);
            newItemField.setText("");
            itemsList.setSelectedIndex(0);
        });
    }

    // MODIFIES: this
    // EFFECTS: adds an item to the unsorted items of the current node. Also adds the item to the items list model.
    private void addItem(Item newItem) {
        currentNode.addUnsortedItem(newItem);
        itemsListModel.addElement(newItem);
    }

    // MODIFIES: this
    // EFFECTS: initializes the question forms elements
    private void createQuestionForm() {
        questionLabel = new JLabel("Question:");
        questionField = new JTextField(currentNode.getQuestion(), FIELD_WIDTH);
        questionButton = new JButton("Update");

        addQuestionButtonAction();
    }

    // MODIFIES: this
    // EFFECTS: sets the functionality of the question button. When pushed, the question is set to the text in the field
    private void addQuestionButtonAction() {
        questionButton.addActionListener(e -> {
            String newText = questionField.getText();
            currentNode.setQuestion(newText);
        });
    }

    // MODIFIES: this
    // EFFECTS: initializes the move form elements
    private void createMoveButtons() {
        moveLabel = new JLabel("Move:");
        moveYes = new JButton("Yes");
        moveNo = new JButton("No");
        moveParent = new JButton("Parent");
        moveRoot = new JButton("Root");

        addMoveButtonAction();
    }

    // MODIFIES: this
    // EFFECTS: sets the functionality of the move buttons
    private void addMoveButtonAction() {
        moveYes.addActionListener(e -> moveToChildNode(true));
        moveNo.addActionListener(e -> moveToChildNode(false));
        moveParent.addActionListener(e -> moveToParentNode());
        moveRoot.addActionListener(e -> moveToRoot());
    }

    // MODIFIES: this
    // EFFECTS: sets current node to the root node.
    private void moveToRoot() {
        currentNode = root;
        updateGUI();
    }

    // MODIFIES: this
    // EFFECTS: sets current node to parent node, if it exists.
    private void moveToParentNode() {
        Node parentNode = currentNode.getParentNode();
        if (parentNode != null) {
            currentNode = parentNode;
        }
        updateGUI();
    }

    // MODIFIES: this
    // EFFECTS: if isYes node is true, change current node to the "yes" child node. Else change to "no" child node
    private void moveToChildNode(boolean isYesNode) {
        Node node = isYesNode ? currentNode.getYesNode() : currentNode.getNoNode();
        if (node != null) {
            currentNode = node;
        }
        updateGUI();
    }

    // MODIFIES: this
    // EFFECTS: update all elements of the GUI
    @Override
    protected void updateGUI() {
        updateQuestion();
        updateList();
        updateDescription();
    }

    // MODIFIES: this
    // EFFECTS: sets the description text to the description of the first item in the current node's unsorted items.
    private void updateDescription() {
        LinkedList<Item> itemsList = currentNode.getUnsortedItems();
        if (itemsList.size() > 0) {
            Item item = itemsList.get(0);
            descriptionField.setText(item.getDescription());
        } else {
            descriptionField.setText("");
        }
    }

    // MODIFIES: this
    // EFFECTS: sets the text of the question field to the question of the current node
    private void updateQuestion() {
        questionField.setText(currentNode.getQuestion());
    }

    // MODIFIES: this
    // EFFECTS: sets constraints for each form section
    @Override
    protected void setConstraints() {
        setQuestionFormConstraint();
        setItemFormConstraint();
        setMoveConstraint();
        setFileConstraint();
    }

    // MODIFIES: this
    // EFFECTS: sets constraints for the file form elements
    private void setFileConstraint() {
        setConstraint(fileLabel, INDENT, frame, SPACING_Y, moveLabel);
        setConstraint(fileField, SPACING_X, descriptionLabel, SPACING_Y, moveLabel);
        setConstraint(fileSaveButton, SPACING_X, fileField, SPACING_Y, moveLabel);
        setConstraint(fileLoadButton, SPACING_X, fileSaveButton, SPACING_Y, moveLabel);
    }

    // MODIFIES: this
    // EFFECTS: sets constraints for the moving form elements
    private void setMoveConstraint() {
        setConstraint(moveLabel, INDENT, frame, SPACING_Y, descriptionField);
        setConstraint(moveYes, SPACING_X, descriptionLabel, SPACING_Y, descriptionField);
        setConstraint(moveNo, SPACING_X, moveYes, SPACING_Y, descriptionField);
        setConstraint(moveParent, SPACING_X, moveNo, SPACING_Y, descriptionField);
        setConstraint(moveRoot, SPACING_X, moveParent, SPACING_Y, descriptionField);
    }

    // MODIFIES: this
    // EFFECTS: sets constraints for the item form elements
    private void setItemFormConstraint() {
        setConstraint(newItemLabel, INDENT, frame, SPACING_Y, questionLabel);
        setConstraint(newItemField, SPACING_X, descriptionLabel, 0, newItemLabel);
        setConstraint(newItemButton, SPACING_X, questionField, 0, newItemField);
        setConstraint(itemsLabel, INDENT, frame, SPACING_Y, newItemLabel);
        setConstraint(itemsList, SPACING_X, descriptionLabel, 0, itemsLabel);
        layout.putConstraint(SpringLayout.EAST, itemsList, 0, SpringLayout.EAST, newItemField);
        setConstraint(sortLabel, SPACING_X, itemsList, 0, itemsList);
        setConstraint(yesButton, SPACING_X, itemsList, SPACING_Y, sortLabel);
        setConstraint(noButton, SPACING_X, itemsList, SPACING_Y, yesButton);
        layout.putConstraint(SpringLayout.SOUTH, itemsList, 0, SpringLayout.SOUTH, noButton);
        setConstraint(descriptionLabel, INDENT, frame, SPACING_Y, noButton);
        setConstraint(descriptionField, SPACING_X, descriptionLabel, 0, descriptionLabel);
        setConstraint(descriptionButton, SPACING_X, descriptionField, 0, descriptionField);
    }

    // MODIFIES: this
    // EFFECTS: sets constraints for the question form elements
    private void setQuestionFormConstraint() {
        setConstraint(questionLabel, INDENT, frame, INDENT, frame);
        setConstraint(questionField, SPACING_X, descriptionLabel, INDENT, frame);
        setConstraint(questionButton, SPACING_X, questionField, INDENT, frame);
    }

    // MODIFIES: this
    // EFFECTS: initializes fields to their default values
    @Override
    protected void initializeFields() {
        frame = new JFrame("20 Questions Game Designer");
        root = new Node(new LinkedList<>(), null);
        currentNode = root;
        layout = new SpringLayout();
        itemsListModel = new DefaultListModel<>();
    }

    // MODIFIES: this
    // EFFECTS: updates the items list model to show all unsorted items in the current node
    private void updateList() {
        itemsListModel.clear();
        for (Item item : currentNode.getUnsortedItems()) {
            itemsListModel.addElement(item);
        }
        itemsList.setSelectedIndex(0);
    }
}
