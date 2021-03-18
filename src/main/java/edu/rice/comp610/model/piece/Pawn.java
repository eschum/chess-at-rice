package edu.rice.comp610.model.piece;

import edu.rice.comp610.model.game.Player;

/**
 * Class: Pawn
 * Implementation of the Pawn piece object.
 * Subclass of Piece, implements IPiece.
 */
public class Pawn extends Piece {

    /**
     * Public constructor
     * @param location The location of the piece in chess notation
     * @param team Integer of the team, 0 for light, 1 for dark
     */
    public Pawn(String location, int team) {
        super(location, team);

        if (team == 0) {
            setImage("plt60.png");
        } else {
            setImage("pdt60.png");
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


