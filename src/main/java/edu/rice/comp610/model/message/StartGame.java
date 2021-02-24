package edu.rice.comp610.model.message;

import edu.rice.comp610.model.game.Game;
import edu.rice.comp610.model.piece.Piece;

import java.util.ArrayList;

public class StartGame extends Message {
    private ArrayList<Piece> lightPieces;
    private ArrayList<Piece> darkPieces;
    private boolean lightPlayer;
    private boolean darkPlayer;
    private boolean spectator;

    public StartGame(ArrayList<Piece> light, ArrayList<Piece> dark, boolean p1, boolean p2, boolean spectator) {
        type = "start_game";
        lightPlayer = p1;
        darkPlayer = p2;
        this.spectator = spectator;
        lightPieces = light;
        darkPieces = dark;
    }
}
