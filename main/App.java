package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;

import imgs.Img;

class App extends JFrame {

    // Fields for GUI.
    private int WIDTH = 900;
    private int HEIGHT = 680;
    private Color COLOR_THEME = new Color(41,8,41);
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
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setTitle("Porcupine Pursuit");

        // Creating the background of the application now so animation is consistent between Home and Select.
        loadHome();
        levelTwo();
        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                closePhase.run();
            }
        });
    }

    /**
     * Creates the background of the application now so animation is consistent between Home and Select.
     */
    private void loadHome(){
        ImageIcon backgroundImage = new ImageIcon("imgs/home_background0.png");
        JLabel backgroundLabel = new StartPanel(WIDTH, HEIGHT, Img.home_background0, "");
        backgroundLabel.setBounds(0, 0, backgroundImage.getIconWidth(), backgroundImage.getIconHeight());
        home(backgroundLabel, backgroundImage);
    }


    /**
     * Displays the main home page.
     * @param backgroundLabel
     * @param backgroundImage
     */
    private void home(JLabel backgroundLabel, ImageIcon backgroundImage) {

        // Create panel to display on top of background.
        JPanel homePanel = new JPanel();
        homePanel.setLayout(null);

        // Textbox image to put on panel.
        JLabel tb = getTextBox(0);
        tb.setBounds(WIDTH/2 - 120, HEIGHT/2 + 100, 300, 126);

        // Buttons to be displayed on the panel.
        JButton start = makeButton("START GAME");
        JButton select = makeButton("SELECT KEYS");
        start.setBounds(WIDTH/2 - 120, 580, 142, 35);
        select.setBounds(WIDTH/2 + 30, 580, 142, 35);
        start.addActionListener((e) -> levelOne());
        select.addActionListener((e) -> select(backgroundLabel, backgroundImage));

        // Add and pack elements onto panel, then add panel to frame.
        homePanel.add(start);
        homePanel.add(select);
        homePanel.add(tb);
        homePanel.add(backgroundLabel);
        this.add(homePanel);

        this.setPreferredSize(new Dimension(backgroundImage.getIconWidth(), backgroundImage.getIconHeight()));
        this.pack();
        this.setVisible(true);

        // To be executed when the home is closed.
        closePhase.run();
        closePhase = () -> {
            remove(homePanel);
            remove(start);
            remove(select);
            remove(backgroundLabel);
        };
    }


    /**
     * Displays the select keys page.
     * @param backgroundLabel
     * @param backgroundImage
     */
    private void select(JLabel backgroundLabel, ImageIcon backgroundImage) {

        JPanel selectPanel = new JPanel();
        selectPanel.setLayout(null);

        // Creating a back button that returns to home page.
        JButton back = makeButton("BACK TO HOME");
        back.setBounds(WIDTH/2 - 45, 580, 142, 35);
        back.addActionListener(e -> home(backgroundLabel, backgroundImage));
        
    
        // Creating the combo boxes for selecting new keys.
        ArrayList<JComboBox> boxes = new ArrayList<JComboBox>();
        ArrayList<JLabel> labels = new ArrayList<JLabel>();
        for (int i = 0; i < labelNames.length; i++) {
            boxes.add(new JComboBox(options));
            labels.add(makeLabel(labelNames[i]));
            boxes.get(i).setSelectedIndex(defaultKeys[i]);
            labels.get(i).setBounds(100 + 200 * i, 420, 100, 50);
            boxes.get(i).setBounds(150 + 200 * i, 420, 90, 50);
            selectPanel.add(labels.get(i));
            selectPanel.add(boxes.get(i));
        }
        boxes.forEach((i) -> i.addActionListener(e -> defaultKeys[boxes.indexOf(i)] = Arrays.asList(options).indexOf(i.getSelectedItem())));
        

        // Add and pack elements onto the panel, then add panel to frame.
        selectPanel.add(back);
        selectPanel.add(backgroundLabel);
        add(selectPanel);

        this.setPreferredSize(new Dimension(backgroundImage.getIconWidth(), backgroundImage.getIconHeight()));
        this.pack();
        this.setVisible(true);

        closePhase.run();
        closePhase = () -> {
            SwingUtilities.invokeLater(() -> {
                boxes.forEach(e -> selectPanel.remove(e));
                labels.forEach(e -> selectPanel.remove(e));
                selectPanel.revalidate();
                selectPanel.repaint();
                remove(selectPanel);
                remove(back);
                revalidate();
                repaint();
            });
        };
        pack();
    }



    /**
     * Displays the transition panel between level one and two.
     * Creates a new StartPanel, as it will use a different animation.
     * 1 for after level one, 2 for after level two, etc.
     */
    private void transition(int level) {

        ImageIcon backgroundImage = new ImageIcon("imgs/home_background0.png");
        JLabel backgroundLabel = new StartPanel(WIDTH, HEIGHT, Img.home_background0, "foxchase");
        backgroundLabel.setBounds(0, 0, backgroundImage.getIconWidth(), backgroundImage.getIconHeight());

        JPanel transitionPanel = new JPanel();
        transitionPanel.setLayout(null);

        // Textbox image --> depends on the level.
        JLabel tb = getTextBox(level);
        tb.setBounds(WIDTH/2 - 111, HEIGHT/2 + 20, 300, 126);

        // Buttons to be displayed on the transition screen.
        JButton cont = makeButton("CONTINUE");
        cont.setBounds(WIDTH/2 - 30, 500, 142, 35);
        cont.addActionListener((e) -> levelTwo());

        // Add and pack elements onto the panel, then panel to frame.
        transitionPanel.add(cont);
        transitionPanel.add(tb);
        transitionPanel.add(backgroundLabel);
        this.add(transitionPanel);

        this.setPreferredSize(new Dimension(backgroundImage.getIconWidth(), backgroundImage.getIconHeight()));
        this.pack();
        this.setVisible(true);

        closePhase.run();
        closePhase = () -> {
            remove(transitionPanel);
            remove(cont);
            remove(backgroundLabel);
        };
    }


    /**
     * Executes when the start button is pressed.
     * Creates a new level and sets the phase to the current level.
     */
    private void levelOne() {
        setPhase(Level.level(() -> transition(1), () -> endGame(false),
                (options[defaultKeys[0]] + options[defaultKeys[1]] + options[defaultKeys[2]] + options[defaultKeys[3]]).toCharArray(),
                getLevelEnemies(1), getLevelCollectables(1), 1));
    }

    /**
     * Starts level two after level one is complete.
     * Creates a new level and sets the phase to the current level.
     * CHANGE TO TRANSITION TWO WHEN IMPLEMENTED THIRD LEVEL.
     */
    private void levelTwo() {
        setPhase(Level.level(() -> endGame(true), () -> endGame(false),
                (options[defaultKeys[0]] + options[defaultKeys[1]] + options[defaultKeys[2]] + options[defaultKeys[3]]).toCharArray(),
                getLevelEnemies(2), getLevelCollectables(2), 2));
    }


    /**
     * Starts level three after level two is complete.
     * Creates a new level and sets the phase to the current level.
     */
    private void levelThree() {
        setPhase(Level.level(() -> endGame(true), () -> endGame(false),
                (options[defaultKeys[0]] + options[defaultKeys[1]] + options[defaultKeys[2]] + options[defaultKeys[3]]).toCharArray(),
                getLevelEnemies(2), getLevelCollectables(2), 3));
    }


    /**
     * Executes when the game is won. 
     */
    private void endGame(boolean win) {
        ImageIcon backgroundImage = new ImageIcon("imgs/home_background0.png");
        JLabel backgroundLabel = new EndPanel(WIDTH, HEIGHT, Img.home_background0, win);
        backgroundLabel.setBounds(0, 0, backgroundImage.getIconWidth(), backgroundImage.getIconHeight());

        JPanel endPanel = new JPanel();
        endPanel.setLayout(null);

        // Textbox image
        JLabel tb = new JLabel();
        if(!win) tb = getTextBox(3); // Display loss textbox
        else tb = getTextBox(4);     // Display win textbox
        tb.setBounds(WIDTH/2 - 111, HEIGHT/2 + 20, 300, 126);

        
        // Buttons to be displayed on the main home screen.
        JButton back = makeButton("BACK TO HOME");
        back.setBounds(WIDTH/2 - 30, 500, 142, 35);
        back.addActionListener((e) -> loadHome());

        endPanel.add(back);
        endPanel.add(tb);
        endPanel.add(backgroundLabel);
        this.add(endPanel);

        this.setPreferredSize(new Dimension(backgroundImage.getIconWidth(), backgroundImage.getIconHeight()));
        this.pack();
        this.setVisible(true);

        // To be executed when the home is closed.
        closePhase.run();
        closePhase = () -> {
            remove(endPanel);
            remove(back);
            remove(backgroundLabel);
        };
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


// --- HELPER METHODS & GETTERS FOR ENEMIES/ COLLECTABLES -------------------------------------------------------------------------------------------

    /**
     * Helper method for creating JButtons.
     * @param text Text to put inside JButton.
     * @return JButton with color theme and font.
     */
    private JButton makeButton(String text) {
        JButton temp = new JButton(text);
        temp.setOpaque(true);
        temp.setBorder(null);
        temp.setForeground(COLOR_THEME);
        temp.setFont(FONT_THEME);
        temp.setBackground(new Color(250, 242, 210));
        temp.setBorderPainted(true);
        temp.setBorder(new RoundedBorder(10));
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
     * Helper method for creating the text box on the main/transition screens.
     * 0 = home page
     * 1 = transition 1
     * 2 = transition 2
     * 3 = loss
     * 4 = win
     * @param num Which text box image to use.
     * @return JLabel with text box image on it.
     */
    private JLabel getTextBox(int num){
        Icon icon = new ImageIcon("imgs/textbox" + num + ".gif");
        JLabel label = new JLabel();
        label.setIcon(icon);
        return label;
    }

    /**
     * Given a certain level, return the list of enemies for the level.
     * @param level Can be level 1, 2, or 3.
     * @return A list of enemies for the level.
     */
    static ArrayList<Entity> getLevelEnemies(int level){
        if(level == 1){
            return new ArrayList<>(List.of(
            new Enemy(new Point(2, 2)), new Enemy(new Point(2, 14)),
            new Enemy(new Point(14, 14)), new Enemy(new Point(14, 2)),
            new Enemy(new Point(7, 2), new RoamingState()), new Enemy(new Point(7, 14), new FollowState())
            ));
        }
        else if(level == 2){
            return new ArrayList<>(List.of(
            new Enemy(new Point(2, 2)), new Enemy(new Point(2, 14)),
            new Enemy(new Point(14, 14)), new Enemy(new Point(14, 2)),
            new Enemy(new Point(7, 2), new RoamingState()), new Enemy(new Point(7, 14), new FollowState())
            ));
        }
        return new ArrayList<>();
    }


    static ArrayList<Entity> getLevelCollectables(int level){
        if(level == 1){
            return new ArrayList<>(List.of(
            new Collectable(new Point(1, 1)), new Collectable(new Point(1, 15)),
            new Collectable(new Point(15, 15)), new Collectable(new Point(15, 1)),
            new Collectable(new Point(5, 5), new MovingCollectable()), new Collectable(new Point(11, 11), new MovingCollectable()),
            new Collectable(new Point(5, 11), new MovingCollectable()), new Collectable(new Point(11, 5), new MovingCollectable())
            ));
        }
        else if(level == 2){
            return new ArrayList<>(List.of(
            new Collectable(new Point(1, 1)), new Collectable(new Point(1, 15)),
            new Collectable(new Point(5, 5), new EscapingCollectable()), new Collectable(new Point(11, 11), new EscapingCollectable()),
            new Collectable(new Point(5, 11), new EscapingCollectable()), new Collectable(new Point(11, 5), new EscapingCollectable())
            ));
        }
        return new ArrayList<>();
    }
}


// --- MINOR CLASS FOR DRAWING ROUNDED BUTTONS -------------------------------------------------------------------------------------------

class RoundedBorder implements Border {

    private int radius;
    RoundedBorder(int radius) {
        this.radius = radius;
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
    }

    public boolean isBorderOpaque() {
        return false;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.drawRoundRect(x, y, width-1, height-1, radius, radius);
    }
}



