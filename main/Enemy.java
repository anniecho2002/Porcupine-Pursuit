package main;

import java.awt.Dimension;
import java.awt.Graphics;

import imgs.Img;

class Enemy implements Entity {

    /**
     * Stores location of enemy on the map.
     */
    Point location;
    Point originalLocation;

    /**
     * Stores the current state of the enemy.
     */
    EnemyState state = new AwakeState();

    /**
     * Stores the original state of the enemy.
     */
    EnemyState originalState = new AwakeState();


    /**
     * Stores the current image of the enemy.
     */
    public Img currentImg = Img.porcWalk0;

    public boolean flipped = false;

    Img[] walkImages = { Img.porcWalk0, Img.porcWalk1, Img.porcWalk2, Img.porcWalk3, Img.porcWalk4 };
    Img[] spikeImages = { Img.porcSpike0, Img.porcSpike1, Img.porcSpike2, Img.porcSpike3, Img.porcSpike4, Img.porcSpike5, Img.porcSpike6 };
    Img[] sleepImages = { Img.porcSleep0, Img.porcSleep1, Img.porcSleep2, Img.porcSleep3, Img.porcSleep4 };

    /**
     * The speed of which the enemy travels.
     */
    public double speed = 0.06d;

    /**
     * Returns the speed of the enemy.
     * 
     * @return
     */
    public double speed() {
        return speed;
    }

    /**
     * Returns the location of the enemy.
     */
    public Point location() {
        return location;
    }

    /**
     * Sets the location of the enemy.
     */
    public void location(Point p) {
        location = p;
    }

    /**
     * Creates an enemy at a given location.
     * 
     * @param location
     */
    Enemy(Point location) {
        this.location = location;
        this.originalLocation = location;
    }

    /**
     * Creates an enemy at a given location and speed.
     * 
     * @param location
     */
    Enemy(Point location, double speed) {
        this.location = location;
        this.originalLocation = location;
        this.speed = speed;
    }

    /**
     * Creates an enemy at a given location and a given state.
     * 
     * @param location
     * @param s
     */
    Enemy(Point location, EnemyState s) {
        this.location = location;
        this.originalLocation = location;
        this.state = s;
        this.originalState = s;
    }


    /**
     * Creates an enemy at a given location, state, and speed.
     * 
     * @param location
     * @param s
     */
    Enemy(Point location, EnemyState s, double speed) {
        this.location = location;
        this.originalLocation = location;
        this.state = s;
        this.originalState = s;
        this.speed = speed;
    }

    /**
     * Pings depending on the current state.
     */
    public void ping(Game m) {
        state.ping(this, m);
    }

    /**
     * Draws the enemy.
     */
    public void draw(Graphics g, Point center, Dimension size) {
        drawImg(currentImg.image, g, center, size, 5, flipped);
    }

}