package edu.rice.comp610.model.game;

import com.google.gson.Gson;
import edu.rice.comp610.model.DispatchAdapter;
import edu.rice.comp610.model.message.*;
import edu.rice.comp610.model.piece.*;
import edu.rice.comp610.model.validation.ValidateMove;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class Game
 * Store and manage the players and spectators of this game.
 * Store all piece positions
 * Handle move requests and status changes.
 */
public class Game {
    private ArrayList<Piece> lightPieces;
    private ArrayList<Piece> darkPieces;
    private Map<String, Piece> positions;
    private final ArrayList<Player> spectators;
    private Player lightPlayer;
    private Player darkPlayer;
    private boolean lightPlayerTurn;
    private final Gson gson;
    private final Map<Session, Player> entities;
    private String gameID;

    /**
     * Public Constructor - If we already have two players.
     * @param p1 - first player
     * @param p2 - first player
     */
    public Game(Player p1, Player p2) {
        gson = new Gson();
        entities = new HashMap<>();
        lightPlayer = p1;
        darkPlayer = p2;
        entities.put(p1.getSession(), p1);
        entities.put(p2.getSession(), p2);
        spectators = new ArrayList<>();
        initNewGame();
    }

    /**
     * Public Constructor - passing an intended game ID (String)
     * @param ID - String of the game id.
     */
    public Game(String ID) {
        gson = DispatchAdapter.gson;
        entities = new HashMap<>();
        spectators = new ArrayList<>();
        this.gameID = ID;
        initNewGame();
    }

    /**
     * Method: Get All Players
     * Accessor method that returns an ArrayList of all players
     * mapped to this game.
     * @return ArrayList of all the players (players and spectators) involved with this game.
     */
    public ArrayList<Player> getAllPlayers() {
        return new ArrayList<>(entities.values());
    }

    /**
     * Method: Get Player From Session
     * Return the player that was connected to the Session.
     * @param userSession The session that is desired to match with the respective Player object.
     * @return Return the Player object associated with userSession.
     */
    public Player getPlayerFromSession(Session userSession) {
        return entities.get(userSession);
    }

    /**
     * Method: Handle Leave
     * Determine whether the session leaving is a spectator or a player.
     * Return whether or not it is a player; so the DA can remove the rest of the
     * connections.
     * @param userSession The Session of the player that is leaving (if King taken, just put the victorious
     *                    player's session here)
     * @param exitMessage Null if this is a session closure. Otherwise the Message that describes why the game
     *                    is now over or a player is leaving.
     * @return true if needed to garbage collect all entities related to the game, or false if just need to
     * garbage collect the leaving spectator.
     */
    public boolean handleLeave(Session userSession, Message exitMessage) {
        //Determine which player is leaving.
        Player leaver = entities.get(userSession);

        //Remove the player from the entity set.
        entities.remove(userSession);

        if (exitMessage == null) {
            if (leaver == lightPlayer || leaver == darkPlayer) {
            /*
            If a key player leaves, then send a message to everyone that the game is over.
            Also, need to remove the game from the list in the DA.
             */
                System.out.print("Player " + leaver.getName() + " has left\n");
                broadcastMessage(new PlayerLeave(leaver));
                return true;

            } else {
            /*
            If a spectator leaves, remove them from the spectator list.
            Send a spectator_leave message to let others know the spectator has left.
            Gameplay can continue.
             */
                System.out.print("Spectator " + leaver.getName() + " left\n");
                spectators.remove(leaver);
                broadcastMessage(new SpectatorLeave(leaver));
                return false;
            }

        } else if (exitMessage instanceof KingTaken){
            /*
            If a King has been taken, then broadcast the king taken message to all players.
            Return true to garbage collect all elements of the game.
             */
            System.out.print("A Game has ended due to a King being taken.\n");
            broadcastMessage(exitMessage);
            //Need to send the message specifically to userSession as they were already removed from the entity list.
            try {
                userSession.getRemote().sendString(gson.toJson(exitMessage));
            } catch (IOException e) {
                System.out.println("IO Exception");
            }

            return true;
        }

        return false;  //Guard to compile without issue.
    }

    /**
     * Method: Get ID
     * Accessor method to return the String
     * @return String of game ID.
     */
    public String getID() {
        return this.gameID;
    }

    /**
     * Method: Add Player
     * Associate proper player (lightplayer / darkplayer) with
     * the provided instance of Player.
     * @param p The Player object
     */
    public void addPlayer(Player p) {
        if (lightPlayer == null) {
            lightPlayer = p;
        } else if (darkPlayer == null) {
            darkPlayer = p;
        }
    }

    /**
     * Method: Add Entity
     * Mutator to add the session - player mapping once the session joins as the
     * appropriate player.
     * @param p - Player to add to the entity list (once they have a connected session)
     */
    public void addEntity(Player p) {
        entities.put(p.getSession(), p);
    }

    /**
     * Method: Add Spectator
     * Description: Mutator method to add a spectator when selected from the lobby.
     * @param p Player to add to spectator list.
     */
    public void addSpectator(Player p) {
        spectators.add(p);
    }


    /**
     * Method: Connect Spectator
     * Send messages to all entities in the game when the spectator joins.
     * Send a message to manually update the board of the spectator.
     * @param p - player that is joining as a spectator.
     */
    public void connectSpectator(Player p) {
        broadcastMessage(p.getSpectatorJoin());

        //Give the spectator a manual update of the board status.
        Message update = new UpdateGame(lightPieces, darkPieces, lightPlayerTurn, "void");
        try {
            p.getSession().getRemote().sendString(gson.toJson(update));
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }

    /**
     * Accessor to return the light player.
     * @return Player: lightPlayer.
     */
    public Player getLightPlayer() {
        return lightPlayer;
    }

    /**
     * Accessor to return the dark player.
     * @return - Player - darkPlayer
     */
    public Player getDarkPlayer() {
        return darkPlayer;
    }

    /**
     * Method: Get Piece From Positions
     * Returns the piece that is at a selected location
     * @param fromLoc String (chess notation) of a desired location
     * @return The piece at the location (or null)
     */
    public Piece getPieceFromPositions(String fromLoc) {
        return positions.get(fromLoc);
    }


    /**
     * Method: Process Move
     * Process a move that is received from a session.
     * Call the validator class to check the validity of the move.
     * Respond with an update to the board, or an error message. To both players and all spectators.
     * @param userSession - current session the "move" message is coming from.
     * @param fromLoc - the from location (a String).
     * @param toLoc - the to location (a String).
     */
    public void processMove(Session userSession, String fromLoc, String toLoc) {
        //Validate the move. Send error message and return if the move is not valid.
        Pair<Integer, String> validStatus = ValidateMove.getInstance().checkIfLegal(fromLoc, toLoc,
                entities.get(userSession), this);
        if (validStatus != null && validStatus.getKey() != 0) {
            //A piece wasn't selected.
            sendErrorMessage(userSession, validStatus.getValue());
            return;
        }

        //If passes the validation, then make the move. Update positions. Remove captured piece. Send update.
        Piece selectedPiece = positions.get(fromLoc);
        Piece targetPiece = positions.get(toLoc);

        if (targetPiece == null) {
            //If moving to an empty spot, update position of the piece
            selectedPiece.updateLoc(toLoc);

            //Update the record of this piece's position in the game's position map
            positions.remove(fromLoc);
            positions.put(toLoc, selectedPiece);

        } else {
            //if there is an enemy piece, then take the enemy piece.

            //Remove piece from appropriate team's piece store.
            removePiece(targetPiece);

            //Update the record of this piece's position in the game's position map
            positions.remove(fromLoc);
            positions.remove(toLoc);


            //Set the attacking piece as the present position.
            positions.put(toLoc, selectedPiece);
            selectedPiece.updateLoc(toLoc);
        }

        //Broadcast update message to all entities
        String name = entities.get(userSession).getName();
        String move = name + ": " + fromLoc + " -> " + toLoc;
        sendUpdateMessage(move);

        //Finally, send any other additional messages or take additional action.
        if (targetPiece != null) {
            targetPiece.ifTaken(selectedPiece, entities.get(userSession));
        }
    }

    /**
     * Method: Send Update Message
     * Update the board state.
     * @param move - A String with text describing the move that was just updated.
     */
    public void sendUpdateMessage(String move) {
        lightPlayerTurn = !lightPlayerTurn;
        Message update = new UpdateGame(lightPieces, darkPieces, lightPlayerTurn, move);

        //Send the message to all entities.
        broadcastMessage(update);
    }

    /**
     * Method: Broadcast Message
     * @param msg - A Message to send to every entity involved in the game.
     */
    public void broadcastMessage(Message msg) {
        for (Map.Entry mapElement : entities.entrySet()) {
            Session currSession = (Session) mapElement.getKey();
            try {
                currSession.getRemote().sendString(gson.toJson(msg));
            } catch (IOException e) {
                System.out.println("IO Exception");
            }
        }
    }

    /**
     * Method: Send Error Message
     * Helper method to send an error message to the erring player.
     * @param session - the Session to send the error message to.
     * @param errString - the String describing the error.
     */
    public void sendErrorMessage(Session session, String errString) {
        try {
            session.getRemote().sendString(gson.toJson(new ErrorMessage(errString)));
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }

    /**
     * Remove Piece
     * Helper method to remove a piece from the Array List containing the pieces for the appropriate team.
     * @param targetPiece - the Piece to remove
     */
    private void removePiece(Piece targetPiece) {
        ArrayList<Piece> pieces = targetPiece.getTeam() == 0 ? lightPieces : darkPieces;
        pieces.remove(targetPiece);
    }

    /**
     * Method: Process Chat
     * Receive a chat message; and distribute it to the chat history of everyone in this game.
     * @param userSession - The Session that is sending the message.
     * @param content - the String with contents of the message.
     */
    public void processChat(Session userSession, String content) {
        Player sender = entities.get(userSession);
        String spectatorDisclaimer = (sender != lightPlayer && sender != darkPlayer) ? "(Spectator)" : "";
        String chatMessageText = sender.getName() + " " + spectatorDisclaimer + ": " + content;

        //Wrap the message to be interpreted correctly by the view.
        ChatMessage message = new ChatMessage(chatMessageText);

        //Send the message to every entity.
        for (Map.Entry mapElement : entities.entrySet()) {
            Session currSession = (Session) mapElement.getKey();
            try {
                currSession.getRemote().sendString(gson.toJson(message));
            } catch (IOException e) {
                System.out.println("IO Exception");
            }
        }

    }

    /**
     * Method: Get Light Pieces
     * Accessor method to return the Light Piece arraylist.
     * @return - an ArrayList of the light pieces.
     */
    public ArrayList<Piece> getLightPieces() {
        return lightPieces;
    }

    /**
     * Method: Get Dark Pieces
     * Helper accessor method to return the ArrayList of dark pieces.
     * @return An ArrayList of the dark pieces.
     */
    public ArrayList<Piece> getDarkPieces() {
        return darkPieces;
    }

    /**
     * Method: init new game
     * Initiate turn (light player), initiate all pieces, and
     * register all pieces in the positions map
     */
    private void initNewGame() {
        lightPlayerTurn = true; //Light moves first per chess rules.

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
