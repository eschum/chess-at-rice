package edu.rice.comp610.model.piece;

import edu.rice.comp610.model.game.Player;

/**
 * Class: Rook
 * Implementation of the Rook piece object.
 * Subclass of Piece, implements IPiece.
 */
public class Rook extends Piece {

    /**
     * Public constructor
     * @param location The location of the piece in chess notation
     * @param team Integer of the team, 0 for light, 1 for dark
     */
    public Rook(String location, int team) {
        super(location, team);

        if (team == 0) {
            setImage("rlt60.png");
        } else {
            setImage("rdt60.png");
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


