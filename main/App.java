package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
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
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
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
        temp.setForeground(Color.WHITE);
        temp.setFont(FONT_THEME);
        return temp;
    }

    private JLabel getTextBox(){
        Icon icon = new ImageIcon("imgs/textbox.gif");
        JLabel label = new JLabel();
        label.setIcon(icon);
        return label;
    }


    private void home() {
        ImageIcon backgroundImage = new ImageIcon("imgs/home_background.png");
        JPanel homePanel = new JPanel();
        homePanel.setLayout(null);
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, backgroundImage.getIconWidth(), backgroundImage.getIconHeight());

        // Textbox image
        JLabel tb = getTextBox();
        tb.setBounds(WIDTH/2 - 120, HEIGHT/2 + 100, 300, 126);

        
        // Buttons to be displayed on the main home screen.
        JButton start = makeButton("START GAME");
        JButton select = makeButton("SELECT KEYS");
        start.setBounds(WIDTH/2 - 120, 580, 142, 35);
        select.setBounds(WIDTH/2 + 30, 580, 142, 35);

        // Laying out buttons in a top and bottom panel.
        start.addActionListener((e) -> play());
        select.addActionListener((e) -> select());

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


    private void select() {

        ImageIcon backgroundImage = new ImageIcon("imgs/home_background.png");
        JPanel selectPanel = new JPanel();
        selectPanel.setLayout(null);
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, backgroundImage.getIconWidth(), backgroundImage.getIconHeight());


        // Creating a back button that returns to home page.
        JButton back = makeButton("BACK TO HOME");
        back.setBounds(WIDTH/2 - 45, 580, 142, 35);
        back.addActionListener(e -> home());
        
    
        // Creating the combo boxes for selecting new keys.
        ArrayList<JComboBox> boxes = new ArrayList<JComboBox>();
        ArrayList<JLabel> labels = new ArrayList<JLabel>();


        for (int i = 0; i < labelNames.length; i++) {
            boxes.add(new JComboBox(options));
            labels.add(makeLabel(labelNames[i]));
            boxes.get(i).setSelectedIndex(defaultKeys[i]);

            labels.get(i).setBounds(80 + 200 * i, 540, 100, 50);
            boxes.get(i).setBounds(130 + 200 * i, 540, 90, 50);
            
            selectPanel.add(labels.get(i));
            selectPanel.add(boxes.get(i));
        }
        boxes.forEach((i) -> i.addActionListener(e -> defaultKeys[boxes.indexOf(i)] = Arrays.asList(options).indexOf(i.getSelectedItem())));
        

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
            new Collectable(new Point(5, 5), new MovingCollectable()),
            new Collectable(new Point(11, 11), new MovingCollectable()),
            new Collectable(new Point(5, 11), new MovingCollectable()),
            new Collectable(new Point(11, 5), new MovingCollectable())
            ));
        }
        return null;
    }
}
