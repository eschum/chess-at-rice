package edu.rice.comp610.model.strategy;

import edu.rice.comp610.model.ball.Ball;
import edu.rice.comp610.model.ball.Fish;
import edu.rice.comp610.model.ball.PaintObject;
import java.awt.*;

/**
 *  Fast Object transfers some (50%) of it's energy (velocity) to the slow object.
 */
public class ExcitationCollision implements ICollisionStrategy{
    private static ExcitationCollision ref;

    /**
     * Private constructor - implement singleton design pattern.
     */
    private ExcitationCollision() { }

    /**
     * The name of the strategy.
     * @return strategy name
     */
    public String getName() { return "Excitation"; }

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
     * @param contextOne - First Paint World Object.
     * @param contextTwo - Second Paint World Object
     */
    public void action(PaintObject contextOne, PaintObject contextTwo) {
        //Take action if there is a collision.
        if (contextOne.isCollision(contextTwo)) {
            //Add speed.
            contextOne.addSpeed(3);

            //And then just collide away.
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
    public static ExcitationCollision getInstance() {
        if (ref == null) ref = new ExcitationCollision();
        return ref;
    }

}
