package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;

class App extends JFrame {

    // Fields for GUI.
    private int WIDTH = 900;
    private int HEIGHT = 680;
    private Color COLOR_THEME = new Color(74, 115, 64);
    private Font FONT_THEME = new Font("Arial Black", Font.BOLD, 12);


    // Fields for setting the key changes.
    private int[] defaultKeys = { 22, 0, 18, 3 };
    private String[] labelNames = { "UP:", "DOWN:", "LEFT:", "RIGHT:" };
    private String[] options = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "0" };


    // Runs when a GUI panel is closed.
    private Runnable closePhase = () -> {};


    /**
     * Creates an App JPanel.
     */
    public App() {
        assert SwingUtilities.isEventDispatchThread();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setTitle("Porcupine Pursuit");
        home();
        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                closePhase.run();
            }
        });
    }


    /**
     * Helper method for creating JButtons.
     * @param text Text to put inside JButton.
     * @return JButton with color theme and font.
     */
    private JButton makeButton(String text) {
        JButton temp = new JButton(text);
        temp.setForeground(COLOR_THEME);
        temp.setFont(FONT_THEME);
        return temp;
    }


    /**
     * Helper method for creating JLabels.
     * @param text Text to put inside JLabel.
     * @return JLabel with color theme and font.
     */
    private JLabel makeLabel(String text) {
        JLabel temp = new JLabel(text);
        temp.setForeground(COLOR_THEME);
        temp.setFont(FONT_THEME);
        return temp;
    }


    /**
     * Creates a home page JPanel.
     */
    private void home() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        Icon icon = new ImageIcon("imgs/textbox.gif");
        JLabel label = new JLabel();
        label.setIcon(icon);
        


        // Home panel for the buttons.
        JPanel homePanel = new JPanel();
        homePanel.setBounds(0, HEIGHT - 90, WIDTH, HEIGHT - 20);
        homePanel.requestFocus();

        // Buttons to be displayed on the main home screen.
        JButton start = makeButton("START GAME");
        JButton select = makeButton("SELECT KEYS");
        homePanel.add(start);
        homePanel.add(select);

        // Panel for the animated textbox.
        JPanel textbox = new JPanel();
        textbox.add(label, BorderLayout.CENTER);

        // Panel for the bottom textbox.
        JPanel bottomPanel = new JPanel();
        textbox.add(new JLabel());


        // Laying out buttons in a top and bottom panel.
        start.addActionListener((e) -> play());
        select.addActionListener((e) -> select());

        add(bottomPanel, BorderLayout.SOUTH);
        add(textbox, BorderLayout.CENTER);
        add(homePanel, BorderLayout.NORTH);


        // To be executed when the home is closed.
        closePhase.run();
        closePhase = () -> {
            remove(homePanel);
            remove(textbox);
            remove(bottomPanel);
        };

        // Adding ActionListeners to button to be executed when pressed.

        pack();
    }


    /*
     * Creates a JPanel for selecting new keys.
     */
    private void select() {

        // Creating the select panel.
        JPanel selectPanel = new JPanel();
        selectPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        selectPanel.setFocusable(true);
        

        // Creating a back button that returns to home page.
        JButton back = makeButton("BACK TO HOME");
        selectPanel.add(back);
        add(selectPanel, BorderLayout.CENTER);
        back.addActionListener(e -> home());


        // Creating the combo boxes for selecting new keys.
        JPanel comboPanel = new JPanel();
        ArrayList<JComboBox> boxes = new ArrayList<JComboBox>();
        ArrayList<JLabel> labels = new ArrayList<JLabel>();


        for (int i = 0; i < labelNames.length; i++) {
            boxes.add(new JComboBox(options));
            labels.add(makeLabel(labelNames[i]));
            boxes.get(i).setSelectedIndex(defaultKeys[i]);
            boxes.get(i).setBounds(130 * i, 40, 100, 50);
            labels.get(i).setBounds(130 * i + 5, 25, 130, 50);
            comboPanel.add(BorderLayout.CENTER, labels.get(i));
            comboPanel.add(BorderLayout.CENTER, boxes.get(i));
        }
        add(comboPanel, BorderLayout.SOUTH);
        boxes.forEach((i) -> i.addActionListener(
                e -> defaultKeys[boxes.indexOf(i)] = Arrays.asList(options).indexOf(i.getSelectedItem())));
        

        closePhase.run();
        closePhase = () -> {
            remove(selectPanel);
            remove(comboPanel);
            remove(back);
            boxes.forEach(e -> remove(e));
            labels.forEach(e -> remove(e));
        };
        pack();
    }


    /**
     * Executes when the start button is pressed.
     * Creates a new level and sets the phase to the current level.
     */
    private void play() {
        setPhase(Level.level(() -> win(), () -> lose(),
                (options[defaultKeys[0]] + options[defaultKeys[1]] + options[defaultKeys[2]] + options[defaultKeys[3]]).toCharArray(),
                getLevelEnemies(1), getLevelCollectables(1)));
    }


    /**
     * Executes when the game is won. 
     */
    private void win() {
        add(BorderLayout.CENTER, new JLabel("Victory!"));
        closePhase.run();
        pack();
    }


    /**
     * Executes when the game is lost. 
     */
    private void lose() {
        add(BorderLayout.CENTER, new JLabel("Loss!"));
        closePhase.run();
        pack();
    }


    /**
     * Sets the view port to the current level and begins the game.
     * @param level
     */
    void setPhase(Level level) {

        // Set up the viewport.
        Viewport viewport = new Viewport(level.game());
        viewport.addKeyListener(level.controller());
        viewport.setFocusable(true);


        // Set up the timer (the tick of the game).
        Timer timer = new Timer(34, unused -> {
            assert SwingUtilities.isEventDispatchThread();
            level.game().tick();
            viewport.repaint();
        });


        // Close phase before adding any element of the new phase.
        closePhase.run(); 
        closePhase = () -> {
            timer.stop();
            remove(viewport);
        };


        add(BorderLayout.CENTER, viewport);
        setPreferredSize(getSize());
        pack();
        viewport.requestFocus();
        timer.start();
    }


    static ArrayList<Entity> getLevelEnemies(int level){
        if(level == 1){
            return new ArrayList<>(List.of(
            new Enemy(new Point(2, 2)), new Enemy(new Point(2, 14)),
            new Enemy(new Point(14, 14)), new Enemy(new Point(14, 2)),
            new Enemy(new Point(7, 2), new RoamingState()),
            new Enemy(new Point(7, 14), new FollowState())
            ));
        }
        return null;
    }

    static ArrayList<Entity> getLevelCollectables(int level){
        if(level == 1){
            return new ArrayList<>(List.of(
            new Collectable(new Point(1, 1)), new Collectable(new Point(1, 15)),
            new Collectable(new Point(15, 15)), new Collectable(new Point(15, 1)),
            new Collectable(new Point(4, 4), new MovingCollectable()),
            new Collectable(new Point(12, 12), new MovingCollectable()),
            new Collectable(new Point(4, 12), new MovingCollectable()),
            new Collectable(new Point(12, 4), new MovingCollectable())
            ));
        }
        return null;
    }
}
