package edu.rice.comp610.model.strategy;

import edu.rice.comp610.model.ball.PaintObject;

import java.awt.*;

/**
 * Color Change Strategy - Change Colors after an elapsed period of time.
 */
public class ColorChangeStrategy implements IUpdateStrategy {
    private int currTime = 0;

    /**
     * Private Constructor - Singleton Design pattern.
     */
    private ColorChangeStrategy() {}

    /**
     * The name of the strategy.
     * @return strategy name
     */
    public String getName() {
        return "ColorChange";
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

        //Change color if time has arrived.
        if (--currTime < 0) {
            context.changeColor();
            currTime = 5; //reset the action timer.
        }

        //Then just move as normal.
        context.setLocation(new Point(loc.x + vel.x, loc.y + vel.y));


        //Update image angle - if required.
        context.updateImageAngle();
    }


    /**
     * Public Static method Get Instance.
     * Mimic the Singleton design pattern for consistent API.
     * But in this case, just return a new instance of the class.
     * @return A Color Change strategy.
     */
    public static ColorChangeStrategy getInstance() {
        return new ColorChangeStrategy();
    }
}
