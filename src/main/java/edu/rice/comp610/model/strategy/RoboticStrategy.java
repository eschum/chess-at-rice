package edu.rice.comp610.model.strategy;

import edu.rice.comp610.model.ball.PaintObject;
import java.awt.*;

/**
 * Dart Strategy - the object moves and then evasively stops, as if being dangerously pursued.
 */
public class RoboticStrategy implements IUpdateStrategy {
    int timeTarget = 3;
    int currCount = timeTarget;
    boolean moving = true;

    /**
     * Private Constructor - Singleton Design pattern.
     */
    private RoboticStrategy() {}
    /**
     * The name of the strategy.
     * @return strategy name
     */
    public String getName() {
        return "Dart";
    }

    /**
     * Update the state of the ball
     * Dart Strategy - move and then stop.
     * @param context The ball.
     */
    public void updateState(PaintObject context) {
        context.detectCollision();  //Detect Collisions

        currCount -= 1;
        if (currCount < 0) {
            moving = !moving;
            currCount = timeTarget;
        }
        if (moving) {
            //Move with our velocity if the time has arrived.
            Point loc = context.getLocation();
            Point vel = context.getVelocity();

            //Change x direction if odd time interval.

            //Update x and y position based on velocity
            context.setLocation(new Point(loc.x + vel.x, loc.y + vel.y));

            //Update image angle - if required.
            context.updateImageAngle();
        }
    }

    /**
     * Public Static method Get Instance.
     * Mimic the singleton design pattern, but just return a single strategy.
     * @return the singleton Horizontal Strategy.
     */
    public static RoboticStrategy getInstance() {
        return new RoboticStrategy();
    }
}
