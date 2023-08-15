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
        Point arrow = c.getRandPoint().distance(c.location());
        arrow = arrow.times(c.speed() / arrow.size());
        double size = ((Point) arrow).size();
        arrow = ((Point) arrow).times(c.speed() / size);
        c.location = c.location().add(arrow);


        checkCaught(c, game);
        checkFlipped(c, game);
    }

}


/**
 * A collectable that can move around and run away from the sprite.
 */
record EscapingCollectable() implements CollectableState{

    void findEscape(Collectable c, Game game){
        Point spriteLocation = game.getSprite().location();
        Point collectableLocation = c.location();

        double probability = 8;
        if((Math.random() * probability) != 4){
            c.setRandPoint(new Point(Math.random() * 16, Math.random() * 16));
        }
        // Collectable is on the left of the sprite
        else if(spriteLocation.x() - collectableLocation.x() > 0){
            c.setRandPoint(new Point(Math.random() * spriteLocation.x(), Math.random() * 16));
        }
        // Collectable is on the right side of the sprite
        else if(spriteLocation.x() - collectableLocation.x() <= 0){
            c.setRandPoint(new Point(Math.random() * 16, Math.random() * 16));
        }
        // Collectable is on the top of the 
        else if(spriteLocation.y() - collectableLocation.y() > 0){
            c.setRandPoint(new Point(Math.random() * 16, Math.random() * spriteLocation.y()));
        }
        else if(spriteLocation.y() - collectableLocation.y() <= 0){
             c.setRandPoint(new Point(Math.random() * 16, Math.random() * 16));
        }
        
        c.setTicks(0);
    }

    @Override
    public void ping(Collectable c, Game game){

        if (c.getTicks() == c.getRandTime()) {
            findEscape(c, game); // Sets random point for collectable to follow
        }

        // Move towards the random point that is chosen in findEscape()
        c.setTicks(c.getTicks() + 1);
        Point arrow = c.getRandPoint().distance(c.location());
        arrow = arrow.times(c.speed() / arrow.size());
        double size = ((Point) arrow).size();
        arrow = ((Point) arrow).times(c.speed() / size);


        // If the collectable has been picked up by the sprite
        checkCaught(c, game);
        checkFlipped(c, game);
}

}