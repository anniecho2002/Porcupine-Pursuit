package main;

/**
 * An interface that holds the different states an enemy can be in.
 */
public interface CollectableState {

    /**
     * Ping determines the collectable's next move and redraws.
     * @param enemy The collectable in the state.
     * @param game  The current game.
     */
    void ping(Collectable c, Game game);

    /**
     * Checks if the collectable has been caught by the sprite.
     */
    default void checkCaught(Collectable c, Game game){
        Point userArrow = game.getSprite().location().distance(c.location);
        double userSize = ((Point) userArrow).size();
        if (userSize < game.getSprite().closeness()) {
            game.getSprite().grow();
            game.remove(c);
        }
    }

    /**
     * Checks if the collectable should be drawn flipped.
     * Not applicable for the StationaryCollectable.
     */
    default void checkFlipped(Collectable c, Game game){
        double distance = c.getRandPoint().x() - c.location().x();
        c.flipped = (distance > 0) ? false : true;
    }

    /*
     * Moves the collectable towards the given destination.
     */
    default void move(Collectable c, Game game, Point destination){
        Point arrow = destination.distance(c.location());
        arrow = arrow.times(c.speed() / arrow.size());
        double size = ((Point) arrow).size();
        arrow = ((Point) arrow).times(c.speed() / size);
        c.location = c.location().add(arrow);
    }
}


/**
 * A collectable that cannot move around and stays in its original location.
 */
record StationaryCollectable() implements CollectableState{
    @Override
    public void ping(Collectable c, Game game){ checkCaught(c, game); }
}


/**
 * A collectable that can move around and run around randomly.
 */
record MovingCollectable() implements CollectableState{

    double findDistance(Point a, Point b){
        double distX = Math.abs(a.x() - b.x());
        double distY = Math.abs(a.y() - b.y());
        return (distX + distY) / 2;
    }

    @Override
    public void ping(Collectable c, Game game){
        
        // Change point to walk to after time.
        if (c.getTicks() == c.getRandTime() || findDistance(c.location, c.getRandPoint()) < 3) {
            c.setRandPoint(new Point(Math.random() * 16, Math.random() * 16)); // Sets random point for collectable to follow
            c.setTicks(0);
        }
        c.setTicks(c.getTicks() + 1);
        move(c, game, c.getRandPoint());
        checkCaught(c, game);
        checkFlipped(c, game);
    }

}


/**
 * A collectable that can move around and run away from the sprite.
 */
record EscapingCollectable() implements CollectableState{

    double findDistance(Point a, Point b) {
        double distX = a.x() - b.x();
        double distY = a.y() - b.y();
        return Math.sqrt(distX * distX + distY * distY);
    }


    void findEscape(Collectable c, Game game){
        Point spriteLocation = game.getSprite().location();
        double distanceThreshold = 5.0; // Adjust this threshold as needed
        Point randomPoint;
        do {
            randomPoint = new Point(Math.random() * 16, Math.random() * 16);
        } while (findDistance(randomPoint, spriteLocation) < distanceThreshold);
        c.setRandPoint(randomPoint);
    }

    void adjustSpeed(Collectable c, Game game){
        if(findDistance(c.location, c.getRandPoint()) < 6) c.setSpeed(0.12d);
        else c.setSpeed(0.09d);
    }

    @Override
    public void ping(Collectable c, Game game){

        if (c.getTicks() == c.getRandTime() || findDistance(c.location, c.getRandPoint()) < 3) {
            findEscape(c, game); // Sets random point for collectable to follow
            c.setTicks(0);
        }
        c.setTicks(c.getTicks() + 1);
        adjustSpeed(c, game);
        move(c, game, c.getRandPoint());
        checkCaught(c, game);
        checkFlipped(c, game);
    }

}