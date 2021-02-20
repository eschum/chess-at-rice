package edu.rice.comp610.model.strategy;

import edu.rice.comp610.model.ball.Ball;
import edu.rice.comp610.model.ball.Fish;
import edu.rice.comp610.model.ball.PaintObject;

import java.awt.*;

public class ChaseCollision implements ICollisionStrategy{
    private static ChaseCollision ref;

    /**
     * Private constructor - implement singleton design pattern.
     */
    private ChaseCollision() {}

    /**
     * The name of the strategy.
     * @return strategy name
     */
    public String getName() { return "Chase"; }

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
     * Check for collision (In whatever way the strategy specifies)
     * and update positions accordingly.
     */
    public void action(PaintObject contextOne, PaintObject contextTwo) {
        //Take action if there is a collision.
        if (contextOne.isCollision(contextTwo) && contextOne.getRadius() < contextTwo.getRadius()) {
            //Update velocity of one object to the exact same of the other object.
            Point v2 = contextTwo.getVelocity();
            contextOne.setVelocity(new Point (v2.x, v2.y));
        }
    }

    /**
     * Static method to return the singleton class.
     * @return - the singleton instance of the class.
     */
    public static ChaseCollision getInstance() {
        if (ref == null) ref = new ChaseCollision();
        return ref;
    }

}
