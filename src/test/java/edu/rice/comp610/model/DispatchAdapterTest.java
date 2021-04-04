package edu.rice.comp610.model;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.heroku.api.parser.Json;
import edu.rice.comp610.model.authentication.IAuthenticate;
import edu.rice.comp610.model.authentication.SimpleAuthenticator;
import edu.rice.comp610.model.game.Game;
import edu.rice.comp610.model.game.Player;
import edu.rice.comp610.model.message.Message;
import edu.rice.comp610.model.message.PlayerJoin;
import edu.rice.comp610.model.validation.IValidateMove;
import edu.rice.comp610.model.validation.SimpleMoveValidator;
import junit.framework.TestCase;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;

/**
 * Dispatch Adapter Test
 * Unit Tests for Template Ball World.
 */
public class DispatchAdapterTest extends TestCase {

    @Spy
    Game testGame = new Game("test game");

    @Spy
    Player p1 = new Player("test_player_1", null);

    @Spy
    Player p2 = new Player("test_player_2", null);

    @Mock
    Session sess_p1;

    @Mock
    Session sess_p2;

    public void testChessModel() {
        MockitoAnnotations.initMocks(this);

        DispatchAdapter da = new DispatchAdapter();
        Gson gson = new Gson();

        /*
        Test DispatchAdapter Creation, Joining, and Connecting users.
         */
        String user1 = "test_user_1";
        String user2 = "test_user_2";
        String user3 = "test_user_3";
        //Create/join the game - from the lobby
        da.addNewGame(user1);  //Will create the game as "Game0" and create lightPlayer
        assertEquals("darkPlayer join", "darkPlayer", da.joinGame(user2, "Game0"));  //Will create darkPlayer
        assertEquals("spectator join", "spectator", da.joinGame(user3, "Game0"));

        /*
        Mock the sessions (and endpoints).
         */
        Session test_sess_1 = Mockito.mock(Session.class);
        RemoteEndpoint endpoint_1 = Mockito.mock(RemoteEndpoint.class);
        Mockito.when(test_sess_1.getRemote()).thenReturn(endpoint_1);

        Session test_sess_2 = Mockito.mock(Session.class);
        RemoteEndpoint endpoint_2 = Mockito.mock(RemoteEndpoint.class);
        Mockito.when(test_sess_2.getRemote()).thenReturn(endpoint_2);

        Session test_sess_3 = Mockito.mock(Session.class);
        RemoteEndpoint endpoint_3 = Mockito.mock(RemoteEndpoint.class);
        Mockito.when(test_sess_3.getRemote()).thenReturn(endpoint_2);

        /*
        Test messaging responses
         */
        //Connect lightPlayer
        JsonObject msg = new JsonObject();
        msg.addProperty("type", "join");
        msg.addProperty("username", user1);
        msg.addProperty("role", "lightPlayer");
        da.processMessage(test_sess_1, gson.toJson(msg));

        Game gameCheck = da.getGameFromSession(test_sess_1);
        assertEquals("lightPlayer mapped to test_sess_1", test_sess_1,
                gameCheck.getLightPlayer().getSession());

        //Connect darkPlayer
        msg = new JsonObject();
        msg.addProperty("type", "join");
        msg.addProperty("username", user2);
        msg.addProperty("role", "darkPlayer");
        da.processMessage(test_sess_2, gson.toJson(msg));

        assertEquals("darkPlayer mapped to test_sess_2", test_sess_2,
                gameCheck.getDarkPlayer().getSession());

        //Connect spectator
        msg = new JsonObject();
        msg.addProperty("type", "join");
        msg.addProperty("username", user3);
        msg.addProperty("role", "spectator");
        da.processMessage(test_sess_3, gson.toJson(msg));

        /*
        lightPlayer makes a move.
         */

        //Test piece location before move.
        SimpleMoveValidator validator = SimpleMoveValidator.getInstance();
        assertEquals("piece correct before move", "rlt60.png",
                gameCheck.getPieceFromPositions("a1").getImage());

        //Send move message.
        msg = new JsonObject();
        msg.addProperty("type", "move");
        msg.addProperty("fromLoc", "a1");
        msg.addProperty("toLoc", "a3");
        da.processMessage(test_sess_1, gson.toJson(msg));

        //Test location after the move.
        assertEquals("piece moved correctly", "rlt60.png",
                gameCheck.getPieceFromPositions("a3").getImage());

        /*
        darkPlayer makes a move
         */
        msg = new JsonObject();
        msg.addProperty("type", "move");
        msg.addProperty("fromLoc", "g8");
        msg.addProperty("toLoc", "g6");
        da.processMessage(test_sess_2, gson.toJson(msg));

        //Test location after the move.
        assertEquals("piece moved correctly", "ndt60.png",
                gameCheck.getPieceFromPositions("g6").getImage());

        /*
        lightPlay move - Take a piece from darkPlayer
         */
        msg = new JsonObject();
        msg.addProperty("type", "move");
        msg.addProperty("fromLoc", "a3");
        msg.addProperty("toLoc", "g6");
        da.processMessage(test_sess_1, gson.toJson(msg));

        //Test location after the move.
        assertEquals("piece moved correctly", "rlt60.png",
                gameCheck.getPieceFromPositions("g6").getImage());

        //Test the correct team of the piece.
        assertEquals("piece captured by lightPlayer", 0,
                gameCheck.getPieceFromPositions("g6").getTeam());

        /*
        Make a wrong move - lightPlayer selects opposite piece.
         */
        msg = new JsonObject();
        msg.addProperty("type", "move");
        msg.addProperty("fromLoc", "a8");
        msg.addProperty("toLoc", "b4");
        da.processMessage(test_sess_1, gson.toJson(msg));

        //Test that the piece has not moved.
        assertEquals("piece captured by lightPlayer", 1,
                gameCheck.getPieceFromPositions("a8").getTeam());

        /*
        Chat message - sent by darkPlayer
         */
        msg = new JsonObject();
        msg.addProperty("type", "chat");
        msg.addProperty("content", "Testing the chat message");
        da.processMessage(test_sess_2, gson.toJson(msg));

        /*
        Heartbeat message - lightPlayer, darkPlayer, and spectator
         */
        msg = new JsonObject();
        msg.addProperty("type", "heartbeat");
        da.processMessage(test_sess_1, gson.toJson(msg));
        da.processMessage(test_sess_2, gson.toJson(msg));
        da.processMessage(test_sess_3, gson.toJson(msg));

        /*
        Chat message
         */
        msg = new JsonObject();
        msg.addProperty("type", "chat");
        msg.addProperty("content", "test chat message content.");
        da.processMessage(test_sess_1, gson.toJson(msg));
        da.processMessage(test_sess_2, gson.toJson(msg));
        da.processMessage(test_sess_3, gson.toJson(msg));

        /*
        Request Draw
         */
        // Player 1 requests.
        msg = new JsonObject();
        msg.addProperty("type", "request_draw");
        da.processMessage(test_sess_1, gson.toJson(msg));

        /*
        Draw Deny - player 2 denies.
         */
        msg = new JsonObject();
        msg.addProperty("type", "draw_deny");
        da.processMessage(test_sess_2, gson.toJson(msg));

        /*
        Spectator leave; and test that light and dark player are still connected.
         */
        da.handleClose(test_sess_3, null);

        //Confirm that lightPlayer and darkPlayer are now still associated with
        //the game
        assertEquals("lightPlayer still connected", gameCheck, da.getGameFromSession(test_sess_1));
        assertEquals("darkPlayer still connected", gameCheck, da.getGameFromSession(test_sess_2));


        /*
        Player leave - through lightPlayer agreeing to a draw.
        Test that the game has now been cancelled (and darkplayer no longer associated with the game)
         */
        msg = new JsonObject();
        msg.addProperty("type", "draw_agree");
        da.processMessage(test_sess_1, gson.toJson(msg));
        assertEquals("darkPlayer disconnected", null, da.getGameFromSession(test_sess_2));

        /*
        Player leave - through lightPlayer requesting a resign.
        Recreate the game and players. Then submit the resign.
        Finally, confirm again that darkPlay has been disconnected.
         */
        da.addNewGame(user1);  //Will create the game as "Game1" and create lightPlayer
        //Creates "Game1" as the counter has already been incremented.
        msg = new JsonObject();
        msg.addProperty("type", "join");
        msg.addProperty("username", user1);
        msg.addProperty("role", "lightPlayer");
        da.processMessage(test_sess_1, gson.toJson(msg));

        gameCheck = da.getGameFromSession(test_sess_1);
        assertEquals("lightPlayer mapped to test_sess_1", test_sess_1,
                gameCheck.getLightPlayer().getSession());

        //Dark player - join game and connect the session.
        assertEquals("darkPlayer join", "darkPlayer", da.joinGame(user2, "Game1"));
        msg = new JsonObject();
        msg.addProperty("type", "join");
        msg.addProperty("username", user2);
        msg.addProperty("role", "darkPlayer");
        da.processMessage(test_sess_2, gson.toJson(msg));
        assertEquals("darkPlayer mapped to test_sess_2", test_sess_2,
                gameCheck.getDarkPlayer().getSession());

        //Player leave - through lightPlayer resignation.
        //Again test that the game has been cancelled and lightPlayer is no longer associated.
        msg = new JsonObject();
        msg.addProperty("type", "request_resign");
        da.processMessage(test_sess_1, gson.toJson(msg));
        assertEquals("darkPlayer disconnected", null, da.getGameFromSession(test_sess_2));

        /*
        Recreate the game, and take all of an opponent's pieces.
        End with the King; and test that the game has ended.
         */
        da.addNewGame(user1);  //Will create the game as "Game2" and create lightPlayer
        //Creates "Game1" as the counter has already been incremented.
        msg = new JsonObject();
        msg.addProperty("type", "join");
        msg.addProperty("username", user1);
        msg.addProperty("role", "lightPlayer");
        da.processMessage(test_sess_1, gson.toJson(msg));
        gameCheck = da.getGameFromSession(test_sess_1);
        assertEquals("lightPlayer mapped to test_sess_1", test_sess_1,
                gameCheck.getLightPlayer().getSession());
        //Dark player - join game and connect the session.
        assertEquals("darkPlayer join", "darkPlayer", da.joinGame(user2, "Game2"));
        msg = new JsonObject();
        msg.addProperty("type", "join");
        msg.addProperty("username", user2);
        msg.addProperty("role", "darkPlayer");
        da.processMessage(test_sess_2, gson.toJson(msg));
        assertEquals("darkPlayer mapped to test_sess_2", test_sess_2,
                gameCheck.getDarkPlayer().getSession());

        //Take the opponent's pieces - with various move messages
        //Take Rook at a1
        msg = new JsonObject();
        msg.addProperty("type", "move");
        msg.addProperty("fromLoc", "a8");
        msg.addProperty("toLoc", "a1");
        da.processMessage(test_sess_2, gson.toJson(msg));
        //Take Pawn at a2
        msg = new JsonObject();
        msg.addProperty("type", "move");
        msg.addProperty("fromLoc", "a1");
        msg.addProperty("toLoc", "a2");
        da.processMessage(test_sess_2, gson.toJson(msg));
        //Confirm that now there is no piece at a1 after moving away.
        assertEquals("light piece taken", null, gameCheck.getPieceFromPositions("a1"));

        //Take Knight at B1
        msg = new JsonObject();
        msg.addProperty("type", "move");
        msg.addProperty("fromLoc", "a2");
        msg.addProperty("toLoc", "b1");
        da.processMessage(test_sess_2, gson.toJson(msg));

        //Take Bishop at C1
        msg = new JsonObject();
        msg.addProperty("type", "move");
        msg.addProperty("fromLoc", "b1");
        msg.addProperty("toLoc", "c1");
        da.processMessage(test_sess_2, gson.toJson(msg));

        //Take Queen at D1
        msg = new JsonObject();
        msg.addProperty("type", "move");
        msg.addProperty("fromLoc", "c1");
        msg.addProperty("toLoc", "d1");
        da.processMessage(test_sess_2, gson.toJson(msg));

        //Finally, take King at E1. Finally, test that the game has ended.
        msg = new JsonObject();
        msg.addProperty("type", "move");
        msg.addProperty("fromLoc", "d1");
        msg.addProperty("toLoc", "e1");
        da.processMessage(test_sess_2, gson.toJson(msg));
        //Confirm that both players are disconnected.
        assertEquals("darkPlayer disconnected", null, da.getGameFromSession(test_sess_2));
        assertEquals("lightPlayer disconnected", null, da.getGameFromSession(test_sess_1));

        /*
        Reset the game, and handle a close window (exitMessage == null)
        from the lightPlayer / darkPlayer
         */
        da.addNewGame(user1);  //Will create the game as "Game3" and create lightPlayer
        //Creates "Game1" as the counter has already been incremented.
        msg = new JsonObject();
        msg.addProperty("type", "join");
        msg.addProperty("username", user1);
        msg.addProperty("role", "lightPlayer");
        da.processMessage(test_sess_1, gson.toJson(msg));
        gameCheck = da.getGameFromSession(test_sess_1);
        assertEquals("lightPlayer mapped to test_sess_1", test_sess_1,
                gameCheck.getLightPlayer().getSession());
        //Dark player - join game and connect the session.
        assertEquals("darkPlayer join", "darkPlayer", da.joinGame(user2, "Game3"));
        msg = new JsonObject();
        msg.addProperty("type", "join");
        msg.addProperty("username", user2);
        msg.addProperty("role", "darkPlayer");
        da.processMessage(test_sess_2, gson.toJson(msg));
        assertEquals("darkPlayer mapped to test_sess_2", test_sess_2,
                gameCheck.getDarkPlayer().getSession());

        //Before closing the game, get all the games when there was a game added via the messaging.
        ArrayList<JsonObject> collectedGames = da.getAllGames();
        String gameID = collectedGames.get(0).get("gameID").toString();
        gameID = gameID.substring(1, gameID.length() - 1);
        assertEquals("collected game ID", "Game3", gameID);

        da.handleClose(test_sess_1, null);
        //Check that the game was closed by showing both players booted.
        assertEquals("game ended, darkPlayer booted",null, da.getGameFromSession(test_sess_2));
        assertEquals("game ended, lightPlayer booted",null, da.getGameFromSession(test_sess_1));

        /*
        Miscellaneous remaining tests
         */
        //Test addPlayer() on our Spy Game.
        testGame.addPlayer(p1);
        testGame.addPlayer(p2);
        assertEquals("lightPlayer assignment", true, testGame.getLightPlayer() == p1);
        ArrayList<JsonObject> collectGames = da.getAllGames();
        Player getSender = da.getSendingPlayer(null);

        Player testP = new Player("Test_user");
        PlayerJoin playerJoin = new PlayerJoin(testP);
        playerJoin.setName("new name");
        assertEquals("playerjoin name set", "new name",playerJoin.getName());
    }

    /**
     * Method: Test Simple Authenticator
     * Test cases to test the simple Authenticator.
     */
    public void testSimpleAuthenticator() {
        IAuthenticate simpAuth = SimpleAuthenticator.getInstance();

        JsonObject credCheck = simpAuth.validateCredentials("eric", "ricestudent");
        JsonObject expectedResponse = new JsonObject();
        expectedResponse.addProperty("auth", "true");
        assertEquals("valid login - eric", expectedResponse, credCheck);

        credCheck = simpAuth.validateCredentials("bogus", "credentials");
        expectedResponse.remove("auth");
        expectedResponse.addProperty("auth", "false");
        assertEquals("invalid login", expectedResponse, credCheck);
    }


    public void testSimpleMoveValidator() {
        IValidateMove valMove = SimpleMoveValidator.getInstance();

        Game anotherTestGame = new Game("another test game");
        anotherTestGame.addPlayer(p1);
        anotherTestGame.addPlayer(p2);

        //Test for no piece being selected.
        Pair<Integer, String> retVal = valMove.checkIfLegal("a4", "b4", p1, anotherTestGame);

        //Test selecting other team's piece.
        Pair<Integer, String> retVal2 = valMove.checkIfLegal("a8", "a1", p1, testGame);

        //Move onto square occupied by own piece.
        Pair<Integer, String> retVal3 = valMove.checkIfLegal("a1", "a1", p1, anotherTestGame);
    }
}