package main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

import javax.swing.JLabel;

import imgs.Img;

public class EndPanel extends JLabel implements ActionListener {

    // Start panel fields
    private Img backgroundImg;
    private boolean win;
    Timer timer; 


    /**
     * Creates an EndPanel to be displayed in the app.
     * What is displayed depends on Win or Loss.
     * @param width The app's width, to be set as label bounds.
     * @param height The app's height, to be set as label bounds.
     */
    public EndPanel(int width, int height, Img backgroundImg, boolean win) {
        this.backgroundImg = backgroundImg;
        this.win = win;
        this.setBounds(0,0, width, height);
        timer = new Timer(110, this);
        timer.start();
    }


    /**
     * Draw the animation onto the JLabel.
     */
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(backgroundImg.image, 0, 0, null);
        scaledPaint(g2d, 1, clouds[0].image, cloud0Location, cloudsFlipped[0]);
        scaledPaint(g2d, 1, clouds[1].image, cloud1Location, cloudsFlipped[1]);
    }
    


    /**
     * Draws the images scaled and flipped if necessary.
     * @param g2d
     * @param scaleFactor
     * @param image
     * @param location
     * @param flipped
     */
    private void scaledPaint(Graphics2D g2d, double scaleFactor, BufferedImage image, Point location, boolean flipped){
        int newWidth = (int) (image.getWidth() * scaleFactor);
        int newHeight = (int) (image.getHeight() * scaleFactor);

        // Create a scaled version of the image
        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
        AffineTransformOp scaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);
        scaleOp.filter(image, scaledImage);
        
        // Translate the position to draw the scaled image at (x, y)
        int scaledX = (int) (location.x() + (image.getWidth() - newWidth) / 2.0);
        int scaledY = (int) (location.y() + (image.getHeight() - newHeight) / 2.0);

        // Perform horizontal flip if needed
        if (flipped) {
            AffineTransform flipTransform = AffineTransform.getScaleInstance(-1, 1);
            flipTransform.translate(-scaledImage.getWidth(), 0);
            AffineTransformOp flipOp = new AffineTransformOp(flipTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            scaledImage = flipOp.filter(scaledImage, null);
        }
        g2d.drawImage(scaledImage, scaledX, scaledY, null);
    }

    


    /**
     * Change the coordinates and image of each element every tick of the timer.
     * This will create the animation itself. 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        cloudAnimation();
        if(this.win){
            spriteWinAnimation();
            enemyWinAnimation();
        } else{
            spriteLoseAnimation();
            enemyLoseAnimation();
        }
        repaint();
    }


/*------ DRAWING THE ANIMATIONS ------------------------------------------------------------------------------------------------------- */

    // Fields for cloud animation
    private Point cloud0Location = new Point(970, 150);
    private Point cloud1Location = new Point(970, 250);
    private Img[] clouds = { Img.cloud0, Img.cloud1 };
    private boolean[] cloudsFlipped = { true, true };
    private int[] cloudsSpeed = { 1, 3 };

    /**
     * Determines how the cloud is animated.
     */
    private void cloudAnimation(){
        cloud0Location = new Point(cloud0Location.x() - cloudsSpeed[0], cloud0Location.y());
        cloud1Location = new Point(cloud1Location.x() - cloudsSpeed[1], cloud1Location.y());
        cloud0Location = checkOffScreen(0, cloud0Location);
        cloud1Location = checkOffScreen(1, cloud1Location);
    }

    /**
     * Helper method for cloudAnimation().
     * Checks if the cloud is offscreen.
     * @param num The index of the cloud.
     * @param location The location of the cloud.
     * @return The new location of the cloud.
     */
    private Point checkOffScreen(int num, Point location){
        if(location.x() < - 300){
            randomize(num);
            return new Point(900, (int)(Math.random() * (500 - 100 + 1)) + 100);
        } else {
            return location;
        }
    }

    /**
     * Helper method for checkOffScreen().
     * If the cloud is now offscreen, randomize the image, flip, and speed
     */
    private void randomize(int num){
        if((int)(Math.random() * 2) == 1) clouds[num] = Img.cloud0;
        else clouds[num] = Img.cloud1;
        if((int)(Math.random() * 2) == 1) cloudsFlipped[num] = true;
        else cloudsFlipped[num] = false;
        cloudsSpeed[num] = (int)(Math.random() * 4) + 1;
    }

/*------ DRAWING THE ANIMATIONS -------------------------------------------------------------------------------------------------- */

    private void spriteWinAnimation(){

    }

    private void enemyWinAnimation(){
        
    }

    private void spriteLoseAnimation(){

    }

    private void enemyLoseAnimation(){
        
    }

}
