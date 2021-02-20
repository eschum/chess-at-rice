package edu.rice.comp610.model.strategy;

import edu.rice.comp610.model.ball.PaintObject;
import java.awt.*;

/**
 * Hopscotch Strategy - Ball will move only vertical, then only horizontal, and then repeat.
 */
public class HopScotchStrategy implements IUpdateStrategy {
    private int currTime = 0;
    private boolean horizontal = true;  //start out horizontal

    /**
     * Private Constructor - Singleton Design pattern.
     */
    private HopScotchStrategy() {}

    /**
     * The name of the strategy.
     * @return strategy name
     */
    public String getName() {
        return "Hopscotch";
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
        if (horizontal) context.setLocation(new Point(loc.x + vel.x, loc.y));
        else context.setLocation(new Point(loc.x, loc.y + vel.y));
        if (--currTime < 0) {
            horizontal = !horizontal;
            currTime = 5; //reset the action timer.
        }

        //Update image angle - if required.
        context.updateImageAngle();
    }

    /**
     * Public Static method Get Instance.
     * Mimic the Singleton design pattern for consistent API.
     * But in this case, just return a new instance of the class.
     * @return A Hopscotch strategy.
     */
    public static HopScotchStrategy getInstance() {
        return new HopScotchStrategy();
    }
}
