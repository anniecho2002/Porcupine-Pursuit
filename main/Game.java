package main;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface representing the game.
 * 
 * @author anniecho
 *
 */
public class Game {

    private Sprite sprite;
    private List<Entity> entities;
    private List<Entity> collectables;
    private List<Decoration> decorations;
    private Map cells;
    private Runnable onWin;
    private Runnable onLose;
    private TextBox textBox;

    private boolean startGame = false;


    public Game(Sprite sprite, List<Entity> entities, List<Entity> collectables, List<Decoration> decorations,
                Map cells, Runnable onWin, Runnable onLose, TextBox textBox){
        this.sprite = sprite;
        this.entities = entities;
        this.collectables = collectables;
        this.decorations = decorations;
        this.cells = cells;
        this.onWin = onWin;
        this.onLose = onLose;
        this.textBox = textBox;
    }

    public Sprite getSprite(){
        return this.sprite;
    }

    public List<Entity> getEntities(){
        return this.entities;
    }

    public List<Entity> getCollectables(){
        return this.collectables;
    }

    public List<Decoration> getDecorations(){
        return this.decorations;
    }

    public TextBox getTextBox(){
        return this.textBox;
    }

    public void remove(Entity e){ 
        entities = entities.stream()
          .filter(ei -> !ei.equals(e))
          .toList();

        collectables = collectables.stream()
          .filter(ei -> !ei.equals(e))
          .toList();
    }


    public void add(Enemy e){
        entities.add(e);
    }


    public Map cells(){ 
        return cells; 
    }

    public void onNextLevel(){ 
        onWin.run(); 
    }

    public void onGameOver(){ 
        onLose.run(); 
    }

    public int getHealth(){
        return sprite.getGrowthIndex() + 1;
    }
    
    public void startGame(){
        startGame = true;
    }

    public void tick() {
        getSprite().ping(this);
        getTextBox().ping(this);
        if(!startGame) return;
        Sprite s = getSprite();
        if (s.getGrowthIndex() < 0) onGameOver();

        // Check if all the monsters are still there
        // If there are no more monsters
        getEntities().forEach(e -> e.ping(this));
        getCollectables().forEach(e -> e.ping(this));
        boolean lose = getCollectables().stream().noneMatch(e -> e instanceof Collectable);
        if (lose) onGameOver();
    }
}