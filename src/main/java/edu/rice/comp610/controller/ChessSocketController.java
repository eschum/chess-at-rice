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

        get("/update", (request, response) -> gson.toJson(dis.updateBallWorld()));

        post("/canvas/dims", (request, response) -> {
            DispatchAdapter.setCanvasDims(new Point(Integer.parseInt(request.queryParams("width")),
                    Integer.parseInt(request.queryParams("height"))));
            System.out.print("Canvas dimensions set in model");
            return "Canvas dimensions set in model";
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
