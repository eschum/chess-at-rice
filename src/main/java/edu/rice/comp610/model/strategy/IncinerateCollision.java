package edu.rice.comp610.model.strategy;

import edu.rice.comp610.model.ball.Ball;
import edu.rice.comp610.model.ball.Fish;
import edu.rice.comp610.model.DispatchAdapter;
import edu.rice.comp610.model.ball.PaintObject;

/**
 * Incinerate Class - The bigger object will just make the smaller object disappear.
 */
public class IncinerateCollision implements ICollisionStrategy{
    private static IncinerateCollision ref;
    DispatchAdapter da;

    /**
     * Private constructor - implement singleton design pattern.
     */
    private IncinerateCollision(DispatchAdapter da) {
        this.da = da;
    }

    /**
     * The name of the strategy.
     * @return strategy name
     */
    public String getName() { return "Incinerate"; }

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
            //Compare sizes and destroy the smaller object.
            if (contextOne.getRadius() >= contextTwo.getRadius()) {
                da.removeSingleObject(contextTwo);
            }
        }
    }

    /**
     * Static method to return the singleton class.
     * @return - the singleton instance of the class.
     */
    public static IncinerateCollision getInstance(DispatchAdapter da) {
        if (ref == null) ref = new IncinerateCollision(da);
        return ref;
    }

}
