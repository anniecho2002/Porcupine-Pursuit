package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

/**
 * Determines what action to execute based on controller events.
 * 
 * @author anniecho
 *
 */
class Keys implements KeyListener {

    /**
     * Stores the KeyEvents and corresponding actions to execute when pressed.
     */
    private Map<Integer, Runnable> actionsPressed = new HashMap<>();

    /**
     * Stores the KeyEvents and corresponding actions to execute when released.
     */
    private Map<Integer, Runnable> actionsReleased = new HashMap<>();

    /**
     * Sets the key pressed to given action.
     * Called inside Controller constructor.
     * 
     * @param keyCode    The KeyEvent keycode, e.g. VK_UP.
     * @param onPressed  Runnable to execute when keycode is pressed.
     * @param onReleased Runnnable to execute when keycode is released.
     */
    public void setAction(int keyCode, Runnable onPressed, Runnable onReleased) {
        actionsPressed.put(keyCode, onPressed);
        actionsReleased.put(keyCode, onReleased);
    }

    public void keyTyped(KeyEvent e) {
    }

    /**
     * Executes corresponding action depending on KeyEvent when pressed.
     */
    public void keyPressed(KeyEvent e) {
        assert SwingUtilities.isEventDispatchThread();
        actionsPressed.getOrDefault(e.getKeyCode(), () -> {
        }).run();
    }

    /**
     * Executes corresponding action depending on KeyEvent when released.
     */
    public void keyReleased(KeyEvent e) {
        assert SwingUtilities.isEventDispatchThread();
        actionsReleased.getOrDefault(e.getKeyCode(), () -> {
        }).run();
    }
}