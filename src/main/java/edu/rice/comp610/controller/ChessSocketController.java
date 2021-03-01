package edu.rice.comp610.controller;

import com.google.gson.Gson;
import edu.rice.comp610.model.DispatchAdapter;
import java.awt.*;
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

        webSocket("/chess", WebSocketController.class);

        Gson gson = new Gson();
        DispatchAdapter dis = new DispatchAdapter();

        post("/load/:type", (request, response) -> {
            String ballType = request.queryParams("strategies");
            String switchable = request.queryParams("switchable");
            return "";
            //return gson.toJson(obj);
        });

        //Start a new game, instantiating the game / player .
        post("/new", (request, response) -> {
            String username = request.queryParams("username");
            System.out.print("User " + username + " requested a new game\n");
            //Instantiate a new game
            dis.addNewGame(username);
            //redirect to the match interface.
            //response.redirect("/match.html");
            return "OK";
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
