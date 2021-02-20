package edu.rice.comp610.model;

import edu.rice.comp610.model.ball.Ball;
import edu.rice.comp610.model.ball.Fish;
import edu.rice.comp610.model.ball.PaintObject;
import edu.rice.comp610.model.cmd.SwitchStrategyCommand;
import edu.rice.comp610.model.strategy.*;
import org.eclipse.jetty.websocket.api.Session;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;

import static com.heroku.api.http.Http.ContentType.JSON;

/**
 * This adapter interfaces with the view (paint objects) and the controller.
 */
public class DispatchAdapter {
    private static Session playerOne;
    private static Session playerTwo;


    private static PropertyChangeSupport pcs;
    private static Point dims;
    private final HashMap<String, PaintObject> nameIndex;  //Keep a hashmap to associate individual objects.
    public static ICollisionStrategy ballBallCollisionStrategy;
    public static ICollisionStrategy ballFishCollisionStrategy;
    public static ICollisionStrategy fishFishCollisionStrategy;
    private int fishCounter;
    private int ballCounter;

    /**
     * Constructor call.
     */
    public DispatchAdapter() {
        playerOne = null;
        playerTwo = null;

        pcs = new PropertyChangeSupport(this);
        //Initialize counters for our object counters and our hashmap to have name references to all.
        fishCounter = 0;
        ballCounter = 0;
        nameIndex = new HashMap<>();

        ballBallCollisionStrategy = BounceCollision.getInstance();
        fishFishCollisionStrategy = BounceCollision.getInstance();
        ballFishCollisionStrategy = BounceCollision.getInstance();
    }

    /**
     * Connect Players: Method to associate our connection
     * @param user
     * @return
     */
    public static int connectPlayer(Session user) {
        if (playerOne == null) {
            playerOne = user;
            return 1;
        } else if (playerTwo == null) {
            playerTwo = user;
            return 2;
        } else {
            return 0;
        }
    }


    /**
     * Accessor for the getBallCollisionStrategy
     * @return the current collision strategy for ball-ball.
     */
    public ICollisionStrategy getBallColStrategy() {
        return ballBallCollisionStrategy;
    }

    /**
     * Accessor for the getBallCollisionStrategy
     * @return the current collision strategy for fish-fish.
     */
    public ICollisionStrategy getFishColStrategy() {
        return fishFishCollisionStrategy;
    }

    /**
     * Accessor for the getBallCollisionStrategy
     * @return the current collision strategy for ball-fish.
     */
    public ICollisionStrategy getBallFishColStrategy() {
        return ballFishCollisionStrategy;
    }

    /**
     * Accessor method to return the static pcs - so objects can call the collision changes themselves.
     * @return the PropertyChangeSupport object.
     */
    public static PropertyChangeSupport getPCS() {
        return pcs;
    }

    /**
     * Get the canvas dimensions.
     * @return The canvas dimensions
     */
    public static Point getCanvasDims() {
        return dims;
    }

    /**
     * Set the canvas dimensions.
     * @param dims The canvas width (x) and height (y).
     */
    public static void setCanvasDims(Point dims) {
        DispatchAdapter.dims = dims;
    }

    /**
     * Call the update method on all the ball observers to update their position in the ball world.
     */
    public PropertyChangeListener[] updateBallWorld() {
        pcs.firePropertyChange("theClock", false, true);
        return pcs.getPropertyChangeListeners("theClock");
    }

    /**
     * Generate a random number.
     * Made public for easy access from Strategy classes.
     * @param base  The minimum value
     * @param limit The maximum number from the base
     * @return A randomly generated number
     */
    public static int getRnd(int base, int limit) {
        return (int)Math.floor(Math.random() * limit + base);
    }

    /**
     * loadObj: General Method to load an object into the paint world.
     * Will delegate to loadBall or loadFish.
     * @param body - Strategy desired (String).
     * @param type - String encoding the type of object desired to create.
     * @param switches - String encoding whether or not the object is switchable.
     * @return - the paint world object.
     */
    public PaintObject loadObj(String body, String type, String switches) {
        int radius = switch(type) {
            case "ball" -> getRnd(1, 100);
            //Hard-code the radius as half of the length of the image, adding any scaling
            case "fish" -> 200;
            default -> 200;
        };
        //Don't place the ball on the border (with enough margin) - otherwise it can't effectively rotate.
        Point loc = new Point(getRnd(radius + 1, dims.x - 2 * radius - 2),
                getRnd(radius + 1, dims.y - 2 * radius - 2));
        Point vel = new Point(getRnd(1, 50), getRnd(1, 50));
        String[] color = {"red", "orange", "yellow", "green", "blue", "indigo", "violet"};
        int colorIdx = getRnd(0, color.length - 1);
        boolean switchable = switches.equals("true");

        IUpdateStrategy strategy = switch(body) {
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

        PaintObject obj = switch(type) {
            case "ball" -> new Ball(loc, radius, vel, color[colorIdx], switchable, strategy);
            case "fish" -> new Fish(loc, radius, vel, color[colorIdx], switchable, strategy);
            default -> null;
        };

        //name the object
        String objName = "";
        if (obj != null) objName = obj.getName();
        if (type.equals("ball")) {
            objName += "-" + ballCounter;
            ballCounter++;
        } else if (type.equals("fish")) {
            objName += "-" + fishCounter;
            fishCounter++;
        }
        if (!switchable) objName += " (Non-Switchable)";
        if (obj != null) obj.setName(objName);

        //Add appropriate event listeners.
        addListener(obj);

        return obj;
    }

    /**
     * Helper method to switch strategies.
     * Also, switch the intra-object collision types in the DA.
     * @param body - New strategy desired
     */
    public void switchStrategy(String body, ArrayList<String> selections, String ballCollision,
                                                   String fishCollision, String ballFishCollision) {
        //Make sure to check that there is a selection made.
        if (selections != null) {
            for (String s : selections) {
                //placeholder - manually change strategies here - if allowable.
                PaintObject obj = nameIndex.get(s);
                if (obj != null && obj.isSwitchable())
                    //If the object is switchable, directly send the update command.
                    new SwitchStrategyCommand(body).execute(obj);
            }
        }

        //Change each of the intra-object collision types as applicable:
        ballBallCollisionStrategy = switch(ballCollision) {
            case "bounce" -> BounceCollision.getInstance();
            case "chase" -> ChaseCollision.getInstance();
            case "scared-dodge" -> DodgeCollision.getInstance();
            case "color-change" -> ColorChangeCollision.getInstance();
            case "incinerate" -> IncinerateCollision.getInstance(this);
            case "explode" -> ExplodeCollision.getInstance(this);
            case "excitation" -> ExcitationCollision.getInstance();
            case "eat-absorb" -> AbsorbCollision.getInstance(this);
            default -> null;
        };

        fishFishCollisionStrategy = switch(fishCollision) {
            case "bounce" -> BounceCollision.getInstance();
            case "chase" -> ChaseCollision.getInstance();
            case "scared-dodge" -> DodgeCollision.getInstance();
            case "color-change" -> ColorChangeCollision.getInstance();
            case "incinerate" -> IncinerateCollision.getInstance(this);
            case "explode" -> ExplodeCollision.getInstance(this);
            case "excitation" -> ExcitationCollision.getInstance();
            case "eat-absorb" -> AbsorbCollision.getInstance(this);
            default -> null;
        };

        ballFishCollisionStrategy = switch(ballFishCollision) {
            case "bounce" -> BounceCollision.getInstance();
            case "chase" -> ChaseCollision.getInstance();
            case "scared-dodge" -> DodgeCollision.getInstance();
            case "color-change" -> ColorChangeCollision.getInstance();
            case "incinerate" -> IncinerateCollision.getInstance(this);
            case "explode" -> ExplodeCollision.getInstance(this);
            case "excitation" -> ExcitationCollision.getInstance();
            case "eat-absorb" -> AbsorbCollision.getInstance(this);
            default -> null;
        };
    }

    /**
     * dd an object that will listen for a property change (i.e. time elapsed)
     * @param Obj - An Object for Paint World.
     */
    public void addListener(PaintObject Obj) {
        //Associate the object in our name lookup.
        nameIndex.put(Obj.getName(), Obj);

        pcs.addPropertyChangeListener("theClock", Obj);
        pcs.addPropertyChangeListener("collision-detect", Obj);
    }

    /**
     * Remove deletable subset of objects based on selection in the view.
     */
    public void removeSubsetListeners(ArrayList<String> selections) {
        for (String s : selections) {
            //placeholder - manually change strategies here - if allowable.
            PaintObject obj = nameIndex.get(s);
            if (obj != null)
                removeSingleObject(obj);
        }
    }

    /**
     * High level helper method to remove a single Object from Paint world.
     * This is meant to be called from other classes; so that we don't have external
     * access to private data structures.
     * @param Obj - the object to remove.
     */
    public void removeSingleObject(PaintObject Obj) {
        removeSingleListener(Obj);
        nameIndex.remove(Obj.getName());
    }

    /**
     * Remove all objects from listening for property change events for a particular property.
     * Note: cannot remove an item while iterating over the list.
     */
    public void removeListeners() {
        for (PropertyChangeListener pcl : pcs.getPropertyChangeListeners("theClock")) {
            removeSingleListener(pcl);
        }

        nameIndex.clear();
    }

    /**
     * Remove Single Listener
     * Remove a single listener from all the event listener registries, and from the collision
     * @param pcl - PropertyChangeListener
     */
    public void removeSingleListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener("theClock", pcl);
        pcs.removePropertyChangeListener("collision-detect", pcl);
    }
}
