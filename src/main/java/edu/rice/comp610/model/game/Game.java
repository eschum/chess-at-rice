package edu.rice.comp610.model.game;

import edu.rice.comp610.model.piece.*;
import java.util.ArrayList;

public class Game {
    ArrayList<Piece> lightPieces;
    ArrayList<Piece> darkPieces;

    public Game() {
        initNewGame();
    }

    /**
     * Method: init
     * Darw all pieces
     */
    private void initNewGame() {
        lightPieces = new ArrayList<>();
        lightPieces.add(new Pawn("a2", 0));
        lightPieces.add(new Pawn("b2", 0));
        lightPieces.add(new Pawn("c2", 0));
        lightPieces.add(new Pawn("d2", 0));
        lightPieces.add(new Pawn("e2", 0));
        lightPieces.add(new Pawn("f2", 0));
        lightPieces.add(new Pawn("g2", 0));
        lightPieces.add(new Pawn("h2", 0));
    }
}
