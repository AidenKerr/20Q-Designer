package ui;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.File;

public abstract class GUI {
    protected static final Font FONT = new Font(null, Font.PLAIN, 50);
    protected static final int INDENT = 100;
    protected static final int SPACING_X = 10;
    protected static final int SPACING_Y = 90;
    protected static final int FIELD_WIDTH = 15;
    private static final int WIDTH = 2000;
    private static final int HEIGHT = 1200;
    protected SpringLayout layout;
    protected Icon scaledIcon;
    JFrame frame;

    // credit: http://suavesnippets.blogspot.com/2011/06/add-sound-on-jbutton-click-in-java.html
    // EFFECTS: plays a sound
    protected void playSound() {
        try {
            File file = new File("data/move.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file.getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Sound error");
            e.printStackTrace();
        }
    }

    // MODIFIES: this
    // EFFECTS: sets up font, layout, forms, etc
    protected void initializeGraphics() {
        setupWarningBoxes();
        frame.setLayout(layout);
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        createForms();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // EFFECTS: sets warning box font and creates scaled warning icon
    private void setupWarningBoxes() {
        UIManager.put("OptionPane.buttonFont", new FontUIResource(FONT));
        ImageIcon icon = (ImageIcon) UIManager.getIcon("OptionPane.warningIcon");
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(80, 80, Image.SCALE_DEFAULT);
        scaledIcon = new ImageIcon(scaledImage);
    }

    // EFFECTS: run functions to set up the forms for the game editor
    protected abstract void createForms();

    // EFFECTS: initializes all components
    protected abstract void initializeComponents();

    // MODIFIES: this
    // EFFECTS: displays error message with given strings and message type.
    protected void showErrorMessage(String s, String s2, int messageType) {
        JLabel msg = new JLabel(s);
        msg.setFont(FONT);
        JOptionPane.showMessageDialog(frame,
                msg, s2, messageType, scaledIcon);
    }

    // MODIFIES: this
    // EFFECTS: update all elements of the GUI
    protected abstract void updateGUI();

    // MODIFIES: this
    // EFFECTS: sets constraints for each form section
    protected abstract void setConstraints();

    // MODIFIES: this, (component, above ?? TODO check this)
    // Effects: sets the constraints of the given component. x is the space between the left side of "component" and
    // the right side of "left". y is the space between the top of "component" and the top of "above"
    protected void setConstraint(Component component, int x, Component left, int y, Component above) {
        layout.putConstraint(SpringLayout.WEST, component, x, SpringLayout.EAST, left);
        layout.putConstraint(SpringLayout.NORTH, component, y, SpringLayout.NORTH, above);
    }

    // MODIFIES: this, component
    // EFFECTS: set the font of the given component and add it to this JFrame
    protected void initializeComponent(Component component) {
        component.setFont(FONT);
        frame.add(component);
    }

    // MODIFIES: this
    // EFFECTS: initializes fields to their default values
    protected abstract void initializeFields();
}
