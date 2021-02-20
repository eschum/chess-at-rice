package edu.rice.comp610.controller;

import com.google.gson.Gson;
import edu.rice.comp610.model.*;
import edu.rice.comp610.model.ball.PaintObject;
import java.awt.*;
import java.util.ArrayList;
import static spark.Spark.*;


/**
 * The paint world controller creates the adapter(s) that communicate with the view.
 * The controller responds to requests from the view after contacting the adapter(s).
 */
public class BallWorldController {

    /**
     * The main entry point into the program.
     * @param args  The program arguments normally specified on the cmd line
     */
    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        staticFiles.location("/public");

        Gson gson = new Gson();
        DispatchAdapter dis = new DispatchAdapter();

        post("/load/:type", (request, response) -> {
            String ballType = request.queryParams("strategies");
            String switchable = request.queryParams("switchable");
            PaintObject obj = dis.loadObj(ballType, request.params(":type"), switchable);
            return gson.toJson(obj);
        });

        post("/switch", "application/json", (request, response) -> {
            //System.out.print(request.queryParams("selections"));
            dis.switchStrategy(request.queryParams("strategies"), gson.fromJson(request.queryParams("selections"), ArrayList.class),
                    request.queryParams("ballCollision"), request.queryParams("fishCollision"),
                    request.queryParams("ballFishCollision"));
            return "";
        });

        get("/update", (request, response) -> gson.toJson(dis.updateBallWorld()));

        post("/remove", (request, response) -> {
            dis.removeSubsetListeners(gson.fromJson(request.queryParams("selections"), ArrayList.class));
            return "";
        });

        post("/canvas/dims", (request, response) -> {
            DispatchAdapter.setCanvasDims(new Point(Integer.parseInt(request.queryParams("width")),
                    Integer.parseInt(request.queryParams("height"))));
            System.out.print("Canvas dimensions set in model");
            return "Canvas dimensions set in model";
        });

        get("/clear", (request, response) -> {
            dis.removeListeners();
            return "";
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
