package edu.rice.comp610.model.message;

import edu.rice.comp610.model.game.Game;
import edu.rice.comp610.model.game.Player;

public class StartGame extends Message {
    private Game game;

    public StartGame(Game game) {
        type = "start_game";
        this.game = game;

    }
}
