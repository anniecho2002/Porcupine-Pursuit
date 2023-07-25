package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import imgs.Img;

public record Cell(List<Img> imgs, int x, int y) {

    public static final int renderX = 80;
    public static final int renderY = 80;

    public void draw(Graphics g, Point center, Dimension size) {
        // Determining bounds of the map
        int w1 = x * Cell.renderX - (int) (center.x() * Cell.renderX);
        int h1 = y * Cell.renderY - (int) (center.y() * Cell.renderY);
        int w2 = w1 + Cell.renderX;
        int h2 = h1 + Cell.renderY;

        // If out of bounds, do not draw the default flooring
        boolean outOfBounds = outOfBounds(w1, w2, h1, h2, size);
        if (outOfBounds)
            return;

        imgs.forEach(i -> {
            int imgWidth = i.image.getWidth(null);
            int imgHeight = i.image.getHeight(null);
            g.drawImage(i.image, w1, h1, w2, h2, 0, 0, imgWidth, imgHeight, null);
        });
    }

    /**
     * Determines if it is out of bounds.
     * 
     * @param w1
     * @param w2
     * @param h1
     * @param h2
     * @param size
     * @return
     */
    private boolean outOfBounds(int w1, int w2, int h1, int h2, Dimension size) {
        return h2 <= 0 || w2 <= 0 || h1 >= size.height || w1 >= size.width;
    }

}