package edu.rice.comp610.model.message;

import edu.rice.comp610.model.piece.Piece;

import java.util.ArrayList;

public class UpdateGame extends Message {
    private final ArrayList<Piece> lightPieces;
    private final ArrayList<Piece> darkPieces;
    private final boolean lightPlayerTurn;
    private final String move;

    /**
     * Constructor
     * @param light ArrayList of Light Pieces.
     * @param dark ArrayList of Dark Pieces
     * @param lightTurn Boolean - whether or not it is lightPlayer turn
     * @param move - string describing the most recent move (will be NULL if sending manual update for
     *             newly-joining spectator)
     */
    public UpdateGame(ArrayList<Piece> light, ArrayList<Piece> dark, boolean lightTurn, String move) {
        type = "update_game";
        lightPieces = light;
        darkPieces = dark;
        lightPlayerTurn = lightTurn;
        this.move = move;
    }
}
