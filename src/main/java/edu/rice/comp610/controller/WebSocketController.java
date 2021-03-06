package edu.rice.comp610.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.rice.comp610.model.DispatchAdapter;
import edu.rice.comp610.model.game.Game;
import edu.rice.comp610.model.game.Player;
import edu.rice.comp610.model.message.StartGame;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebSocket
public class WebSocketController {
    DispatchAdapter da;
    Gson gson;
    Game game;
    Map<Session, Game> allSessions; //Map each session to the appropriate game, to forward moves.
    int userCounter;

    public WebSocketController(DispatchAdapter da) {
        this.da = da;
        gson = new Gson();
        allSessions = new HashMap<>();
    }

    /**
     * On Connect for the Websockets protocol.
     *
     * @param userSession
     */
    @OnWebSocketConnect
    public void onConnect(Session userSession) {

        //Server does not initiate any action on connection.
        //Client will message with the

//        //Player will be instantiated upon login and entering the System.
//        Player player = new Player("user" + userCounter++, userSession);
//
//        //Player will have also selected or instantiated a new game.
//        if (game == null) game = new Game();
//
//        //Pass to Dispatch Adapter to process.
//        int connected = DispatchAdapter.connectPlayer(player, game);
//
//        if (connected == 1) {
//            try {
//                player.getSession().getRemote().sendString(gson.toJson(player.getJoinMessage()));
//            } catch (IOException e) {
//                System.out.println("IO Exception");
//            }
//            allSessions.put(player.getSession(), game);
//        } else if (connected == 2) {
//
//            try {
//                player.getSession().getRemote().sendString(gson.toJson(player.getJoinMessage()));
//                player.getSession().getRemote().sendString(gson.toJson(game.getLightPlayer().getJoinMessage()));
//                game.getLightPlayer().getSession().getRemote().sendString(gson.toJson(player.getJoinMessage()));
//            } catch (IOException e) {
//                System.out.println("IO Exception");
//            }
//
//            allSessions.put(player.getSession(), game);
//
//            try {
//                Player p1 = game.getLightPlayer();
//                Player p2 = game.getDarkPlayer();
//                p1.getSession().getRemote().sendString(gson.toJson(sendStartMsg(game, true, false, false)));
//                p2.getSession().getRemote().sendString(gson.toJson(sendStartMsg(game, false, true, false)));
//            } catch (IOException e) {
//                System.out.println("IO Exception");
//            }
//        } else if (connected == 0) {
//            //if connected is 0, that means the person is a spectator.
//            allSessions.put(player.getSession(), game);
//            game.addSpectator(player); //Delegate to game class to add the spectator and send the messages.
//        }
    }

    @OnWebSocketClose
    public void onClose(Session userSession, int statusCode, String reason) {}

    @OnWebSocketMessage
    public void onMessage(Session userSession, String message) {

        da.processMessage(userSession, message);

//        //System.out.print(message);
//        JsonObject parsedMsg = gson.fromJson(message, JsonObject.class);
//
//        String type = parsedMsg.get("type").toString();
//        type = type.substring(1, type.length() - 1);
//
//        System.out.print(type);
//        //Take action based on message type.
//        if (type.equals("move")) {
//            //Get the to and from fields.
//            String from = parsedMsg.get("fromLoc").toString();
//            from = from.substring(1, from.length() - 1);
//            String to = parsedMsg.get("toLoc").toString();
//            to = to.substring(1, to.length() - 1);
//            //Delegate that game to process the action and follow up with response.
//            allSessions.get(userSession).processMove(userSession, from, to);
//        } else if (type.equals("chat")) {
//            System.out.print("Processed a chat message");
//            //Get the content field.
//            String content = parsedMsg.get("content").toString();
//            content = content.substring(1, content.length() - 1);
//            //Delegate that game to process sending the chat to all participants.
//            allSessions.get(userSession).processChat(userSession, content);
//        }
    }
}