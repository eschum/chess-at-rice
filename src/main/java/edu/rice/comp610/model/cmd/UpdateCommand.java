package edu.rice.comp610.model.cmd;

import edu.rice.comp610.model.ball.PaintObject;


/**
 * Update Command will be passed to each ball to tell it to update state.
 *
 */
public class UpdateCommand implements IBallObjCmd {

    /**
     * Execute the command. Vary the behavior based on the object type that is getting the command.
     * Using dynamic dispatch design pattern - based on what type the object is.
     * @param context The receiver paint object on which the command is executed.
     */
    public void execute(PaintObject context) {
        context.getStrategy().updateState(context);
    }
}
