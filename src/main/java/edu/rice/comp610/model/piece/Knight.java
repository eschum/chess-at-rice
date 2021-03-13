package edu.rice.comp610.model.piece;

/**
 * Class: Knight
 * Implementation of the Knight piece object.
 * Subclass of Piece, implements IPiece.
 */
public class Knight extends Piece {

    /**
     * Public constructor
     * @param location The location of the piece in chess notation
     * @param team Integer of the team, 0 for light, 1 for dark
     */
    public Knight(String location, int team) {
        super(location, team);

        if (team == 0) {
            setImage("nlt60.png");
        } else {
            setImage("ndt60.png");
        }
    }


}


