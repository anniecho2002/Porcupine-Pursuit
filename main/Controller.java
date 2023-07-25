package main;

import java.awt.event.KeyEvent;

class Controller extends Keys {
    
    Controller(Sprite c, char[] keyCodes) {
        // Sets sprite actions based on the keycodes passed in from the GUI.
        setAction(KeyEvent.getExtendedKeyCodeForChar(keyCodes[0]), c.set(Direction::up), c.set(Direction::unUp));
        setAction(KeyEvent.getExtendedKeyCodeForChar(keyCodes[1]), c.set(Direction::left), c.set(Direction::unLeft));
        setAction(KeyEvent.getExtendedKeyCodeForChar(keyCodes[2]), c.set(Direction::down), c.set(Direction::unDown));
        setAction(KeyEvent.getExtendedKeyCodeForChar(keyCodes[3]), c.set(Direction::right), c.set(Direction::unRight));
    }
}