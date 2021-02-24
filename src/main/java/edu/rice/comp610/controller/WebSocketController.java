package edu.rice.comp610.controller;

import com.google.gson.Gson;
import edu.rice.comp610.model.DispatchAdapter;
import edu.rice.comp610.model.game.Game;
import edu.rice.comp610.model.game.Player;
import edu.rice.comp610.model.message.StartGame;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import java.io.IOException;



@WebSocket
public class WebSocketController {
    DispatchAdapter da;
    Gson gson;
    Player p1;
    Player p2;
    Game game;

    public WebSocketController() {
        da = new DispatchAdapter();
        gson = new Gson();
    }

    @OnWebSocketConnect
    public void onConnect(Session userSession) {
        System.out.print("New Session: " + userSession + "\n");

        //Identify the connection
        int connected = DispatchAdapter.connectPlayer(userSession);
        System.out.print(connected);
        if (connected == 1) {
            //start a new game.
            p1 = new Player("Player One");
            p1.setSession(userSession);

            try {
                p1.getSession().getRemote().sendString(gson.toJson(p1.getJoinMessage()));
            } catch (IOException e) {
                System.out.println("IO Exception");
            }
        } else if (connected == 2) {
            p2 = new Player("Player Two");
            p2.setSession(userSession);
            try {
                p2.getSession().getRemote().sendString(gson.toJson(p2.getJoinMessage()));
                p2.getSession().getRemote().sendString(gson.toJson(p1.getJoinMessage()));
                p1.getSession().getRemote().sendString(gson.toJson(p2.getJoinMessage()));
            } catch (IOException e) {
                System.out.println("IO Exception");
            }

            //As player two is now connected, we can start the game.
            //This code here only temporarily. Eventually handle this with the dispatch adapter.

            //Note: passing game while calling on p1 will lead to reflexive calls.
            game = new Game(p1, p2);

            try {
                p1.getSession().getRemote().sendString(gson.toJson(sendStartMsg(game, true, false, false)));
                p2.getSession().getRemote().sendString(gson.toJson(sendStartMsg(game, false, true, false)));
            } catch (IOException e) {
                System.out.println("IO Exception");
            }
        }
    }

    /**
     * Send Start Message: Helper function to send a message to start the game.
     * Just send piece locations and current status.
     * @param game
     * @param lightPlayer
     * @param darkPlayer
     * @param spectator
     * @return
     */
    private StartGame sendStartMsg(Game game, boolean lightPlayer, boolean darkPlayer, boolean spectator) {
        return new StartGame(game.getLightPieces(), game.getDarkPieces(), lightPlayer, darkPlayer, spectator);
    }

    @OnWebSocketClose
    public void onClose(Session userSession, int statusCode, String reason) {}

    @OnWebSocketMessage
    public void onMessage(Session userSession, String message) {
        System.out.print(message);

    }
}
