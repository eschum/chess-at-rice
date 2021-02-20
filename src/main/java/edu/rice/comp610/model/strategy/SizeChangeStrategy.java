package edu.rice.comp610.model.strategy;

import edu.rice.comp610.model.ball.PaintObject;
import java.awt.*;

/**
 * Size Change Strategy - ball will change size within the bounds of min and max radius/scale.
 */
public class SizeChangeStrategy implements IUpdateStrategy {
    private final static int maxRadius = 80;
    private final static int minRadius = 10;
    private final static double maxScale = 1.0;
    private final static double minScale = 0.1;
    private boolean increasing = true;

    /**
     * Private Constructor - Singleton Design pattern.
     */
    private SizeChangeStrategy() {}

    /**
     * The name of the strategy.
     * @return strategy name
     */
    public String getName() {
        return "SizeChange";
    }

    /**
     * Update the state of the ball.
     * @param context The ball.
     */
    public void updateState(PaintObject context) {
        //Detect Collisions and update velocity appropriately
        context.detectCollision();

        //Update position with velocity
        Point loc = context.getLocation();
        Point vel = context.getVelocity();
        context.setLocation(new Point(loc.x + vel.x, loc.y + vel.y));

        //Get the radius and update it, within the bounds of min and max radius.
        int currRadius = context.getRadius();
        double currScale = context.getScale();
        if (increasing) {
            if (currRadius >= maxRadius) {
                increasing = false;
            } else {
                context.setRadius(currRadius + 5);
            }
            if (currScale >= maxScale) {
                increasing = false;
            } else {
                context.setScale(0.05);
            }

        } else {
            //if the status is currently decreasing.
            if (currRadius <= minRadius) {
                increasing = true;
            } else {
                context.setRadius(currRadius - 10);
            }
            if (currScale <= minScale) {
                increasing = true;
            } else {
                context.setScale(-0.05);
            }

        }

        //Update image radius for accurate collision detection.
        context.updateRadius();

        //Update image rendering angle.
        context.updateImageAngle();
    }

    /**
     * Public Static method Get Instance.
     * Mimic the Singleton design pattern for consistent API.
     * Really just returns a new object for each call.
     * @return the singleton strategy
     */
    public static SizeChangeStrategy getInstance() {
        return new SizeChangeStrategy();
    }
}
