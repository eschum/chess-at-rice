package edu.rice.comp610.model.game;

import edu.rice.comp610.model.piece.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game {
    private ArrayList<Piece> lightPieces;
    private ArrayList<Piece> darkPieces;
    private Map<String, Piece> positions;
    private ArrayList<Player> spectators;
    private Player lightPlayer;
    private Player darkPlayer;

    public Game(Player p1, Player p2) {
        lightPlayer = p1;
        darkPlayer = p2;
        initNewGame();
    }


    public ArrayList<Piece> getLightPieces() {
        return lightPieces;
    }

    public ArrayList<Piece> getDarkPieces() {
        return darkPieces;
    }

    /**
     * Method: init
     * Darw all pieces
     */
    private void initNewGame() {
        //Initialize map to store the positions of all the pieces.
        positions = new HashMap<>();

        //Initialize light pieces for team 0.
        lightPieces = new ArrayList<>();
        //Pawn row
        lightPieces.add(new Pawn("a2", 0));
        positions.put("a2", lightPieces.get(lightPieces.size()-1));
        lightPieces.add(new Pawn("b2", 0));
        positions.put("b2", lightPieces.get(lightPieces.size()-1));
        lightPieces.add(new Pawn("c2", 0));
        positions.put("c2", lightPieces.get(lightPieces.size()-1));
        lightPieces.add(new Pawn("d2", 0));
        positions.put("d2", lightPieces.get(lightPieces.size()-1));
        lightPieces.add(new Pawn("e2", 0));
        positions.put("e2", lightPieces.get(lightPieces.size()-1));
        lightPieces.add(new Pawn("f2", 0));
        positions.put("f2", lightPieces.get(lightPieces.size()-1));
        lightPieces.add(new Pawn("g2", 0));
        positions.put("g2", lightPieces.get(lightPieces.size()-1));
        lightPieces.add(new Pawn("h2", 0));
        positions.put("h2", lightPieces.get(lightPieces.size()-1));
        //Other row
        lightPieces.add(new Rook("a1", 0));
        positions.put("a1", lightPieces.get(lightPieces.size()-1));
        lightPieces.add(new Rook("h1", 0));
        positions.put("h1", lightPieces.get(lightPieces.size()-1));
        lightPieces.add(new Knight("b1", 0));
        positions.put("b1", lightPieces.get(lightPieces.size()-1));
        lightPieces.add(new Knight("g1", 0));
        positions.put("g1", lightPieces.get(lightPieces.size()-1));
        lightPieces.add(new Bishop("c1", 0));
        positions.put("c1", lightPieces.get(lightPieces.size()-1));
        lightPieces.add(new Bishop("f1", 0));
        positions.put("f1", lightPieces.get(lightPieces.size()-1));
        lightPieces.add(new Queen("d1", 0));
        positions.put("d1", lightPieces.get(lightPieces.size()-1));
        lightPieces.add(new King("e1", 0));
        positions.put("e1", lightPieces.get(lightPieces.size()-1));

        //initialize dark pieces for team 1.
        darkPieces = new ArrayList<>();
        //Pawn row
        darkPieces.add(new Pawn("a7", 1));
        positions.put("a7", darkPieces.get(darkPieces.size()-1));
        darkPieces.add(new Pawn("b7", 1));
        positions.put("b7", darkPieces.get(darkPieces.size()-1));
        darkPieces.add(new Pawn("c7", 1));
        positions.put("c7", darkPieces.get(darkPieces.size()-1));
        darkPieces.add(new Pawn("d7", 1));
        positions.put("d7", darkPieces.get(darkPieces.size()-1));
        darkPieces.add(new Pawn("e7", 1));
        positions.put("e7", darkPieces.get(darkPieces.size()-1));
        darkPieces.add(new Pawn("f7", 1));
        positions.put("f7", darkPieces.get(darkPieces.size()-1));
        darkPieces.add(new Pawn("g7", 1));
        positions.put("g7", darkPieces.get(darkPieces.size()-1));
        darkPieces.add(new Pawn("h7", 1));
        positions.put("h7", darkPieces.get(darkPieces.size()-1));
        //Other row
        darkPieces.add(new Rook("a8", 1));
        positions.put("a8", darkPieces.get(darkPieces.size()-1));
        darkPieces.add(new Rook("h8", 1));
        positions.put("h8", darkPieces.get(darkPieces.size()-1));
        darkPieces.add(new Knight("b8", 1));
        positions.put("b8", darkPieces.get(darkPieces.size()-1));
        darkPieces.add(new Knight("g8", 1));
        positions.put("g8", darkPieces.get(darkPieces.size()-1));
        darkPieces.add(new Bishop("c8", 1));
        positions.put("c8", darkPieces.get(darkPieces.size()-1));
        darkPieces.add(new Bishop("f8", 1));
        positions.put("f8", darkPieces.get(darkPieces.size()-1));
        darkPieces.add(new Queen("d8", 1));
        positions.put("d8", darkPieces.get(darkPieces.size()-1));
        darkPieces.add(new King("e8", 1));
        positions.put("e8", darkPieces.get(darkPieces.size()-1));
    }
}
