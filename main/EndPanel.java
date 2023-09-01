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
        if(!win){
            spriteLocation = new Point(width + 30, 533);
            enemyLocation = new Point(width + 90, 485);
        }
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
        if(!win){
            scaledPaint(g2d, 0.35, currEnemy.image, enemyLocation, true);
            scaledPaint(g2d, 0.4, currSprite.image, spriteLocation, true);
        }
        else{
            scaledPaint(g2d, 0.6, currSprite.image, spriteLocation, false);
            scaledPaint(g2d, 0.5, collectable.image, collectableLocation, false);
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
        cloudAnimation();
        if(this.win){
            spriteWinAnimation();
            collectableWinAnimation();
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

    private Img[] getLookArray(int repeat){
        Img[] spriteIdle = { Img.foxIdle0, Img.foxIdle1, Img.foxIdle2, Img.foxIdle3, Img.foxIdle4 };
        Img[] spriteLook = { Img.foxLook0, Img.foxLook1, Img.foxLook2, Img.foxLook3, Img.foxLook4, Img.foxLook5, Img.foxLook6, Img.foxLook7, Img.foxLook8, Img.foxLook9, Img.foxLook10, Img.foxLook11, Img.foxLook12, Img.foxLook13 };

        int totalLength = spriteIdle.length * repeat + spriteLook.length;
        Img[] resultArray = new Img[totalLength];

        int index = 0;
        for (int i = 0; i < repeat; i++) {
            for (Img img : spriteIdle) {
                resultArray[index++] = img;
            }
        }
        for (Img img : spriteLook) {
            resultArray[index++] = img;
        }
        return resultArray;

    }

    // Fields for sprite animation
    private Point spriteLocation = new Point(-120, 533);
    private Img currSprite = Img.foxRun0;
    private Img[] spriteWalking = {Img.foxRun0, Img.foxRun1, Img.foxRun2, Img.foxRun3, Img.foxRun4, Img.foxRun5, Img.foxRun6}; 
    private Img[] spriteLook = getLookArray(4);
    private int spriteCounter = 0;


    // Fields for enemy animation
    private Point enemyLocation = new Point(890, 485);
    private Img currEnemy = Img.porcWalk0;
    private Img[] enemyChase = {Img.porcWalk0, Img.porcWalk1, Img.porcWalk2, Img.porcWalk3, Img.porcWalk4,
                                Img.porcSpike0, Img.porcSpike1, Img.porcSpike2, Img.porcSpike3, Img.porcSpike4, Img.porcSpike5, Img.porcSpike6, Img.porcSpike7};
    private int enemyCounter = 0;

    
    // Fields for collectable animation
    private Point collectableLocation = new Point(-15, 565);
    private Img collectable = Img.orange;


    /**
     * Determines how the sprite is animated for a win animation.
     * Sprite is big and chases the collectable across the screen.
     */
    private void spriteWinAnimation(){
        spriteCounter++;
        int maxSpriteCounter = spriteLocation.x() < 390 ? this.spriteWalking.length : this.spriteLook.length;
        if (spriteCounter >= maxSpriteCounter) spriteCounter = 0;
        if (spriteLocation.x() < 390) {
            currSprite = spriteWalking[spriteCounter];
            spriteLocation = new Point(spriteLocation.x() + 15, spriteLocation.y());
        } else {
            currSprite = spriteLook[spriteCounter];
        }
    }

    /**
     * Determines how the collectable is animated for a win animation.
     */
    private void collectableWinAnimation(){
        collectableLocation = new Point(collectableLocation.x() + 17, collectableLocation.y());
    }

    
    /**
     * Determines how the sprite is animated for a lose animation.
     */
    private void spriteLoseAnimation(){
        if(++spriteCounter >= spriteWalking.length) spriteCounter = 0;
        currSprite = spriteWalking[spriteCounter];
        spriteLocation = new Point(spriteLocation.x() - 13, spriteLocation.y());

    }

    /**
     * Determines how the enemy is animated for a lose animation.
     */
    private void enemyLoseAnimation(){
        if(++enemyCounter >= enemyChase.length) enemyCounter = 0;
        currEnemy = enemyChase[enemyCounter];
        enemyLocation = new Point(enemyLocation.x() +-13, enemyLocation.y());
    }

}
