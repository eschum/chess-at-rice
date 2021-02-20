package edu.rice.comp610.model.strategy;

import edu.rice.comp610.model.DispatchAdapter;
import edu.rice.comp610.model.ball.Ball;
import edu.rice.comp610.model.ball.Fish;
import edu.rice.comp610.model.ball.PaintObject;

/**
 * Explode Class - Upon collision, both objects will explode.
 */
public class ExplodeCollision implements ICollisionStrategy{
    private static ExplodeCollision ref;
    DispatchAdapter da;

    /**
     * Private constructor - implement singleton design pattern.
     */
    private ExplodeCollision(DispatchAdapter da) {
        this.da = da;
    }

    /**
     * The name of the strategy.
     * @return strategy name
     */
    public String getName() { return "Explode"; }

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
            //Just remove both objects because they have "exploded"
            da.removeSingleObject(contextOne);
            da.removeSingleObject(contextTwo);
        }
    }

    /**
     * Static method to return the singleton class.
     * @return - the singleton instance of the class.
     */
    public static ExplodeCollision getInstance(DispatchAdapter da) {
        if (ref == null) ref = new ExplodeCollision(da);
        return ref;
    }

}
