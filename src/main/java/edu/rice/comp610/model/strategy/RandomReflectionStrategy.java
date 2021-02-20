package edu.rice.comp610.model.strategy;

import edu.rice.comp610.model.ball.PaintObject;
import java.util.Random;
import java.awt.*;

/**
 * Random Reflection Strategy - ball will randomly invert x or y (or both) components of its velocity
 * after an interval of time has passed (5 clock ticks).
 */
public class RandomReflectionStrategy implements IUpdateStrategy {
    private final static int change_interval = 5;  //time to wait before changing colors.
    private int currChanges = change_interval;
    private static final Random rand = new Random();

    /**
     * Private constructor - follow the singleton design pattern.
     */
    private RandomReflectionStrategy() {}

    /**
     * The name of the strategy.
     * @return strategy name
     */
    public String getName() {
        return "RandomReflection";
    }

    /**
     * Update the state of the ball.
     * @param context The ball.
     */
    public void updateState(PaintObject context) {
        //Detect Collisions and randomly invert the ball velocity.
        currChanges--;
        boolean collide = context.detectCollision();

        //If enough time has passed, potentially randomly move.
        if (currChanges == 0) {
            currChanges = change_interval;
            if (!collide) {
                //If the interval has been reached and there is no collision, then change directions.
                Point currVel = context.getVelocity();
                if (rand.nextBoolean()) {
                    currVel.x = -currVel.x;
                    if (rand.nextBoolean()) currVel.y = -currVel.y;
                } else {
                    currVel.y = -currVel.y;
                    if (rand.nextBoolean()) currVel.x = -currVel.x;
                }
                context.setVelocity(currVel);
            }
        }

        //Update position with velocity
        Point loc = context.getLocation();
        Point vel = context.getVelocity();
        context.setLocation(new Point(loc.x + vel.x, loc.y + vel.y));

        //Update the image angle
        context.updateImageAngle();
    }

    /**
     * Static class accessor method.
     * Mimics the singleton design pattern; but just creates a new instance each time.
     * @return - a RandomReflectionStrategy.
     */
    public static RandomReflectionStrategy getInstance() {
        return new RandomReflectionStrategy();
    }

}
