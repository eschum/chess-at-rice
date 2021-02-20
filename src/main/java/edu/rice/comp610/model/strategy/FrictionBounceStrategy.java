package edu.rice.comp610.model.strategy;

import edu.rice.comp610.model.ball.PaintObject;
import java.awt.*;

/**
 * Friction Bounce Strategy - On each bounce, the ball will lose 20% of it's velocity.
 */
public class FrictionBounceStrategy implements IUpdateStrategy {
    private static FrictionBounceStrategy ref;

    /**
     * Private constructor - singleton design pattern.
     */
    private FrictionBounceStrategy() { }

    /**
     * The name of the strategy.
     * @return strategy name
     */
    public String getName() {
        return "FrictionBounce";
    }

    /**
     * Update the state of the ball.
     * @param context The ball.
     */
    public void updateState(PaintObject context) {
        //Detect Collisions and compute the velocity with friction effects.
        computeFrictionVelocity(context);

        //Update position with newly calculated velocity.
        Point loc = context.getLocation();
        Point vel = context.getVelocity();
        context.setLocation(new Point(loc.x + vel.x, loc.y + vel.y));

        //Update the image angle angle
        context.updateImageAngle();
    }

    /**
     * Method Compute Friction Velocity
     * Helper function to detect collisions and apply a friction factor to each bounce the ball takes.
     * @param context - Ball object that we are looking to move.
     */
    public void computeFrictionVelocity(PaintObject context) {
        boolean collide = context.detectCollision();
        if (collide) {
            Point vel = context.getVelocity();
            vel.x = (int) Math.floor(vel.x * 0.8);
            vel.y = (int) Math.floor(vel.y * 0.8);
            context.setVelocity(vel);
        }
    }

    /**
     * Singleton design pattern - static accessor of the class.
     * @return the singleton instance of the class.
     */
    public static FrictionBounceStrategy getInstance() {
        if (ref == null) ref = new FrictionBounceStrategy();
        return ref;
    }

}