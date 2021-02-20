package edu.rice.comp610.model.cmd;

import edu.rice.comp610.model.ball.Ball;
import edu.rice.comp610.model.ball.Fish;
import edu.rice.comp610.model.ball.PaintObject;

/**
 * The IPaintObjCmd is an interface used to pass commands to objects in the PaintObjWorld.  The
 * objects must execute the command.
 */
public interface ICollideCommand {

    /**
     * Execute the command.
     */
    void execute(Ball contextOne, Ball contextTwo);
    void execute(Fish contextOne, Fish contextTwo);
    void execute(Ball contextOne, Fish contextTwo);
    void execute(Fish contextOne, Ball contextTwo);
    void execute(PaintObject contextTwo);
}
