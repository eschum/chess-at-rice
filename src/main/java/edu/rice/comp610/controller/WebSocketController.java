package edu.rice.comp610.controller;

import com.google.gson.Gson;
import edu.rice.comp610.model.DispatchAdapter;
import edu.rice.comp610.model.message.Player;
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
            p1 = new Player();
            p1.setName("Player One");
            try {
                playerOne.getRemote().sendString(gson.toJson(p1));
            } catch (IOException e) {
                System.out.println("IO Exception");
            }
        } else if (connected == 2) {
            playerTwo = userSession;
            p2 = new Player();
            p2.setName("Player Two");
            try {
                playerTwo.getRemote().sendString(gson.toJson(p2));
                playerTwo.getRemote().sendString(gson.toJson(p1));
                playerOne.getRemote().sendString(gson.toJson(p2));
            } catch (IOException e) {
                System.out.println("IO Exception");
            }

            //As player two is now connected, we can start the game.


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
