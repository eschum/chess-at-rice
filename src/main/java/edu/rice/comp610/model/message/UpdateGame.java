package edu.rice.comp610.model.message;

import edu.rice.comp610.model.piece.Piece;

import java.util.ArrayList;

public class UpdateGame extends Message {
    private ArrayList<Piece> lightPieces;
    private ArrayList<Piece> darkPieces;
    private boolean lightPlayerTurn;
    private String move;

    public UpdateGame(ArrayList<Piece> light, ArrayList<Piece> dark, boolean lightTurn, String move) {
        type = "update_game";
        lightPieces = light;
        darkPieces = dark;
        lightPlayerTurn = lightTurn;
        this.move = move;
    }
}
