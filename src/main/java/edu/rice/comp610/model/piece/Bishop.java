package edu.rice.comp610.model.piece;

/**
 * Class Bishop
 * Class for the Bishop chess piece.
 */
public class Bishop extends Piece {

    /**
     * Public constructor.
     * @param location Chess notation String of piece location
     * @param team 0 for light, 1 for dark
     */
    public Bishop(String location, int team) {
        super(location, team);

        if (team == 0) {
            setImage("blt60.png");
        } else {
            setImage("bdt60.png");
        }
    }
}


