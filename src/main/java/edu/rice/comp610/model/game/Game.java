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
        //Initialize light pieces for team 0.
        lightPieces = new ArrayList<>();
        //Pawn row
        lightPieces.add(new Pawn("a2", 0));
        lightPieces.add(new Pawn("b2", 0));
        lightPieces.add(new Pawn("c2", 0));
        lightPieces.add(new Pawn("d2", 0));
        lightPieces.add(new Pawn("e2", 0));
        lightPieces.add(new Pawn("f2", 0));
        lightPieces.add(new Pawn("g2", 0));
        lightPieces.add(new Pawn("h2", 0));
        //Other row
        lightPieces.add(new Rook("a1", 0));
        lightPieces.add(new Rook("h1", 0));
        lightPieces.add(new Knight("b1", 0));
        lightPieces.add(new Knight("g1", 0));
        lightPieces.add(new Bishop("c1", 0));
        lightPieces.add(new Bishop("f1", 0));
        lightPieces.add(new Queen("d1", 0));
        lightPieces.add(new King("e1", 0));


        //initialize dark pieces for team 1.
        darkPieces = new ArrayList<>();
        //Pawn row
        darkPieces.add(new Pawn("a7", 1));
        darkPieces.add(new Pawn("b7", 1));
        darkPieces.add(new Pawn("c7", 1));
        darkPieces.add(new Pawn("d7", 1));
        darkPieces.add(new Pawn("e7", 1));
        darkPieces.add(new Pawn("f7", 1));
        darkPieces.add(new Pawn("g7", 1));
        darkPieces.add(new Pawn("h7", 1));
        //Other row
        darkPieces.add(new Rook("a8", 1));
        darkPieces.add(new Rook("h8", 1));
        darkPieces.add(new Knight("b8", 1));
        darkPieces.add(new Knight("g8", 1));
        darkPieces.add(new Bishop("c8", 1));
        darkPieces.add(new Bishop("f8", 1));
        darkPieces.add(new Queen("d8", 1));
        darkPieces.add(new King("e8", 1));
    }
}
