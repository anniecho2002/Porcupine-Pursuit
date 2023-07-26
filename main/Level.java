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
    /*ArrayList<Decoration> decorations = new ArrayList<>(List.of(
      new Decoration(new Point(7.5, 7)),
      new Decoration(new Point(9, 6.5)),
      new Decoration(new Point(8.4, 8.2))
    )); */

    ArrayList<Decoration> decorations = new ArrayList<>(List.of());

    // Creating a new Level object to return.
    Game game = new Game(sprite, monsters, collectables, decorations, cells, onWin, onLose, tb);
    sprite.addGame(game);
    return new Level(game, controller);
  }

}