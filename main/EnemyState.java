package main;

import imgs.Img;

/**
 * An interface that holds the different states an enemy can be in.
 */
public interface EnemyState {

    /**
     * Ping determines the enemy's next move and redraws.
     * 
     * @param enemy The enemy in the state.
     * @param game  The current game.
     */
    void ping(Enemy enemy, Game game);
}

/**
 * Enemy follows the sprite in AwakeState.
 * If the sprite is too far away, enemy will go to SleepState.
 * If the sprite is close, enemy will go into SpikeState.
 */
record AwakeState() implements EnemyState {

    // Static fields for animation.
    static int wait = 3;
    static int currWait = 0;
    static int currIndex = 0;

    @Override
    public void ping(Enemy enemy, Game game) {

        // Determining which images to use in animation.
        Img[] enemyImages = enemy.walkImages;
        if (currIndex >= enemyImages.length)
            currIndex = 0;
        enemy.currentImg = enemyImages[currIndex];
        currWait++;
        if (currWait >= wait) {
            currWait = 0;
            currIndex++;
        }

        // Making the enemy follow the sprite.
        Point arrow = game.getSprite().location().distance(enemy.location);
        double size = ((Point) arrow).size();
        arrow = ((Point) arrow).times(enemy.speed() / size);
        enemy.location = enemy.location.add(arrow);

        // Determining which way to draw the enemy.
        double distance = game.getSprite().location().x() - enemy.location().x();
        enemy.flipped = (distance > 0) ? false : true;

        // Determining which state to move into next.
        if (size > 4)
            enemy.state = new SleepState();
        else if (size <= 2)
            enemy.state = new SpikeState();
    }
}

/**
 * Enemy stays in the same spot and sleeps when the sprite is too far.
 * If the sprite is close, it will awake and go into AwakeState or SpikeState.
 */
record SleepState() implements EnemyState {

    // Static fields for animation.
    static int wait = 3;
    static int currWait = 0;
    static int currIndex = 0;

    @Override
    public void ping(Enemy enemy, Game game) {

        // Determining which images to use in animation.
        Img[] enemyImages = enemy.sleepImages;
        if (currIndex >= enemyImages.length) {
            currIndex = enemyImages.length - 1;
        }
        enemy.currentImg = enemyImages[currIndex];
        currWait++;
        if (currWait >= wait) {
            currWait = 0;
            currIndex++;
        }

        // Determining if the enemy will wake up.
        var arrow = game.getSprite().location().distance(enemy.location);
        double size = ((Point) arrow).size();

        // Determining which state to move into next.
        if (size < 4) {
            enemy.state = enemy.originalState;
            currIndex = 0;
        }
        if (size <= 2) {
            enemy.state = new SpikeState();
            currIndex = 0;
        }
    }
}

/**
 * 
 */
record SpikeState() implements EnemyState {

    // Static fields for animation.
    static int wait = 3;
    static int currWait = 0;
    static int currIndex = 0;

    @Override
    public void ping(Enemy enemy, Game game) {

        // Determining which images to use in animation.
        Img[] enemyImages = enemy.spikeImages;
        if (currIndex >= enemyImages.length)
            currIndex = 0;
        enemy.currentImg = enemyImages[currIndex];
        currWait++;
        if (currWait >= wait) {
            currWait = 0;
            currIndex++;
        }

        // Making the enemy follow the sprite.
        Point arrow = game.getSprite().location().distance(enemy.location);
        double size = ((Point) arrow).size();
        arrow = ((Point) arrow).times(enemy.speed() / size);
        enemy.location = enemy.location.add(arrow);

        // Determining which way to draw the enemy.
        double distance = game.getSprite().location().x() - enemy.location().x();
        enemy.flipped = (distance > 0) ? false : true;

        // Determining which state to move into next.
        if (size > 2) {
            // If we are too far away from the sprite, revert back into old state.
            if (enemy.originalState instanceof AwakeState) enemy.state = new AwakeState();
            else if (enemy.originalState instanceof RoamingState) enemy.state = new RoamingState();
            else if(enemy.originalState instanceof FollowState) enemy.state = new FollowState();
        }
        if (size < game.getSprite().closeness()) {
            game.getSprite().shrink();
            game.remove(enemy);
        }
    }
}

/**
 * An enemy that wanders around unless close to the sprite.
 */
record RoamingState() implements EnemyState {

    // Static fields for animation and determining where to walk to.
    static int wait = 3;
    static int currWait = 0;
    static int currIndex = 0;
    static int ticks;
    static Point randPoint = new Point(Math.random() * 16, Math.random() * 16);

    
    double findDistance(Point a, Point b){
        double distX = Math.abs(a.x() - b.x());
        double distY = Math.abs(a.y() - b.y());
        return (distX + distY) / 2;
    }

    @Override
    public void ping(Enemy enemy, Game game) {

        // Determining which images to use in animation.
        Img[] enemyImages = enemy.walkImages;
        if (currIndex >= enemyImages.length)
            currIndex = 0;
        enemy.currentImg = enemyImages[currIndex];
        currWait++;
        if (currWait >= wait) {
            currWait = 0;
            currIndex++;
        }

        // Change point to walk to after time.
        if (ticks++ == 50 || findDistance(enemy.location(), randPoint) < 3) {
            randPoint = new Point(Math.random() * 16, Math.random() * 16); // Sets random point for monster to follow
            ticks = 0;
        }
        Point arrow = randPoint.distance(enemy.location());
        arrow = arrow.times(enemy.speed() / arrow.size());

        double size = ((Point) arrow).size();
        Point userArrow = game.getSprite().location().distance(enemy.location);
        double userSize = ((Point) userArrow).size();
        arrow = ((Point) arrow).times(enemy.speed() / size);
        enemy.location = enemy.location().add(arrow);

        // Determining which way to draw the enemy.
        double distance = randPoint.x() - enemy.location().x();
        enemy.flipped = (distance > 0) ? false : true;

        // Determining which state to move into next.
        if (userSize <= 2) {
            enemy.state = new SpikeState();
            currIndex = 0;
        }
        if (userSize < game.getSprite().closeness())
            game.onGameOver();
    }
}


/**
 * An enemy that follows the sprite always.
 * Similar to SpikeState, but does not change out.
 */
record FollowState() implements EnemyState {

    // Static fields for animation.
    static int wait = 3;
    static int currWait = 0;
    static int currIndex = 0;

            public void ping(Enemy enemy, Game game) {

        // Determining which images to use in animation.
        Img[] enemyImages = enemy.walkImages;
        if (currIndex >= enemyImages.length)
            currIndex = 0;
        enemy.currentImg = enemyImages[currIndex];
        currWait++;
        if (currWait >= wait) {
            currWait = 0;
            currIndex++;
        }

        // Making the enemy follow the sprite.
        Point arrow = game.getSprite().location().distance(enemy.location);
        double size = ((Point) arrow).size();
        arrow = ((Point) arrow).times(enemy.speed() / size);
        enemy.location = enemy.location.add(arrow);

        // Determining which way to draw the enemy.
        double distance = game.getSprite().location().x() - enemy.location().x();
        enemy.flipped = (distance > 0) ? false : true;

        // Determining which state to move into next.
        if (size <= 2) enemy.state = new SpikeState();
    }
}
