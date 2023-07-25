package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Represents an entity inside Game.
 * Could be a Sprite or Monster.
 * 
 * @author anniecho
 *
 */
interface Entity {

  void ping(Game m);

  void draw(Graphics g, Point center, Dimension size);

  Point location();

  default void drawImg(BufferedImage img, Graphics g, Point center, Dimension size, double growth, boolean flipped) {

    Point location = location();
    double lx = (location.x() - center.x()) * Cell.renderX;
    double ly = (location.y() - center.y()) * Cell.renderY;
    double imgWidth = img.getWidth() / growth;
    double imgHeight = img.getHeight() / growth;

    int w1 = (int) (lx - imgWidth);
    int w2 = (int) (lx + imgWidth);
    int h1 = (int) (ly - imgHeight);
    int h2 = (int) (ly + imgHeight);

    boolean outOfBounds = h2 <= 0 || w2 <= 0 || h1 >= size.height || w1 >= size.width;
    if (outOfBounds) return;

    int imageX1 = 0;
    int imageX2 = img.getWidth();
    int imageY1 = 0;
    int imageY2 = img.getHeight();

    // Flip image horizontally by swapping x-coordinates
    if (flipped) {
      int temp = imageX1;
      imageX1 = imageX2;
      imageX2 = temp;
    }
    g.drawImage(img, w1, h1, w2, h2, imageX1, imageY1, imageX2, imageY2, null);
  }


  /**
   * If the entity has not been implemented and instantiated.
   * 
   * @param p
   */
  default void location(Point p) {
    throw new Error("This entity can not move.");
  }

}