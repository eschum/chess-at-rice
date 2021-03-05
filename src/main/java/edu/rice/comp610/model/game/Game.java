package edu.rice.comp610.model.game;

import com.google.gson.Gson;
import edu.rice.comp610.model.DispatchAdapter;
import edu.rice.comp610.model.message.ChatMessage;
import edu.rice.comp610.model.message.ErrorMessage;
import edu.rice.comp610.model.message.Message;
import edu.rice.comp610.model.message.UpdateGame;
import edu.rice.comp610.model.piece.*;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
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
    private boolean lightPlayerTurn;
    private Gson gson;
    private Map<Session, Player> entities;
    private String gameID;

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

    public Game(String ID) {
        gson = DispatchAdapter.gson;
        entities = new HashMap<>();
        spectators = new ArrayList<>();
        this.gameID = ID;
        initNewGame();
    }

    /**
     * Method: Get ID
     * Accessor method to return the String
     * @return
     */
    public String getID() {
        return this.gameID;
    }

    //public void addPlayer
    public void addPlayer(Player p) {
        if (lightPlayer == null) {
            lightPlayer = p;
            entities.put(p.getSession(), p);
        } else if (darkPlayer == null) {
            darkPlayer = p;
            entities.put(p.getSession(), p);
        }
    }

    /**
     * Method: Add Spectator
     * Description: Mutator method to add a spectator.
     * @param = - Player to add to spectator list.
     */

    public void addSpectator(Player p) {
        spectators.add(p);
        entities.put(p.getSession(), p);

        //Broadcast to all that a spectator has entered.
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
     * Accessor to return the light player .
     * @return
     */
    public Player getLightPlayer() {
        return lightPlayer;
    }

    /**
     * Accessor to return the dark player.
     * @return
     */
    public Player getDarkPlayer() {
        return darkPlayer;
    }


    /**
     * Method: Process Move
     * Process a move that is received from a session.
     * Need to make sure that the from location is a piece, and it is a player's own piece.
     * Check to make sure that the player matches the type of piece that is attempted to be moved.
     * Then, process the validity of the move itself (can insert more advanced Chess logic in future)
     * Finally, respond with an update to the board, or an error message. To both players and all spectators.
     * @param userSession
     * @param fromLoc
     * @param toLoc
     */
    public void processMove(Session userSession, String fromLoc, String toLoc) {
        Piece selectedPiece = positions.get(fromLoc);

        if (selectedPiece == null) {
            //send error message - a piece wasn't selected
            sendErrorMessage(userSession, "A piece was not selected");

            return;

        } else if ((userSession == lightPlayer.getSession() && selectedPiece.getTeam() != 0) ||
                (userSession == darkPlayer.getSession() && selectedPiece.getTeam() != 1)) {
            //send error message - piece selected is of the wrong team
            sendErrorMessage(userSession, "Selecting piece of opposite team is not allowed");


            return;
        }

        //If we made it out, process the move of a valid piece selection.
        if (!validateMove(selectedPiece, toLoc)) {
            //TODO send error message - invalid move requested - moving onto square with own piece.
            sendErrorMessage(userSession, "Cannot move onto square already occupied by own piece");
            return;

        }

        //If passes the validation, then make the move. Update positions. Remove captured piece. Send update.
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

    }

    /**
     * Method: Send Update Message
     * Update the board state.
     */
    public void sendUpdateMessage(String move) {
        lightPlayerTurn = !lightPlayerTurn;
        Message update = new UpdateGame(lightPieces, darkPieces, lightPlayerTurn, move);

        //Send the message to all entities.
        broadcastMessage(update);
    }

    /**
     * Method: Broadcast Message
     * @param msg
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
     * @param session
     * @param errString
     */
    public void sendErrorMessage(Session session, String errString) {
        try {
            session.getRemote().sendString(gson.toJson(new ErrorMessage(errString)));
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }

    /**
     * Method: Validate Move
     * Check if this is a valid move (or if we are moving onto a piece from our own team).
     *
     * This assumes that processMove has already checked that the piece selected is of the correct team.
     * Do it this way so we don't pass userSession info to the validateMove method.
     * @param selectedPiece
     * @param toLoc
     * @return - false if the move is invalid. True if the move is valid.
     */
    private boolean validateMove(Piece selectedPiece, String toLoc) {
        //Boolean zen - yeah!
        //Can extend to add specific error checking based on piece type.
        System.out.print("Contains key? : " + positions.containsKey(toLoc));
        return (!positions.containsKey(toLoc) || positions.get(toLoc).getTeam() != selectedPiece.getTeam());
    }

    /**
     * Remove Piece
     * Helper method to remove a piece from the Array List containing the pieces for the appropriate team.
     * @param targetPiece
     */
    private void removePiece(Piece targetPiece) {
        ArrayList<Piece> pieces = targetPiece.getTeam() == 0 ? lightPieces : darkPieces;
        pieces.remove(targetPiece);
    }

    /**
     * Method: Process Chat
     * Receive a chat message; and distribute it to the chat history of everyone in this game.
     * @param userSession
     * @param content
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
     * Get Light Pieces
     * Helper method to return the array list of pieces.
     * @return
     */
    public ArrayList<Piece> getLightPieces() {
        return lightPieces;
    }

    /**
     * Get Dark
     * Helper method to return the array list of pieces.
     * @return
     */
    public ArrayList<Piece> getDarkPieces() {
        return darkPieces;
    }

    /**
     * Method: init
     * Darw all pieces
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
