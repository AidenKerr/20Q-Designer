package ui;

import model.Item;
import model.Node;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;

// references:
// SimpleDrawingPlayer example program
// and
// https://www.geeksforgeeks.org/java-swing-simple-user-registration-form/
public class GUI extends JFrame {
    private static final int WIDTH = 2000;
    private static final int HEIGHT = 1200;
    private static final Font FONT = new Font(null, Font.PLAIN, 50);
    private static final int INDENT = 100;
    private static final int SPACING_X = 10;
    private static final int SPACING_Y = 90;
    private static final int FIELD_WIDTH = 15;
    DefaultListModel<Item> itemsListModel;
    private Node root;
    private Node currentNode;
    private SpringLayout layout = new SpringLayout();
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

    GUI() {
        super("20 Questions Game Designer");
        initializeFields();
        initializeGraphics();
    }

    private void initializeGraphics() {
        Font font = new Font(null, Font.PLAIN, 500);
        setFont(font);
        setLayout(layout);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        createForm();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void createForm() {
        createQuestionForm();
        createItemForm();
        createMoveButtons();
        createFileForm();
        initializeComponents();
        setConstraints();
        updateGUI();
    }

    private void initializeComponents() {
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

    private void createFileForm() {
        fileLabel = new JLabel("File:");
        fileField = new JTextField(FIELD_WIDTH);
        fileSaveButton = new JButton("Save");
        fileLoadButton = new JButton("Load");

        addFileSaveButtonAction();
        addFileLoadButtonAction();
    }

    private void addFileLoadButtonAction() {
        fileLoadButton.addActionListener(e -> {
            String path = fileField.getText();
            try {
                JLabel warningMessage = new JLabel("This will overwrite your current work. Continue?");
                warningMessage.setFont(FONT);
                int warningResult = JOptionPane.showConfirmDialog(this,
                        warningMessage, "WARNING", JOptionPane.YES_NO_OPTION);
                if (warningResult == 0) {
                    readFile(path);
                }
            } catch (IOException ex) {
                showErrorMessage("An error has occurred.", "File Load Error", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void showErrorMessage(String s, String s2, int warningMessage) {
        JLabel msg = new JLabel(s);
        msg.setFont(FONT);
        JOptionPane.showMessageDialog(this,
                msg, s2, warningMessage);
    }

    private void readFile(String partialPath) throws IOException {
        JsonReader reader = new JsonReader(partialPath);
        root = reader.read();
        currentNode = root;
        updateGUI();
    }

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

    private void saveFile(String path) throws IOException {
        JsonWriter wr = new JsonWriter(path);
        wr.open();
        wr.write(root);
        wr.close();
    }

    private boolean containsTestFiles(String path) {
        return path.equalsIgnoreCase("testReaderEmptyNode")
                || path.equalsIgnoreCase("testReaderGeneralNode")
                || path.equalsIgnoreCase("testWriterEmptyNode")
                || path.equalsIgnoreCase("testWriterGeneralNode");
    }

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

    private void sortItemButtonAction() {
        yesButton.addActionListener(e -> {
            currentNode.moveItemYes();
            updateGUI();
        });
        noButton.addActionListener(e -> {
            currentNode.moveItemNo();
            updateGUI();
        });
    }

    private void addItemButtonAction() {
        newItemButton.addActionListener(e -> {
            String name = newItemField.getText();
            Item newItem = new Item(name);
            addItem(newItem);
            newItemField.setText("");
            itemsList.setSelectedIndex(0);
        });
    }

    private void addItem(Item newItem) {
        currentNode.addUnsortedItem(newItem);
        itemsListModel.addElement(newItem);
    }

    private void createQuestionForm() {
        questionLabel = new JLabel("Question:");
        questionField = new JTextField(currentNode.getQuestion(), FIELD_WIDTH);
        questionButton = new JButton("Update");

        addQuestionButtonAction();
    }

    private void addQuestionButtonAction() {
        questionButton.addActionListener(e -> {
            String newText = questionField.getText();
            currentNode.setQuestion(newText);
        });
    }

    private void createMoveButtons() {
        moveLabel = new JLabel("Move:");
        moveYes = new JButton("Yes");
        moveNo = new JButton("No");
        moveParent = new JButton("Parent");
        moveRoot = new JButton("Root");

        addMoveButtonAction();
    }

    private void addMoveButtonAction() {
        moveYes.addActionListener(e -> moveToChildNode(true));
        moveNo.addActionListener(e -> moveToChildNode(false));
        moveParent.addActionListener(e -> moveToParentNode());
        moveRoot.addActionListener(e -> moveToRoot());
    }

    private void moveToRoot() {
        currentNode = root;
        updateGUI();
    }

    private void moveToParentNode() {
        Node parentNode = currentNode.getParentNode();
        if (parentNode != null) {
            currentNode = parentNode;
        }
        updateGUI();
    }

    private void moveToChildNode(boolean isYesNode) {
        Node node = isYesNode ? currentNode.getYesNode() : currentNode.getNoNode();
        if (node != null) {
            currentNode = node;
        }
        updateGUI();
    }

    private void updateGUI() {
        updateQuestion();
        updateList();
        updateDescription();
    }

    private void updateDescription() {
        LinkedList<Item> itemsList = currentNode.getUnsortedItems();
        if (itemsList.size() > 0) {
            Item item = itemsList.get(0);
            descriptionField.setText(item.getDescription());
        } else {
            descriptionField.setText("");
        }
    }

    private void updateQuestion() {
        questionField.setText(currentNode.getQuestion());
    }

    private void setConstraints() {
        setQuestionFormConstraint();
        setItemFormConstraint();
        setMoveConstraint();
        setFileConstraint();
    }

    private void setFileConstraint() {
        setConstraint(fileLabel, INDENT, this, SPACING_Y, moveLabel);
        setConstraint(fileField, SPACING_X, descriptionLabel, SPACING_Y, moveLabel);
        setConstraint(fileSaveButton, SPACING_X, fileField, SPACING_Y, moveLabel);
        setConstraint(fileLoadButton, SPACING_X, fileSaveButton, SPACING_Y, moveLabel);
    }

    private void setMoveConstraint() {
        setConstraint(moveLabel, INDENT, this, SPACING_Y, descriptionField);
        setConstraint(moveYes, SPACING_X, descriptionLabel, SPACING_Y, descriptionField);
        setConstraint(moveNo, SPACING_X, moveYes, SPACING_Y, descriptionField);
        setConstraint(moveParent, SPACING_X, moveNo, SPACING_Y, descriptionField);
        setConstraint(moveRoot, SPACING_X, moveParent, SPACING_Y, descriptionField);
    }

    private void setItemFormConstraint() {
        setConstraint(newItemLabel, INDENT, this, SPACING_Y, questionLabel);
        setConstraint(newItemField, SPACING_X, descriptionLabel, 0, newItemLabel);
        setConstraint(newItemButton, SPACING_X, questionField, 0, newItemField);
        setConstraint(itemsLabel, INDENT, this, SPACING_Y, newItemLabel);
        setConstraint(itemsList, SPACING_X, descriptionLabel, 0, itemsLabel);
        layout.putConstraint(SpringLayout.EAST, itemsList, 0, SpringLayout.EAST, newItemField);
        setConstraint(sortLabel, SPACING_X, itemsList, 0, itemsList);
        setConstraint(yesButton, SPACING_X, itemsList, SPACING_Y, sortLabel);
        setConstraint(noButton, SPACING_X, itemsList, SPACING_Y, yesButton);
        layout.putConstraint(SpringLayout.SOUTH, itemsList, 0, SpringLayout.SOUTH, noButton);
        setConstraint(descriptionLabel, INDENT, this, SPACING_Y, noButton);
        setConstraint(descriptionField, SPACING_X, descriptionLabel, 0, descriptionLabel);
        setConstraint(descriptionButton, SPACING_X, descriptionField, 0, descriptionField);
    }

    private void setQuestionFormConstraint() {
        setConstraint(questionLabel, INDENT, this, INDENT, this);
        setConstraint(questionField, SPACING_X, descriptionLabel, INDENT, this);
        setConstraint(questionButton, SPACING_X, questionField, INDENT, this);
    }

    private void setConstraint(Component component, int x, Component left, int y, Component above) {
        layout.putConstraint(SpringLayout.WEST, component, x, SpringLayout.EAST, left);
        layout.putConstraint(SpringLayout.NORTH, component, y, SpringLayout.NORTH, above);
    }

    private void initializeComponent(Component component) {
        component.setFont(FONT);
        add(component);
    }

    private void initializeFields() {
        root = new Node(new LinkedList<>(), null);
        currentNode = root;
        itemsListModel = new DefaultListModel<>();

    }

    private void updateList() {
        itemsListModel.clear();
        for (Item item : currentNode.getUnsortedItems()) {
            itemsListModel.addElement(item);
        }
        itemsList.setSelectedIndex(0);
    }
}
