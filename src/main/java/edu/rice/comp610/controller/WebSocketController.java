package edu.rice.comp610.controller;

import com.google.gson.Gson;
import edu.rice.comp610.model.DispatchAdapter;
import edu.rice.comp610.model.game.Game;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import java.util.HashMap;
import java.util.Map;


@WebSocket
public class WebSocketController {
    DispatchAdapter da;
    Gson gson;
    Map<Session, Game> allSessions; //Map each session to the appropriate game, to forward moves.

    /**
     * Public constructor
     * @param da - pass the dispatch adapter for the chess context.
     */
    public WebSocketController(DispatchAdapter da) {
        this.da = da;
        gson = new Gson();
        allSessions = new HashMap<>();
    }

    /**
     * On Connect for the Websockets protocol.
     * Server does not initiate any action. Will wait for message from the client.
     * @param userSession - the session of the current connection.
     */
    @OnWebSocketConnect
    public void onConnect(Session userSession) { }

    /**
     * On Close Method for the WebSockets protocol.
     * @param userSession - the session of the current connection.
     * @param statusCode - status code from the websocket protocol.
     * @param reason - String reason from the websocket protocol
     */
    @OnWebSocketClose
    public void onClose(Session userSession, int statusCode, String reason) {
        //We are not concerned about the reason, but only that a user has left.
        //Delegate to the Dispatch Adapter
        da.handleClose(userSession, null);
    }

    /**
     * On Message - For the Websocket protocol.
     * Delegate to the Dispatch Adapter.
     * @param userSession - the session of the current connection.
     * @param message - the message received from the connection.
     */
    @OnWebSocketMessage
    public void onMessage(Session userSession, String message) {
        da.processMessage(userSession, message);
    }
}