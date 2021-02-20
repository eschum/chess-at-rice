package edu.rice.comp610.model.strategy;

import edu.rice.comp610.model.ball.PaintObject;

/**
 * An interface for Paint Object strategies that calls state updates based on object.
 * Implementation was Pseudo-dynamic dispatch.
 */
public interface IUpdateStrategy {

    /**
     * The name of the strategy.
     * @return strategy name
     */
    String getName();

    /**
     * Update the state of the ball.
     * @param context The ball.
     */
    void updateState(PaintObject context);
}
