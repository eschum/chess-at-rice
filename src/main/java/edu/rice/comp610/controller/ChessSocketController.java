package edu.rice.comp610.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.rice.comp610.model.DispatchAdapter;
import static spark.Spark.*;


/**
 * The paint world controller creates the adapter(s) that communicate with the view.
 * The controller responds to requests from the view after contacting the adapter(s).
 */
public class ChessSocketController {

    /**
     * The main entry point into the program.
     * @param args  The program arguments normally specified on the cmd line
     */
    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        staticFiles.location("/public");

        DispatchAdapter dis = new DispatchAdapter();
        Gson gson = DispatchAdapter.gson;

        WebSocketController wsc = new WebSocketController(dis);

        webSocket("/chess", wsc);

        //Start a new game, instantiating the game / player.
        post("/new", (request, response) -> {
            String username = request.queryParams("username");
            System.out.print("User " + username + " requested a new game\n");
            //Instantiate a new game
            String gameID = dis.addNewGame(username);
            JsonObject obj = new JsonObject();
            obj.addProperty("gameid", gameID);

            return obj;
        });

        //Join a game if the game is available to join.
        post("/join", (request, response) -> {
            String username = request.queryParams("username");
            String gameID = request.queryParams("gameID");
            System.out.print("User " + username + " requested a join\n");

            //Join Game
            String status = dis.joinGame(username, gameID);

            //Report to user as to how (if) they joined
            JsonObject obj = new JsonObject();
            obj.addProperty("status", status);
            return obj;
        });

        //Respond with all games, so that we can refresh the table.
        post("/refresh", (request, response) -> {
            return gson.toJson(dis.getAllGames());
        });
    }

    /**
     * Method GetHerokuAssignedPort
     * Listen for correct port, or assign default if running on localhost.
     * @return - the port to use for the execution context.
     */
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;  //Just return the default port if heroku port isn't set (i.e. when on localhost)
    }
}
