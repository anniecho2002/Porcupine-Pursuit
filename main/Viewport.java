package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import imgs.Img;

class Viewport extends JPanel {

    Game game;
    Keys keys = new Keys();

    /**
     * Creates a Viewport that presents the game (current level).
     * @param game The current game to display.
     */
    public Viewport(Game game) {
        this.game = game;
    }

    /**
     * The center of the viewport is the sprite's location.
     * The camera is centered on the sprite.
     * @return
     */
    public Point center() {
        return game.getSprite().location();
    }

    public void drawImg(BufferedImage img, Graphics g, Point center, Dimension size, double growth) {
        double lx = center.x() - img.getWidth() * 0.5 / growth;
        double ly = center.y() - img.getHeight() * 0.5 / growth;
        double imgWidth = img.getWidth() / growth;
        double imgHeight = img.getHeight() / growth;

        int w1 = (int) lx;
        int w2 = (int) (lx + imgWidth);
        int h1 = (int) ly;
        int h2 = (int) (ly + imgHeight);

        boolean outOfBounds = h2 <= 0 || w2 <= 0 || h1 >= size.height || w1 >= size.width;
        if (outOfBounds) {
            return;
        }

        int imageX1 = 0;
        int imageX2 = img.getWidth();
        int imageY1 = 0;
        int imageY2 = img.getHeight();

        g.drawImage(img, w1, h1, w2, h2, imageX1, imageY1, imageX2, imageY2, null);
    }



    /**
     * Paints the game and all the elements inside of it.
     * Based on the current game.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension size = getSize();
        Point centerP = new Point(
                - size.width / (double) (2 * Cell.renderX),
                - size.height / (double) (2 * Cell.renderY));
        Point center = center().add(centerP);

        game.cells().performActionInRange(center().toCoord(), 50, cell -> cell.draw(g, center, size)); // Draws all cells.
        game.getEntities().forEach(e -> e.draw(g, center, size)); // Draws all the entities (sprites, enemies, collectables).
        game.getCollectables().forEach(e -> e.draw(g, center, size)); // Draws all the collectables.
        game.getSprite().draw(g, center, size);
        for (int i = 0; i < game.getHealth(); i++){
            drawImg(Img.heart.image, g, new Point(40 + (i * 45), 40), size, 2);
        }
        game.getTextBox().draw(g, center, size);
    }

}
