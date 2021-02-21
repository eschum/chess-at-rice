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
    Session playerOne;
    Session playerTwo;
    Player p1;
    Player p2;
    Game game;

    public WebSocketController() {
        da = new DispatchAdapter();
        gson = new Gson();
        playerOne = null;
        playerTwo = null;
    }

    @OnWebSocketConnect
    public void onConnect(Session userSession) {
        System.out.print("New Session: " + userSession + "\n");

        //Identify the connection
        int connected = DispatchAdapter.connectPlayer(userSession);
        System.out.print(connected);
        if (connected == 1) {
            //start a new game.
            playerOne = userSession;
            p1 = new Player("Player One");
            try {
                playerOne.getRemote().sendString(gson.toJson(p1.getJoinMessage()));
            } catch (IOException e) {
                System.out.println("IO Exception");
            }
        } else if (connected == 2) {
            playerTwo = userSession;
            p2 = new Player("Player Two");
            try {
                playerTwo.getRemote().sendString(gson.toJson(p2.getJoinMessage()));
                playerTwo.getRemote().sendString(gson.toJson(p1.getJoinMessage()));
                playerOne.getRemote().sendString(gson.toJson(p2.getJoinMessage()));
            } catch (IOException e) {
                System.out.println("IO Exception");
            }

            //As player two is now connected, we can start the game.
            //This code here only temporarily. Eventually handle this with the dispatch adapter.
            game = new Game();
            StartGame startMessage = new StartGame(game);

            try {
                playerOne.getRemote().sendString(gson.toJson(startMessage));
                playerTwo.getRemote().sendString(gson.toJson(startMessage));
            } catch (IOException e) {
                System.out.println("IO Exception");
            }


        }
    }

    @OnWebSocketClose
    public void onClose(Session userSession, int statusCode, String reason) {}

    @OnWebSocketMessage
    public void onMessage(Session userSession, String message) {
        System.out.print(message);
        if (playerOne != null && playerTwo != null) {
            if (userSession == playerOne) {
                try {
                    playerTwo.getRemote().sendString(message);
                } catch (IOException e) {
                    System.out.println("IO Exception");
                }
            }
        }
    }
}
