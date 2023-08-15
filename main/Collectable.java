package main;

import java.awt.Dimension;
import java.awt.Graphics;

import imgs.Img;

public class Collectable implements Entity {

    /**
     * Stores location of collectable on the map.
     */
    Point location;

    /**
     * Stores the image of the collectable.
     */
    Img img = Img.orange;

    /**
     * Stores the state of the collectable.
     * Can be either Stationary or Moving.
     */
    CollectableState state = new StationaryCollectable();

    /*
     * The speed that the collectable travels.
     */
    public final double speed = 0.09d;

    
    /**
     * Fields only used for walking collectables.
     */
    int ticks = 0;
    double randTime = Math.random() * 50;
    Point randPoint = new Point(Math.random() * 16, Math.random() * 16);

    public boolean flipped = false;




    /**
     * A constructor for a generic collectable (Stationary).
     */
    public Collectable(Point location){
        this.location = location;
    }


    /**
     * A constructor for a collectable of a given state/type.
     */
    public Collectable(Point location, CollectableState state){
        this.location = location;
        this.state = state;
    }


    @Override
    public void ping(Game game) {
        this.state.ping(this, game);
    }

    /**
     * Draws the collectable.
     */
    @Override
    public void draw(Graphics g, Point center, Dimension size) {
        drawImg(img.image, g, center, size, 3.5, flipped);
    }


    /**
     * Retuns the location of the collectable.
     */
    @Override
    public Point location() {
        return this.location;
    }

    public double speed(){
        return this.speed;
    }

    public int getTicks(){
        return this.ticks;
    }

    public Point getRandPoint(){
        return this.randPoint;
    }

    public void setTicks(int ticks){
        this.ticks = ticks;
    }

    public void setRandPoint(Point randPoint){
        this.randPoint = randPoint;
    }

    public double getRandTime(){
        return this.randTime;
    }

    
}
}


