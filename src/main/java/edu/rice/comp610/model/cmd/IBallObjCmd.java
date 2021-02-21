package edu.rice.comp610.model.cmd;

/**
 * The IPaintObjCmd is an interface used to pass commands to objects in the PaintObjWorld.  The
 * objects must execute the command.
 */
public interface IBallObjCmd {

    /**
     * Execute the command.
     */
    void execute();
}
