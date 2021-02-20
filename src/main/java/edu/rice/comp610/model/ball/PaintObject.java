package edu.rice.comp610.model.ball;

import edu.rice.comp610.model.DispatchAdapter;
import edu.rice.comp610.model.cmd.CollisionCommand;
import edu.rice.comp610.model.cmd.SwitchStrategyCommand;
import edu.rice.comp610.model.cmd.UpdateCommand;
import edu.rice.comp610.model.strategy.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The objects that will be drawn in Paint World.
 */
public class PaintObject implements PropertyChangeListener {
    private Point loc;
    private int radius;
    private Point vel;
    private IUpdateStrategy strategy;
    private String color;
    private final boolean switchable;
    private String viewRenderType;  //cannot convert to local var because need view to be able to render.
    private double theta;
    private String name;

    /**
     * Constructor.
     * @param loc  The location of the ball on the canvas
     * @param radius The ball radius
     * @param vel  The ball velocity
     * @param color The ball color
     * @param switchable Determines if the object can switch strategies
     * @param strategy  The object strategy
     */
    public PaintObject(Point loc, int radius, Point vel, String color, boolean switchable, IUpdateStrategy strategy) {
        this.loc = loc;
        this.radius = radius;
        this.vel = vel;
        this.color = color;
        this.switchable = switchable;
        this.strategy = strategy;
    }

    /**
     * Accessor method to return whether the object is switchable or not.
     * @return true: switchable. false: not switchable
     */
    public boolean isSwitchable() {
        return this.switchable;
    }

    /**
     * Method: is Collision
     * General method for detecting collisions between PaintObject objects.
     * Use the post-collision velocity of self, and pre-collision velocity of other.
     * @param other - the other PaintObject object that we are checking for collision.
     * @return - determine whether or not a collision is happening.
     */
    public boolean isCollision(PaintObject other) {
        Point p2 = other.getLocation();
        int r2 = other.getRadius();
        double dist = Math.sqrt(Math.pow(loc.x - p2.x, 2) + Math.pow(loc.y - p2.y, 2));

        //Note: Added 10 to give the fish some margin so there isn't so much instability.
        return dist <= radius + r2;   //Boolean zen. Cool!
    }

    /**
     * Add speed: Helper method to add some speed to self.
     * @param delta - the amount of speed to add.
     */
    public void addSpeed(int delta) {
        //Give the extra speed to self.
        if (vel.x > 0) vel.x += delta;
        else vel.x -= delta;

        if (vel.y > 0) vel.y += delta;
        else vel.y -= delta;
    }

    /**
     * Method to change object color. To be overridden in each subclass.
     */
    public void changeColor() { }

    /**
     * Accessor method for the angle the fish is currently taking around canvas center.
     * @return angle in radians.
     */
    public double getTheta() {
        return theta;
    }

    /**
     * Mutator method for the angle the fish is currently taking around canvas center.
     * @param t - angle in radians.
     */
    public void setTheta(double t) {
        theta = t;
    }

    /**
     * Mutator method for rotation
     * Delegate to subclasses via overloading.
     * Do nothing in the superclass.
     *
     * @param rot - angle in radians.
     */
    public void setRotate(double rot) { }

    /**
     * Mutator method to set whether or not the image should be flipped when drawing.
     * Delegate to subclasses via overloading.
     * Do nothing in the superclass.
     * @param flipInput - boolean whether or not the image should be flipped.
     */
    public void setFlip(boolean flipInput) { }

    /**
     * Update properties to correctly rotate the image in rendering.
     * Will be overridden by respective subclass functions (or ignored).
     * Use dynamic dispatch design pattern
     */
    public void updateImageAngle() { }

    /**
     * Method Update Radius.
     * Update the radius based on original image size and current scaling.
     * For image: clownfish-2.png
     */
    public void updateRadius() { }

    /**
     * Set the scale of an object's image.
     * Will be delegated (through overriding and dynamic dispatch) to the
     * applicable object type.
     * @param s - input image scale (double).
     */
    public void setScale(double s) { }

    /**
     * Get scale
     * Will be overridden by sub class.
     * @return the scale - overridden by classes that actual have a scale.
     */
    public double getScale() { return 0.5; }

    /**
     * Set the name of the object type for rendering with the view.
     * @param name - String name for the object.
     */
    public void setRenderType(String name) {
        this.viewRenderType = name;
    }

    /**
     * Mutator method to set the name of the object.
     * @param name - string with the intended name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Accessor method to return the name of the object.
     * @return The name of the object (a String).
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the ball color.
     * @param c The new ball color
     */
    public void setColor(String c) {
        this.color = c;
    }

    /**
     * Get the ball location in the ball world.
     * @return The ball location.
     */
    public Point getLocation() {
        return this.loc;
    }

    /**
     * Set the ball location in the canvas.  The origin (0,0) is the top left corner of the canvas.
     * @param loc  The ball coordinate.
     */
    public void setLocation(Point loc) {
        this.loc = loc;
    }

    /**
     * Get the velocity of the ball.
     * Don't return the velocity itself...as we don't want any mutation.
     * (Fish rotation angle calculations need to change vel.x / vel.y to 0 if a vertical strategy or horizontal).
     * @return The ball velocity
     */
    public Point getVelocity() {
        return new Point(vel.x, vel.y);
    }

    /**
     * Set the velocity of the ball.
     * @param vel The new ball velocity
     */
    public void setVelocity(Point vel) {
        this.vel = vel;
    }

    /**
     * Get the radius of the ball.
     * @return The ball radius.
     */
    public int getRadius() {
        return this.radius;
    }

    /**
     * Set the radius of the ball.
     * @param r The ball radius.
     */
    public void setRadius(int r) {
        this.radius = r;
    }

    /**
     * Get the ball strategy.
     * @return The ball strategy.
     */
    public IUpdateStrategy getStrategy() {
        return this.strategy;
    }

    /**
     * Set the strategy if the ball can switch strategies.
     * @param strategy  The new strategy
     */
    public void setStrategy(IUpdateStrategy strategy) {
        if (switchable) {
            this.strategy = strategy;
        }
    }

    /**
     * Detects collision between a ball and a wall in the ball world.  Change direction if ball collides with a wall.
     * @return True if there was a collision and false otherwise.
     */
    public boolean detectCollision() {
        Point dims = DispatchAdapter.getCanvasDims();
        boolean collision = false;
        //Check right and left walls. Invert x component of velocity if collision.
        //Pay attention here to not change direction AGAIN if direction was already changed but
        //due to the ball's radius, there is a "glitch" that the direction should be changed again.
        if ((loc.x + radius >= dims.x && vel.x > 0) || (loc.x - radius <= 0 && vel.x < 0)) {
            vel.x = -vel.x;
            collision = true;
        }

        //check top and bottom walls. Invert y component of velocity if collision.
        if ((loc.y - radius <= 0 && vel.y < 0) || (loc.y + radius >= dims.y && vel.y > 0)) {
            vel.y = -vel.y;
            collision = true;
        }
        return collision;
    }

    /**
     *  PaintObjects should respond to various Event triggers from the PropertyChangeSupport.
     *  Each class (ball, fish) will override the class with the desired activity.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "theClock" -> {
                //Call the update command to execute the state update on the ball.
                new UpdateCommand().execute(this);
                //Send command to all other objects to then update themselves
                sendCollisionCommand();
            }
            case "strategyChange" -> {
                //Call the switch strategy command to execute strategy switch on the ball.
                String newStrategy = (String) evt.getNewValue();
                new SwitchStrategyCommand(newStrategy).execute(this);
            }
            case "collision-detect" -> {
                CollisionCommand com = (CollisionCommand) evt.getNewValue();
                com.execute(this);
            }
        }
    }

    /**
     * Send Collision Command
     * Helper method called from propertyChange implementation that will send a command
     * for each object to update their anti-collision status.
     */
    public void sendCollisionCommand() {
        CollisionCommand com = new CollisionCommand(this);
        DispatchAdapter.getPCS().firePropertyChange("collision-detect", "test", com);
    }
}

