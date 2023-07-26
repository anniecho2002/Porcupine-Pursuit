package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import imgs.Img;

public class Decoration {
    
    private Point location;
    private Img currentImg;

    public Decoration(Point location){
        this.location = location;
        this.currentImg = Img.flower;
    }

    public Point location(){
        return this.location;
    }

    void draw(Graphics g, Point center, Dimension size){
        drawImg(currentImg.image, g, center, size, 5);
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
