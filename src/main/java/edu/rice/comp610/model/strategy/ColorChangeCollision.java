package edu.rice.comp610.model.strategy;

import edu.rice.comp610.model.ball.Ball;
import edu.rice.comp610.model.ball.Fish;
import edu.rice.comp610.model.ball.PaintObject;

import java.awt.*;

/**
 *  Color change collision. Object will change colors upon collision.
 */
public class ColorChangeCollision implements ICollisionStrategy{
    private static ColorChangeCollision ref;

    /**
     * Private constructor - implement singleton design pattern.
     */
    private ColorChangeCollision() { }

    /**
     * The name of the strategy.
     * @return strategy name
     */
    public String getName() { return "ColorChange"; }

    /**
     * Check for collision (In whatever way the strategy specifies)
     * and update positions accordingly.
     */
    public void updateState(Fish contextOne, Fish contextTwo) { action(contextOne, contextTwo); }

    /**
     * Check for collision (In whatever way the strategy specifies)
     * and update positions accordingly.
     */
    public void updateState(Ball contextOne, Ball contextTwo) { action(contextOne, contextTwo); }

    /**
     * Check for collision (In whatever way the strategy specifies)
     * and update positions accordingly.
     */
    public void updateState(Ball contextOne, Fish contextTwo) { action(contextOne, contextTwo); }

    /**
     * Check for collision (In whatever way the strategy specifies)
     * and update positions accordingly.
     */
    public void updateState(Fish contextOne, Ball contextTwo) { action(contextOne, contextTwo); }

    /**
     * Helper method - delegated to take action from the two contexts.
     * Will delegate state and behavior updates through dynamic dispatch.
     * @param contextOne - First Paint World Object.
     * @param contextTwo - Second Paint World Object
     */
    public void action(PaintObject contextOne, PaintObject contextTwo) {
        //Take action if there is a collision.
        if (contextOne.isCollision(contextTwo)) {
            //Ask the object to change it's color. The object knows how to change color.
            //Will be evaluated by dynamic dispatch
            contextOne.changeColor();

            //And then, bounce.
            Point v1 = contextOne.getVelocity();
            Point l1 = contextOne.getLocation();
            Point l2 = contextTwo.getLocation();
            Point newVel = new Point(0, 0);
            if (l1.x < l2.x) {
                newVel.x = -Math.abs(v1.x);
            } else {
                newVel.x = Math.abs(v1.x);
            }

            if (l1.y < l2.y) {
                newVel.y = -Math.abs(v1.y);
            } else {
                newVel.y = Math.abs(v1.y);
            }

            contextOne.setVelocity(newVel);
        }
    }

    /**
     * Static method to return the singleton class.
     * @return - the singleton instance of the class.
     */
    public static ColorChangeCollision getInstance() {
        if (ref == null) ref = new ColorChangeCollision();
        return ref;
    }

}
