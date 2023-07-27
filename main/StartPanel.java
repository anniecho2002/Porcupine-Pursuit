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

public class StartPanel extends JLabel implements ActionListener {

    // Fields for sprite animation
    private Point spriteLocation = new Point(-120, 530);
    private Img currSprite = Img.foxRun0;
    private Img[] spriteWalking = {Img.foxRun0, Img.foxRun1, Img.foxRun2, Img.foxRun3, Img.foxRun4, Img.foxRun5, Img.foxRun6}; 
    private Img[] spriteIdle = {Img.foxIdle0, Img.foxIdle1, Img.foxIdle2, Img.foxIdle3, Img.foxIdle4}; 
    private int spriteCounter = 0;

    // Fields for porcupine animation
    private Point enemyLocation = new Point(890, 485);
    private Img currEnemy = Img.porcWalk0;
    private Img[] enemyWalking = {Img.porcWalk0, Img.porcWalk1, Img.porcWalk2, Img.porcWalk3, Img.porcWalk4}; 
    private Img[] enemyIdle = {Img.porcSleep0, Img.porcSleep1, Img.porcSleep2, Img.porcSleep3, Img.porcSleep4}; 
    private int enemyCounter = 0;
    private boolean enemyComplete = false;


    // Start panel fields
    private Img backgroundImg = Img.home_background;
    Timer timer; 

    public StartPanel(int width, int height) {
        this.setBounds(0,0, width, height);
        timer = new Timer(110, this);
        timer.start();
    }

    /**
     * Draw the animation onto the JLabel
     */
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(backgroundImg.image, 0, 0, null); // Background image
        
        // Scale down the currSprite by a factor of 0.2 (1/5)
        scaledPaint(g2d, 0.4, currSprite.image, spriteLocation, false);
        scaledPaint(g2d, 0.35, currEnemy.image, enemyLocation, true);

        

    }

    private void scaledPaint(Graphics2D g2d, double scaleFactor, BufferedImage image, Point location, boolean flipped){
        int newWidth = (int) (image.getWidth() * scaleFactor);
        int newHeight = (int) (image.getHeight() * scaleFactor);

        // Create a scaled version of the sprite
        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
        AffineTransformOp scaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);
        scaleOp.filter(image, scaledImage);
        
        // Translate the position to draw the scaled sprite at (x, y)
        int scaledX = (int) (location.x() + (image.getWidth() - newWidth) / 2.0);
        int scaledY = (int) (location.y() + (image.getHeight() - newHeight) / 2.0);

            // Perform horizontal flip if needed
        if (flipped) {
            AffineTransform flipTransform = AffineTransform.getScaleInstance(-1, 1);
            flipTransform.translate(-scaledImage.getWidth(), 0);
            AffineTransformOp flipOp = new AffineTransformOp(flipTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            scaledImage = flipOp.filter(scaledImage, null);
        }
        
        // Draw the scaled currSprite
        g2d.drawImage(scaledImage, scaledX, scaledY, null);
    }

    /**
     * Change the coordinates and image of text and chap every 200 milliseconds.
     * This will create the animation itself. 
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Redrawing the sprite for its animation.
        spriteCounter++;
        if(spriteLocation.x() < 180) {
            if(spriteCounter >= this.spriteWalking.length) spriteCounter = 0;
            currSprite = spriteWalking[spriteCounter];
            spriteLocation = new Point(spriteLocation.x() + 10, spriteLocation.y()); 
        } else { 
            if(spriteCounter >= this.spriteIdle.length) spriteCounter = 0;
            currSprite = spriteIdle[spriteCounter];
        }

        // Redrawing the enemy for its animation.
        enemyCounter++;
        if(enemyLocation.x() > 570) {
            if(enemyCounter >= this.enemyWalking.length) enemyCounter = 0;
            currEnemy = enemyWalking[enemyCounter];
            enemyLocation = new Point(enemyLocation.x() - 10, enemyLocation.y()); 
        } else { 
            if(enemyComplete){
                currEnemy = enemyIdle[enemyIdle.length - 1];
            } else {
                if(enemyCounter >= this.enemyIdle.length){
                    enemyComplete = true;
                    enemyCounter = enemyIdle.length - 1;
                }
                currEnemy = enemyIdle[enemyCounter];
            }
        }

        repaint();
    }
}
