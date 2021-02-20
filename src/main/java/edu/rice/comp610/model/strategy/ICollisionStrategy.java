package edu.rice.comp610.model.strategy;

import edu.rice.comp610.model.ball.Ball;
import edu.rice.comp610.model.ball.Fish;

/**
 * An interface for Paint Object strategies that calls state updates based on object.
 * Implementation was Pseudo-dynamic dispatch.
 */
public interface ICollisionStrategy {
    /**
     * The name of the strategy.
     * @return strategy name
     */
    String getName();

    /**
     * Check for collision (In whatever way the strategy specifies)
     * and update positions accordingly.
     */
    void updateState(Fish contextOne, Fish contextTwo);
    void updateState(Ball contextOne, Ball contextTwo);
    void updateState(Ball contextOne, Fish contextTwo);
    void updateState(Fish contextOne, Ball contextTwo);
}
