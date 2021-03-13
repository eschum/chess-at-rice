package edu.rice.comp610.model.piece;

import edu.rice.comp610.model.game.Player;

/**
 * IPiece interface defines what behavior the Chess Piece
 * classes must have to interact with the Chess game.
 */
public interface IPiece {
    /**
     * Method: Set Image
     * Store the image that will represent the piece.
     * @param img String that stores the file location for the piece.
     */
    void setImage(String img);

    /**
     * Method: Get Team
     * Return an integer for what team the piece is on.
     * @return integer; 0 for light; 1 for dark.
     */
    int getTeam();

    /**
     * Method: If Taken
     * Defines behavior the piece should have if it is taken by the opponent.
     * @param opponent The Piece that has taken the subject piece.
     */
    void ifTaken(Piece opponentPiece, Player opponent);
}
