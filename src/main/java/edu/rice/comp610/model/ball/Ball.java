package edu.rice.comp610.model.ball;

import edu.rice.comp610.model.DispatchAdapter;
import edu.rice.comp610.model.strategy.IUpdateStrategy;
import java.awt.*;

/**
 * The balls that will be drawn in the ball world.
 * Extends the PaintObject Class - such that there can also be other types of objects.
 */
public class Ball extends PaintObject {
    private final String[] colorSet = {"red", "orange", "yellow", "green", "blue", "indigo", "violet"};

    /**
     * Constructor.
     * @param loc  The location of the ball on the canvas
     * @param radius The ball radius
     * @param vel  The ball velocity
     * @param color The ball color
     * @param switchable Determines if the object can switch strategies
     * @param strategy  The object strategy
     */
    public Ball(Point loc, int radius, Point vel, String color, boolean switchable, IUpdateStrategy strategy) {
        super(loc, radius, vel, color, switchable, strategy);
        setRenderType("ball");
        this.setName("Ball");
    }

    /**
     * Mutator method to change the ball color.
     */
    @Override
    public void changeColor() {
        int colorIdx = DispatchAdapter.getRnd(0, colorSet.length - 1);
        setColor(colorSet[colorIdx]);
    }

    /**
     * Absorb a ball (as a ball).
     * Part of the visitor design pattern.
     * @param other - the other object we are encountering
     * @return Returns true if otherFish has been absorbed. Returns false if self has been absorbed.
     */
    public boolean absorb(Ball other) {
        int r1 = getRadius();
        int r2 = other.getRadius();

        if (r1 >= r2) {
            //If we are larger than the other fish, absorb the fish.
            setRadius(r1 + r2 / 2);
            return true;
        }
        return false;

    }

    /**
     * Absorb a fish (as a ball).s
     * Part of the visitor design pattern.
     * @param otherFish - the other object we are encountering.
     * @return Returns true if otherFish has been absorbed. Returns false if self has been absorbed.
     */
    public boolean absorb(Fish otherFish) {
        if (getRadius() >= otherFish.getRadius()) {
            //If we are larger than the other fish, absorb the fish.
            setRadius(getRadius() + otherFish.getRadius() / 2);
            return true;
        }
        return false;
    }
}

