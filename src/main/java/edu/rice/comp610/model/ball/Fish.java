package edu.rice.comp610.model.ball;

import edu.rice.comp610.model.strategy.*;
import java.awt.*;
import java.util.Random;

/**
 * The fish objects that will be drawn in the Paint World
 * Extends the PaintObject Class - such that there can also be other types of objects.
 */
public class Fish extends PaintObject {
    private String image;
    private static final Random r = new Random();
    private double scale;
    private boolean flip; //Need as instance variable to render in view.
    private double rotate;  //Angle of rotation for the fish image around center of image.
    private boolean normalColor;

    /**
     * Constructor.
     * @param loc  The location of the ball on the canvas
     * @param radius The ball radius
     * @param vel  The ball velocity
     * @param color The ball color
     * @param switchable Determines if the object can switch strategies
     * @param strategy  The object strategy
     */
    public Fish(Point loc, int radius, Point vel, String color, boolean switchable, IUpdateStrategy strategy) {
        super(loc, radius, vel, color, switchable, strategy);
        image = "clownfish-2.png";
        normalColor = true;
        scale = 0.05 + (0.7 - 0.05) * r.nextDouble();   //Set radius / size to randomly scale.
        updateRadius();//set the radius specifically for this fish image and this scale factor.
        setTheta(r.nextInt(360)); //Initialize a random radial location
        setRenderType("fish");
        this.setName("Fish");
    }

    /**
     * Absorb another fish (as a fish).
     * Part of the visitor design pattern.
     * Remember to only operate on a single object.
     * @param otherFish - the other fish we are encountering.
     * @return Returns true if otherFish has been absorbed. Returns false if self has been absorbed.
     */
    public boolean absorb(Fish otherFish) {
        double otherScale = otherFish.getScale();

        if (scale >= otherScale) {
            //If we are larger than the other fish, absorb the fish.
            scale += 0.1;
            return true;
        }
        return false;
    }

    /**
     * Absorb a ball (as a fish)
     * @param other - the other object we are encountering.
     * @return - Return true if self absorbs the other object. False if other way around.
     */
    public boolean absorb(Ball other) {
        int r1 = getRadius();
        int r2 = other.getRadius();

        if (r1 >= r2) {
            //If we are larger than the other fish, absorb the fish.
            scale = scale + 0.1;
            return true;
        }
        return false;
    }
    /**
     * Mutator method to change the fish color (invert the pixels; pre-manipulated image)
     */
    public void changeColor() {
        if (normalColor) {
            //If currently the fish is normal colored, then invert it and set appropriate state.
            image = "clownfish-2-inverted.png";
            normalColor = false;
        } else {
            //If the fish is inverted, then reset.
            image = "clownfish-2.png";
            normalColor = true;
        }
    }

    /**
     * Mutator method to change the image.
     * @param img - String of an image.
     */
    public void setImage(String img) {
        image = img;
    }

    /**
     * Accessor method for image string.
     * @return the string of the current image value.
     */
    public String getImage() {
        return image;
    }

    /**
     * Method Update Radius.
     * Update the radius based on original image size and current scaling.
     * For image: clownfish-2.png
     */
    @Override
    public void updateRadius() {
        setRadius((int) (200 * scale));
    }

    /**
     * Mutator method for rotate variable (angle of rotation of fish)
     * @param rot - angle in radians.
     */
    @Override
    public void setRotate(double rot) {
        rotate = rot;
    }

    /**
     * Accessor method for the rotate parameter.
     * @return - the rotation status in radians.
     */
    public double getRotate() {
        return rotate;
    }

    /**
     * Accessor method for the scaling factor of the fish image.
     * @return scale factor (double)
     */
    @Override
    public double getScale() { return scale; }

    /**
     * Mutator method for the scaling factor of the fish image
     * @param s - scaling factor (a double)
     */
    @Override
    public void setScale(double s) { scale += s; }

    /**
     * Mutator method to set whether or not the fish image should be flipped when drawing.
     * @param flipInput - boolean whether or not the image should be flipped.
     */
    @Override
    public void setFlip(boolean flipInput) {
        flip = flipInput;
    }

    /**
     * Update the angle that the fish image is rendered.
     * Also determine whether or not it should be flipped.
     */
    @Override
    public void updateImageAngle() {
        Point vel = getVelocity();

        double angle;

        setFlip(false);

        if (vel.x == 0 && vel.y > 0) {
            angle = Math.PI / 2;
        } else if (vel.x == 0 && vel.y < 0) {
            angle = - Math.PI / 2;
        } else if (vel.y == 0 && vel.x > 0) {
            angle = 0;
        } else if (vel.y == 0 && vel.x < 0) {
            angle = Math.PI;
            setFlip(true);
        } else {
            angle = Math.atan(((double) vel.y) / vel.x);
            if (vel.x < 0) {
                angle += Math.PI;
                setFlip(true);
            }
        }
        setRotate(angle);
    }
}

