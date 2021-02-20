package edu.rice.comp610.model.strategy;

import edu.rice.comp610.model.ball.PaintObject;
import java.awt.*;

/**
 * Diagonal Strategy - Ball will move at an angle, honoring vertical and horizontal component velocity.
 */
public class DiagonalStrategy implements IUpdateStrategy {
    private static DiagonalStrategy ref;

    /**
     * Private Constructor - Singleton Design pattern.
     */
    private DiagonalStrategy() {}

    /**
     * The name of the strategy.
     * @return strategy name
     */
    public String getName() {
        return "Diagonal";
    }

    /**
     * Update the state of the ball
     * Diagonal Strategy - Increment position by the vertical and horizontal velocities.
     * @param context The ball.
     */
    public void updateState(PaintObject context) {
        context.detectCollision();  //Detect Collisions

        //Finally, update position by moving according to the velocity.
        Point loc = context.getLocation();
        Point vel = context.getVelocity();

        //Update x and y position based on velocity
        context.setLocation(new Point(loc.x + vel.x, loc.y + vel.y));

        //Update image angle - if required.
        context.updateImageAngle();
    }

    /**
     * Public Static method Get Instance.
     * Return the static / only instance of the class.
     * Following the Singleton design patter.
     * @return the singleton Horizontal Strategy.
     */
    public static DiagonalStrategy getInstance() {
        if (ref == null) ref = new DiagonalStrategy();
        return ref;
    }
}
