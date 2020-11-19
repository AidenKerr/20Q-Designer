package ui;

import javax.swing.*;

// SelectionGUI is the "main menu" where you can select play or edit game.
public class SelectionGUI extends GUI {

    private JLabel message;
    private JButton playGame;
    private JButton editGame;

    // EFFECTS: initialize fields and graphics
    public SelectionGUI() {
        initializeFields();
        initializeGraphics();
    }

    // EFFECTS: creates form, initialize components, set constraints
    @Override
    protected void createForms() {
        createForm();
        initializeComponents();
        setConstraints();
    }

    // MODIFIES: this
    // EFFECTS: creates form with label and two buttons for playing and editing game
    private void createForm() {
        message = new JLabel("Welcome to the 20 questions guessing game! Choose an option:");
        playGame = new JButton("Play Game");
        editGame = new JButton("Edit Game");

        setButtonAction();
    }

    // MODIFIES: this
    // EFFECTS: set the appropriate action for the button pushes. playGame will open game player. editGame will open
    // game editor
    private void setButtonAction() {
        playGame.addActionListener(e -> {
            playSound();
            new GameGUI();
            frame.dispose();
        });
        editGame.addActionListener(e -> {
            playSound();
            new EditorGUI();
            frame.dispose();
        });
    }

    // EFFECTS: initializes all components
    @Override
    protected void initializeComponents() {
        initializeComponent(message);
        initializeComponent(playGame);
        initializeComponent(editGame);
    }

    // EFFECTS: as there is nothing to update, this method is empty
    @Override
    protected void updateGUI() {
        // nothing to update
    }

    // MODIFIES: this
    // EFFECTS: sets constraints for each form element
    @Override
    protected void setConstraints() {
        setConstraint(message, INDENT, frame, INDENT, frame);
        setConstraint(playGame, INDENT, frame, SPACING_Y, message);
        setConstraint(editGame, SPACING_X, playGame, SPACING_Y, message);
    }

    // MODIFIES: this
    // EFFECTS: initializes fields to their default values
    @Override
    protected void initializeFields() {
        frame = new JFrame("20 Questions Game - Select Option");
        layout = new SpringLayout();
    }
}
