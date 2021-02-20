package edu.rice.comp610.model.cmd;

import edu.rice.comp610.model.ball.PaintObject;
import edu.rice.comp610.model.strategy.*;

/**
 * Switch Strategy Command: Change the strategy
 * Update Command will be passed to each ball to tell it to update state.
 *
 */
public class SwitchStrategyCommand implements IBallObjCmd {
    private final String newStrategy;

    /**
     * Constructor - Save the state of the new command that is being asked for.
     * @param strategy - the text of the strategy that we should switch to.
     */
    public SwitchStrategyCommand(String strategy) {
        this.newStrategy = strategy;
    }

    /**
     * Execute the command. Vary behavior based on the type of object of the context.
     * @param context The receiver paint object on which the command is executed.
     */
    public void execute(PaintObject context) {
        IUpdateStrategy strategy = switch(newStrategy) {
            case "diagonal" -> DiagonalStrategy.getInstance();
            case "size-change" -> SizeChangeStrategy.getInstance();
            case "random-reflection" -> RandomReflectionStrategy.getInstance();
            case "friction-bounce" -> FrictionBounceStrategy.getInstance();
            case "zig-zag" -> ZigZagStrategy.getInstance();
            case "rotate" -> RotateStrategy.getInstance();
            case "hopscotch" -> HopScotchStrategy.getInstance();
            case "dead / float" -> DeadStrategy.getInstance();
            case "color-change" -> ColorChangeStrategy.getInstance();
            case "do-the-robot" -> RoboticStrategy.getInstance();
            default -> null;
        };

        context.setStrategy(strategy);
    }
}
