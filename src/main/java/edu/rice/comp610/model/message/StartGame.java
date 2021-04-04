package edu.rice.comp610.model.message;

import edu.rice.comp610.model.game.Player;
import edu.rice.comp610.model.piece.Piece;
import java.util.ArrayList;

/**
 * Class: Start Game
 * Message with all the contents to initialize the game in the View of each player.
 */
public class StartGame extends Message {
    /*
    Cannot convert the fields to local variables because
    they will be referenced in the View.
     */
    private final ArrayList<Piece> lightPieces;
    private final ArrayList<Piece> darkPieces;
    private final boolean lightPlayer;
    private final boolean darkPlayer;
    private final boolean spectator;
    private final boolean lightPlayerTurn;
    private final String turnName;

    /**
     * Public Constructor. Initialize all state to be sent to the view.
     * @param light ArrayList of light player pieces.
     * @param dark ArrayList of dark player pieces.
     * @param p1 boolean whether this player is lightPlayer.
     * @param p2 boolean whether this player is darkPlayer.
     * @param spectator boolean whether or not this player is a spectator.
     */
    public StartGame(ArrayList<Piece> light, ArrayList<Piece> dark, boolean p1,
                     boolean p2, boolean spectator, Player currTurnPlayer) {
        type = "start_game";
        lightPlayer = p1;
        darkPlayer = p2;
        this.spectator = spectator;
        lightPieces = light;
        darkPieces = dark;
        lightPlayerTurn = true;
        this.turnName = currTurnPlayer.getName();
    }
}
