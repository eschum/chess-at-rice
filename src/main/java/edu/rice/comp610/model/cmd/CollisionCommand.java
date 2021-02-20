package edu.rice.comp610.model.cmd;

import edu.rice.comp610.model.DispatchAdapter;
import edu.rice.comp610.model.ball.Ball;
import edu.rice.comp610.model.ball.Fish;
import edu.rice.comp610.model.ball.PaintObject;

public class CollisionCommand implements ICollideCommand {
    public PaintObject first;

    public CollisionCommand(PaintObject obj) {
        first = obj;
    }

    public void execute(Ball contextOne, Ball contextTwo) {
        DispatchAdapter.ballBallCollisionStrategy.updateState(contextOne, contextTwo);
    }
    public void execute(Fish contextOne, Fish contextTwo) {
        DispatchAdapter.fishFishCollisionStrategy.updateState(contextOne, contextTwo);
    }
    public void execute(Ball contextOne, Fish contextTwo) {
        DispatchAdapter.ballFishCollisionStrategy.updateState(contextOne, contextTwo);
    }

    public void execute(Fish contextOne, Ball contextTwo) {
        DispatchAdapter.ballFishCollisionStrategy.updateState(contextOne, contextTwo);
    }

    /**
     * We need to cast because the event listener only passes an object.
     * @param contextTwo - the other Paint Object.
     */
    public void execute(PaintObject contextTwo) {
        PaintObject contextOne = first;
        //First, exit if this is self.
        if (contextOne == contextTwo) return;

        if (contextOne instanceof Ball) {
            if (contextTwo instanceof Ball) execute((Ball) contextOne, (Ball) contextTwo);
            else execute((Ball) contextOne, (Fish) contextTwo);
        } else {
            //Context 1 is fish.
            if (contextTwo instanceof Ball) execute((Fish) contextOne, (Ball) contextTwo);
            else execute((Fish) contextOne, (Fish) contextTwo);
        }
    }
}
