package main;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents a level in the game.
 * Stores a Game and a Controller.
 * @author anniecho
 *
 */
record Level(Game game, Controller controller){ 
  
  static Level level(Runnable onWin, Runnable onLose, char[] keyCodes, ArrayList<Entity> monsters, ArrayList<Entity> collectables) {
    
    // Setting up the sprite, controller, and map.
    Sprite sprite = new Sprite(new Point(8,8));
    Controller controller = new Controller(sprite, keyCodes);
    Map cells = new Map();

    // Other elements for visual effect.
    TextBox tb = new TextBox(new Point(8, 9.5));

    // Creating a new Level object to return.
    Game game = new Game(sprite, monsters, collectables, cells, onWin, onLose, tb);
    sprite.addGame(game);
    return new Level(game, controller);
  }

}