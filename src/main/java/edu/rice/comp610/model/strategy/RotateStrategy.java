package edu.rice.comp610.model.strategy;

import edu.rice.comp610.model.DispatchAdapter;
import edu.rice.comp610.model.ball.PaintObject;
import java.awt.*;

/**
 * Rotate Strategy - Object will rotate around the canvas center.
 * Using singleton design pattern
 */
public class RotateStrategy implements IUpdateStrategy {
    private static RotateStrategy ref;

    /**
     * Private constructor - in implementation of the singleton design pattern.
     */
    private RotateStrategy () {}

    /**
     * The name of the strategy.
     * @return strategy name
     */
    public String getName() {
        return "Rotate";
    }

    /**
     * Update the state of the ball.
     * For rotate ball, use the x velocity as the angular velocity.
     * @param context The ball.
     */
    public void updateState(PaintObject context) {
        //Get current ball location
        int currX = context.getLocation().x;
        int currY = context.getLocation().y;
        double theta;

        //Set the rotation point; calculate radius, and current angle.
        Point dims = DispatchAdapter.getCanvasDims();
        int rotX = dims.x / 2;
        int rotY = dims.y / 2;
        double radius = Math.hypot(rotX - currX, rotY - currY);
        theta = context.getTheta();

        //theta = theta == 0 ? Math.toDegrees(Math.atan2(rotY - currY, rotX - currX)) : theta;

        //Detect Collisions and compute the velocity
        //We need to flip the angular velocity if either a side or top/bottom wall is hit.
        //Get the x velocity (we'll use that as angular velocity) first, then flip that if a collision happens.
        //then also reset the velocity.
        Point vel = context.getVelocity();
        if (detectCollisionRadial(context, dims, vel)) vel.x = -vel.x;

        //Set the ball's velocity so it will carry this forward.
        context.setVelocity(vel);

        //Increment/decrement the angle. If velocity is positive, then the angle will be increase.
        //If velocity is negative, then the angle will decrease (and rotation will go the other way)
        if (vel.x > 0) {
            //If theta will be increasing
            if (theta + vel.x <= 360) theta = theta + vel.x;
            else theta = vel.x - (360 - theta);
            context.setRotate(Math.toRadians(theta + 90));
            context.setFlip(theta > 0 && theta <= 180);   //Boolean Zen!
        } else {
            //Otherwise, if theta is decreasing
            if (theta + vel.x >= 0) theta += vel.x;
            else theta = theta + vel.x + 360;
            //Set the fish image rotation and inversion based on swimming a "backward" circle
            context.setRotate(Math.toRadians(theta - 90));
            context.setFlip(theta > 180);
        }

        //Update object rotation angle (around central point)
        context.setTheta(theta);

        //Reconstruct the ball's new position based on the new angle.
        currX = (int) (rotX + radius * Math.cos(Math.toRadians(theta)));
        currY = (int) (rotY + radius * Math.sin(Math.toRadians(theta)));
        context.setLocation(new Point(currX, currY));
    }

    /**
     * detectCollisionRadial: Helper function to detect collision only, but don't change the ball velocity.
     * Don't re-call the context accessor methods for the parameters we have already accessed in the updateState
     * method, because that would be wasteful. Just pass them.
     * @param ball - Ball object context.
     * @param dims - Dimensions of the canvas.
     * @param vel - ball velocity
     * @return boolean indicating whether or not a collision has occurred.
     */
    private boolean detectCollisionRadial(PaintObject ball, Point dims, Point vel) {
        Point loc = ball.getLocation();
        int radius = ball.getRadius();
        return ((loc.x + radius >= dims.x && vel.x > 0) || (loc.x - radius <= 0 && vel.x < 0)) ||
                ((loc.y - radius <= 0) || (loc.y + radius >= dims.y));
    }

    /**
     * Return the static / single instance of the class.
     * Using the Singleton Design Pattern
     * @return - the singleton class instance.
     */
    public static RotateStrategy getInstance() {
        if (ref == null) ref = new RotateStrategy();
        return ref;
    }
}