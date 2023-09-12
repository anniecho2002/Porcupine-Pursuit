package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import imgs.Img;

public class TextBox {
    
    private Point location;
    private boolean visible;
    private int level;

    public TextBox(Point location, int level){
        this.location = location;
        this.visible = true;
        this.level = level;
    }

    public void setInvisible(){
        this.visible = false;
    }


    public void ping(Game game){
        if(!game.getSprite().location().equals(new Point(8, 8))){
            setInvisible();
            game.startGame();
        }
    }

    public Point location(){
        return this.location;
    }

    void draw(Graphics g, Point center, Dimension size){
        if(visible){
            if(level == 1) drawImg(Img.levelone.image, g, center, size, 4);
            if(level == 2) drawImg(Img.leveltwo.image, g, center, size, 4);
            if(level == 3) drawImg(Img.levelthree.image, g, center, size, 4);
        }
    }


    public void drawImg(BufferedImage img, Graphics g, Point center, Dimension size, double growth) {

        Point location = this.location;
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
        g.drawImage(img, w1, h1, w2, h2, imageX1, imageY1, imageX2, imageY2, null);
    }

}
