package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

import javax.swing.Timer;

import javax.swing.JLabel;

import imgs.Img;

public class StartPanel extends JLabel implements ActionListener {

    // Start panel fields
    private Img backgroundImg;
    private String animation;
    Timer timer; 


    /**
     * Creates a StartPanel to be displayed in the app.
     * @param width The app's width, to be set as label bounds.
     * @param height The app's height, to be set as label bounds.
     */
    public StartPanel(int width, int height, Img backgroundImg, String animation) {
        this.backgroundImg = backgroundImg;
        this.animation = animation;
        this.setBounds(0,0, width, height);
        if(this.animation.equals("foxchase")){
            spriteLocation = new Point(-180, 533);
            enemyLocation = new Point(-120, 485);
        }
        timer = new Timer(110, this);
        timer.start();
    }


    /**
     * Draw the animation onto the JLabel.
     */
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(backgroundImg.image, 0, 0, null); // Background image
    
        // Draw each element in the StartPanel.
        // Draw the sprite and the enemy based on the animation.
        if (animation.equals("foxchase")) scaledPaint(g2d, 0.35, currEnemy.image, enemyLocation, false);
        else scaledPaint(g2d, 0.35, currEnemy.image, enemyLocation, true);
        scaledPaint(g2d, 0.4, currSprite.image, spriteLocation, false);
    
        // Draw the clouds and the title based on the location of the clouds.
        if(animation.equals("foxchase")){
            scaledPaint(g2d, 1, clouds[0].image, cloud0Location, cloudsFlipped[0]);
            scaledPaint(g2d, 1, clouds[1].image, cloud1Location, cloudsFlipped[1]);
        } else{
            determineCloudandTitle(g2d);
        }
    }

    public void determineCloudandTitle(Graphics2D g2d){
        if (cloud0Location.y() > 280) {
            scaledPaint(g2d, 1, title.image, titleLocation, false);
            scaledPaint(g2d, 1, clouds[0].image, cloud0Location, cloudsFlipped[0]);
        } else {
            scaledPaint(g2d, 1, clouds[0].image, cloud0Location, cloudsFlipped[0]);
            scaledPaint(g2d, 1, title.image, titleLocation, false);
        }
    
        if (cloud1Location.y() > 280) {
            scaledPaint(g2d, 1, title.image, titleLocation, false);
            scaledPaint(g2d, 1, clouds[1].image, cloud1Location, cloudsFlipped[1]);
        } else {
            scaledPaint(g2d, 1, clouds[1].image, cloud1Location, cloudsFlipped[1]);
            scaledPaint(g2d, 1, title.image, titleLocation, false);
        }
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
        if(animation.equals("foxchase")){
            spriteChaseAnimation();
            enemyChaseAnimation();
        } else{
            spriteAnimation();
            enemyAnimation();
        }
        cloudAnimation();
        titleAnimation();
        repaint();
    }


/*------ DRAWING THE ANIMATIONS ------------------------------------------------------------------------------------------------------- */


    // Fields for sprite animation
    private Point spriteLocation = new Point(-120, 533);
    private Img currSprite = Img.foxRun0;
    private Img[] spriteWalking = {Img.foxRun0, Img.foxRun1, Img.foxRun2, Img.foxRun3, Img.foxRun4, Img.foxRun5, Img.foxRun6}; 
    private Img[] spriteIdle = {Img.foxIdle0, Img.foxIdle1, Img.foxIdle2, Img.foxIdle3, Img.foxIdle4}; 
    private int spriteCounter = 0;

    /**
     * Determines how the sprite is animated for a normal animation.
     */
    private void spriteAnimation(){
        spriteCounter++;
        int maxSpriteCounter = spriteLocation.x() < 180 ? this.spriteWalking.length : this.spriteIdle.length;
        if (spriteCounter >= maxSpriteCounter) spriteCounter = 0;
        if (spriteLocation.x() < 180) {
            currSprite = spriteWalking[spriteCounter];
            spriteLocation = new Point(spriteLocation.x() + 10, spriteLocation.y());
        } else {
            currSprite = spriteIdle[spriteCounter];
        }
    }


    // Fields for enemy animation
    private Point enemyLocation = new Point(890, 485);
    private Img currEnemy = Img.porcWalk0;
    private Img[] enemyWalking = {Img.porcWalk0, Img.porcWalk1, Img.porcWalk2, Img.porcWalk3, Img.porcWalk4}; 
    private Img[] enemyIdle = {Img.porcSleep0, Img.porcSleep1, Img.porcSleep2, Img.porcSleep3, Img.porcSleep4}; 
    private Img[] enemyChase = {Img.porcWalk0, Img.porcWalk1, Img.porcWalk2, Img.porcWalk3, Img.porcWalk4,
                                Img.porcSpike0, Img.porcSpike1, Img.porcSpike2, Img.porcSpike3, Img.porcSpike4, Img.porcSpike5, Img.porcSpike6, Img.porcSpike7};
    private int enemyCounter = 0;
    private boolean enemyComplete = false;

    /**
     * Determines how the enemy is animated.
     */
    private void enemyAnimation(){
        enemyCounter++;
        if (enemyLocation.x() > 570) {
            if (enemyCounter >= this.enemyWalking.length) enemyCounter = 0;
            currEnemy = enemyWalking[enemyCounter];
            enemyLocation = new Point(enemyLocation.x() - 10, enemyLocation.y());
        } else {
            if (enemyComplete) currEnemy = enemyIdle[enemyIdle.length - 1];
            else {
                if (enemyCounter >= this.enemyIdle.length) {
                    enemyComplete = true;
                    enemyCounter = enemyIdle.length - 1;
                }
                currEnemy = enemyIdle[enemyCounter];
            }
        }
    }




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

        // Randomizing the cloud image
        if((int)(Math.random() * 2) == 1) clouds[num] = Img.cloud0;
        else clouds[num] = Img.cloud1;

        // Randomizing the flip of the image
        if((int)(Math.random() * 2) == 1) cloudsFlipped[num] = true;
        else cloudsFlipped[num] = false;

        // Randomizing the speed of the image
        cloudsSpeed[num] = (int)(Math.random() * 4) + 1;
    }

    private Point titleLocation = new Point(10, 8);
    private Img title = Img.title;
    private int titleCounter;

    private void titleAnimation(){
        if(titleCounter++ % 4 == 0){
            if(titleLocation.y() == 8) titleLocation = new Point(titleLocation.x(), titleLocation.y() + 8);
            else titleLocation = new Point(titleLocation.x(), titleLocation.y() - 8);
        }
    }

/*------ DRAWING THE CHASE ANIMATIONS -------------------------------------------------------------------------------------------------- */

    /**
     * Determines how the sprite is animated for a chase animation.
     */
    private void spriteChaseAnimation(){
        if(++spriteCounter >= spriteWalking.length) spriteCounter = 0;
        currSprite = spriteWalking[spriteCounter];
        spriteLocation = new Point(spriteLocation.x() + 13, spriteLocation.y());
    }

    /**
     * Determines how the sprite is animated for a chase animation.
     */
    private void enemyChaseAnimation(){
        if(++enemyCounter >= enemyChase.length) enemyCounter = 0;
        currEnemy = enemyChase[enemyCounter];
        enemyLocation = new Point(enemyLocation.x() + 13, enemyLocation.y());
    }
}
