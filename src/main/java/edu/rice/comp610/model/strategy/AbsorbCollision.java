package edu.rice.comp610.model.strategy;

import edu.rice.comp610.model.DispatchAdapter;
import edu.rice.comp610.model.ball.Ball;
import edu.rice.comp610.model.ball.Fish;

/**
 * Absorb - The smaller object will transfer it's size to the larger object.
 * Uses double dispatch / host&visitor design pattern.
 */
public class AbsorbCollision implements ICollisionStrategy{
    private static AbsorbCollision ref;
    DispatchAdapter da;

    /**
     * Private constructor - implement singleton design pattern.
     */
    private AbsorbCollision(DispatchAdapter da) {
        this.da = da;
    }

    /**
     * The name of the strategy.
     * @return strategy name
     */
    public String getName() { return "Absorb"; }

    /**
     * Check for collision (In whatever way the strategy specifies)
     * and update positions accordingly.
     */
    public void updateState(Fish contextOne, Fish contextTwo) {
        //Take action if there is a collision.
        if (contextOne.isCollision(contextTwo)) {
            //Change the size of the bigger object to include the smaller object.
            //Then, delete the smaller object.
            boolean contextTwoEaten = contextOne.absorb(contextTwo);
            if (contextTwoEaten) da.removeSingleObject(contextTwo);
            else da.removeSingleObject(contextOne);
        }
    }

    /**
     * Check for collision (In whatever way the strategy specifies)
     * and update positions accordingly.
     */
    public void updateState(Ball contextOne, Ball contextTwo) {
        //Take action if there is a collision.
        if (contextOne.isCollision(contextTwo)) {
            //Change the size of the bigger object to include the smaller object.
            //Then, delete the smaller object.
            boolean contextTwoEaten = contextOne.absorb(contextTwo);
            if (contextTwoEaten) da.removeSingleObject(contextTwo);
            else da.removeSingleObject(contextOne);
        }
    }

    /**
     * Check for collision (In whatever way the strategy specifies)
     * and update positions accordingly.
     */
    public void updateState(Ball contextOne, Fish contextTwo) {
        //Take action if there is a collision.
        if (contextOne.isCollision(contextTwo)) {
            //Change the size of the bigger object to include the smaller object.
            //Then, delete the smaller object.
            boolean contextTwoEaten = contextOne.absorb(contextTwo);
            if (contextTwoEaten) da.removeSingleObject(contextTwo);
            else da.removeSingleObject(contextOne);
        }
    }

    /**
     * Check for collision (In whatever way the strategy specifies)
     * and update positions accordingly.
     */
    public void updateState(Fish contextOne, Ball contextTwo) {
        //Take action if there is a collision.
        if (contextOne.isCollision(contextTwo)) {
            //Change the size of the bigger object to include the smaller object.
            //Then, delete the smaller object.
            boolean contextTwoEaten = contextOne.absorb(contextTwo);
            if (contextTwoEaten) da.removeSingleObject(contextTwo);
            else da.removeSingleObject(contextOne);
        }
    }

    /**
     * Static method to return the singleton class.
     * @return a reference to the singleton class.
     */
    public static AbsorbCollision getInstance(DispatchAdapter da) {
        if (ref == null) ref = new AbsorbCollision(da);
        return ref;
    }
}
