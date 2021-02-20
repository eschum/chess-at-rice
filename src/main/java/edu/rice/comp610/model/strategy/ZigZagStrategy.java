package edu.rice.comp610.model.strategy;

import edu.rice.comp610.model.ball.PaintObject;
import java.awt.*;

/**
 * Zig Zag - Object will move in a zig-zag pattern
 */
public class ZigZagStrategy implements IUpdateStrategy {
    private int currChanges = 3;    //Starting value for time to wait before zig-zag action. Will be random later.

    /**
     * Private Constructor - Singleton Design pattern.
     */
    private ZigZagStrategy() { }

    /**
     * The name of the strategy.
     * @return strategy name
     */
    public String getName() {
        return "ZigZag";
    }

    /**
     * Update the state of the fish
     * Fish will swim in a zig-zag pattern.
     * @param context The ball.
     */
    public void updateState(PaintObject context) {
        context.detectCollision();  //Detect Collisions

        //Finally, update position by moving according to the velocity.
        Point loc = context.getLocation();
        Point vel = context.getVelocity();

        currChanges--;
        if (currChanges <= 0) {
            currChanges = 3; //rand.nextInt(10 - 3) + 3;
            vel.x = -vel.x;
        }

        context.setLocation(new Point(loc.x + vel.x, loc.y + vel.y));
        context.setVelocity(vel);   //Remember we need to explicitly set the velocity. getVelocity returns a copy.

        //Update the fish angle to swim at.
        context.updateImageAngle();
    }

    /**
     * Public Static method Get Instance.
     * Mimic the Singleton design pattern for consistent API.
     * Really just returns a new object for each call.
     * @return the singleton strategy
     */
    public static ZigZagStrategy getInstance() {
        return new ZigZagStrategy();
    }
}
