package edu.rice.comp610.model.piece;

import edu.rice.comp610.model.DispatchAdapter;
import edu.rice.comp610.model.game.Player;
import edu.rice.comp610.model.message.KingTaken;
import edu.rice.comp610.model.message.Message;

/**
 * Class: King
 * Implementation of the King piece object.
 * Subclass of Piece, implements IPiece.
 */
public class King extends Piece {

    /**
     * Public constructor
     * @param location The location of the piece in chess notataion
     * @param team Integer of the team, 0 for light, 1 for dark.
     */
    public King(String location, int team) {
        super(location, team);

        if (team == 0) {
            setImage("klt60.png");
        } else {
            setImage("kdt60.png");
        }
    }

    /**
     * Method: If Taken
     * If the King is taken, then the Game is over.
     * Send a message that the opponent wins, and end the game.
     * @param opponentPiece The Piece that has taken the subject piece.
     * @param opponent The Player that has taken the subject piece.
     */
    @Override
    public void ifTaken(Piece opponentPiece, Player opponent) {
        Message gameOver = new KingTaken(opponent);
        DispatchAdapter.handleClose(opponent.getSession(), gameOver);
    }
}


