package edu.rice.comp610.model.piece;

import edu.rice.comp610.model.game.Player;

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

    /**
     * Method: If Taken.
     * No Action.
     * @param opponentPiece The opponent's piece that is overtaking.
     * @param opponent The Piece that has taken the subject piece.
     */
    @Override
    public void ifTaken(Piece opponentPiece, Player opponent) { }
}


