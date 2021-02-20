package edu.rice.comp610.model.strategy;

import edu.rice.comp610.model.ball.PaintObject;
import java.awt.*;

/**
 * Dead Strategy - float towards the top and stay there.
 */
public class DeadStrategy implements IUpdateStrategy {
    private static DeadStrategy ref;

    /**
     * Private Constructor - Singleton Design pattern.
     */
    private DeadStrategy() {}

    /**
     * The name of the strategy.
     * @return strategy name
     */
    public String getName() {
        return "Dead";
    }

    /**
     * Update the state of the ball
     * Dead Strategy - just float upwards.
     * @param context The ball.
     */
    public void updateState(PaintObject context) {
        Point loc = context.getLocation();
        Point vel = context.getVelocity();
        int radius = context.getRadius();

        if (loc.y - radius <= 0) return;  //only let the object float to the top and stop.

        context.setRotate(Math.PI * 1.2);
        context.setFlip(false);
        int yVel = -Math.min(Math.abs(vel.y), 5);
        context.setLocation(new Point(loc.x, loc.y + yVel));
    }

    /**
     * Public Static method Get Instance.
     * Mimic the Singleton design pattern for consistent API.
     * But in this case, just return a new instance of the class.
     * @return A strategy.
     */
    public static DeadStrategy getInstance() {
        if (ref == null) ref = new DeadStrategy();
        return ref;
    }
}
