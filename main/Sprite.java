package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.List;
import java.util.ArrayList;

import imgs.Img;

/**
 * The sprite character.
 * The camera is centered on the sprite.
 * 
 * @author anniecho
 *
 */
class Sprite extends ControllableDirection implements Entity{
  
  private Game game;

  /**
   * Stores the location of the sprite on the map.
   */
  private Point location;

  
  /**
   * Stores the different animated images of the sprite.
   */
  private Img[] idleImages = {Img.foxIdle0, Img.foxIdle1, Img.foxIdle2, Img.foxIdle3, Img.foxIdle4};
  private Img[] walkImages = {Img.foxRun0, Img.foxRun1, Img.foxRun2, Img.foxRun3, Img.foxRun4, Img.foxRun5, Img.foxRun6, Img.foxRun7};


  /**
   * Stores the index of the current image of the animation.
   */
  private int currImage;


  /**
   * A number of pings/ticks to wait before changing the animation.
   * Slows down transition.
   */
  private int wait = 3;


  /**
   * Stores the current number of pings between animation transition.
   */
  private int currWait = 0;

  private Direction prevDirection = Direction.None;

  // Left size is smaller fox, right side is bigger fox.
  private double[] growthSize = {5.0, 4.4, 3.8, 3.2, 2.6};
  private double[] closeness = {0.3, 0.4, 0.5, 0.6, 0.7};
  private double[] speed = {0.1, 0.105, 0.11, 0.115, 0.115};
  private int growthIndex = 0;
  
  
  /**
   * Creates a sprite character
   * @param location
   */
  Sprite(Point location){ 
    this.location = location; 
    this.currImage = 0;
  }  

  public int getGrowthIndex(){
    return growthIndex;
  }

  public double speed(){
    return speed[growthIndex];
  }

  public double closeness(){
    return closeness[growthIndex];
  }

  public void grow(){
    // If you are not at the biggest size then grow!
    if(growthIndex < growthSize.length - 1) growthIndex++;
    else game.onNextLevel();
  }

  public void shrink(){
    // If you are not at the smallest size then shrink!
    if(growthIndex > 0) growthIndex--;
    else game.onGameOver();
  }


  
  public void addGame(Game g){
    this.game = g;
  }
  
  /**
   * Returns the sprite's location.
   */
  public Point location(){ 
    return location; 
  }
  
  
  /**
   * Sets the sprite's location to a certain point.
   * Relocates the sprite on the map.
   */
  public void location(Point p){ 
    location = p; 
  }
  
  
  /**
   * Points to where the sprite is next moving.
   * Depends on the direction of the entity.
   * @return
   */
  public Point findNewLocation(){ 
    Point distance = direction().arrow(speed());
    Point newLocation = location().add(distance);
    if(newLocation.x() < 1 || newLocation.y() < 0.7 || newLocation.x() >= 14.9 || newLocation.y() >= 15) {
      return new Point(100,100);
    } else{
      return distance; 
    }
  }
  
  
  /**
   * Sets the new location of the sprite after moving.
   * Based on the arrow direction.
   */
  public void ping(Game game) {
    if(direction() != Direction.None) prevDirection = direction();
    Point newLocation = findNewLocation();
    if(newLocation.equals(new Point(100,100))){
      location(this.location());
    } else{
      location(location().add(findNewLocation()));
    }
  }

  /**
   * Returns the correct array of images based on the currentDirection.
   * @param g
   * @param center
   * @param size
   */
  private Img[] getAnimation(){
    if(this.direction() == Direction.None) return idleImages;
    else return walkImages;
  }
  
  
  /**
   * Redraws the sprite.
   */
  public void draw(Graphics g, Point center, Dimension size){
    Img[] spriteImages = getAnimation();
    if(currImage >= spriteImages.length) currImage = 0;

    // Drawing image depending on direction (flipped or not)
    if(prevLeft()) drawImg(spriteImages[currImage].image, g, center, size, growthSize[growthIndex], true);
    else drawImg(spriteImages[currImage].image, g, center, size, growthSize[growthIndex], false);
    
    currWait++;
    if(currWait < wait) return;
    currWait = 0;
    currImage++;
  }


  public boolean prevLeft(){
    return prevDirection == Direction.Left || prevDirection == Direction.UpLeft || prevDirection == Direction.DownLeft;
  }
}