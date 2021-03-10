package edu.rice.comp610.model.validation;

import edu.rice.comp610.model.game.Game;
import edu.rice.comp610.model.piece.Piece;
import org.eclipse.jetty.websocket.api.Session;
import java.util.ArrayList;

/**
 * Class: ValidateMove
 * Checks if a chess move is valid. Implements the IValidateMove class.
 *
 */
public class ValidateMove implements IValidateMove{
    private static ValidateMove ref;

    private ValidateMove() {}

    public static ValidateMove getInstance() {
        if (ref == null) ref = new ValidateMove();
        return ref;
    }

    /**
     * Method: Check If Legal
     * Implements IValidateMove requirement to check if the piece move is legal.
     * @param fromLoc String of the current piece location, in chess notation (e.g "e4")
     * @param toLoc String of the desired piece location, in chess notation (e.g "e4")
     * @param game The Game object that will have a HashMap<String, Piece> containing
     *             positions of all the pieces on the board.
     * @return 0 if true, 1 if no piece selected, 2 if piece of opposite team selected,
     * 3 if moving onto square of own piece, 4 if chess logic error.
     */
    public int checkIfLegal(String fromLoc, String toLoc, Game game) {
        return 0;
    }

    /**
     * Method: List of Legal Moves.
     * For a given piece (moves will be constrained by the class of the piece), return
     * a list of all the valid moves.
     * @param fromLoc String of the current piece location, in chess notation (e.g "e4")
     * @param game The Game object that will have a HashMap<String, Piece> containing
     *      *             positions of all the pieces on the board.
     * @return An ArrayList<String> of all the valid positions to move to.
     */
    public ArrayList<String> listOfLegalMoves(String fromLoc, Game game) {
        return null;
    }

    /**
     * Method: Is Piece Selected
     *
     * @param selectedPiece
     * @param game
     * @return
     */
    boolean isPieceSelected(Piece selectedPiece, Game game) {
        return false;
    }

    boolean isSelectedPieceCorrectTeam(Piece selectedPiece, Game game, Session userSession) {
        return false;
    }

    boolean isTargetSquareOccupiedBySelf(String toLoc) {
        return false;
    }
}
