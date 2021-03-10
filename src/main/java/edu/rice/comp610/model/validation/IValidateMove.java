package edu.rice.comp610.model.validation;

import edu.rice.comp610.model.game.Game;
import java.util.ArrayList;

/**
 * IValidateMode interface defines what behavior the validation class should have
 * that the Chess@Rice platform is using to verify whether moves are appropriate
 *
 * The Interface will be a singleton validator, so methods will accept the particular
 * game
 */
public interface IValidateMove {

    /**
     * Method: Check If Legal
     * Given the chess notation of the current piece location, the desired piece location,
     * and the game (which will contain the board layout), determine whether or not the
     * move is acceptable.
     * @param fromLoc String of the current piece location, in chess notation (e.g "e4")
     * @param toLoc String of the desired piece location, in chess notation (e.g "e4")
     * @param game The Game object that will have a HashMap<String, Piece> containing
     *             positions of all the pieces on the board.
     * @return 0 if true, 1 if no piece selected, 2 if piece of opposite team selected,
     * 3 if moving onto square of own piece, 4 if chess logic error.
     */
    int checkIfLegal(String fromLoc, String toLoc, Game game);

    /**
     * Method: List of Legal Moves.
     * For a given piece (moves will be constrained by the class of the piece), return
     * a list of all the valid moves.
     * @param fromLoc String of the current piece location, in chess notation (e.g "e4")
     * @param game The Game object that will have a HashMap<String, Piece> containing
     *      *             positions of all the pieces on the board.
     * @return An ArrayList<String> of all the valid positions to move to.
     */
    ArrayList<String> listOfLegalMoves(String fromLoc, Game game);
}
