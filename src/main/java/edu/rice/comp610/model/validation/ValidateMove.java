package edu.rice.comp610.model.validation;

import edu.rice.comp610.model.game.Game;
import edu.rice.comp610.model.game.Player;
import edu.rice.comp610.model.piece.Piece;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import java.util.ArrayList;

/**
 * Class: ValidateMove
 * Design Pattern: Singleton
 * Checks if a chess move is valid. Implements the IValidateMove class
 */
public class ValidateMove implements IValidateMove{
    private static ValidateMove ref;

    /**
     * Private Constructor
     * Implements the Singleton design pattern
     */
    private ValidateMove() {}

    /**
     * Method: Get Instance
     * Implements the Singleton design pattern by returning the static instance of the ValidateMove class
     * @return The static instance of the ValidateMove class
     */
    public static ValidateMove getInstance() {
        if (ref == null) ref = new ValidateMove();
        return ref;
    }

    /**
     * Method: Check If Legal
     * Implements IValidateMove requirement to check if the piece move is legal.
     * @param fromLoc String of the current piece location, in chess notation (e.g "e4")
     * @param toLoc String of the desired piece location, in chess notation (e.g "e4")
     * @param player The Player context of the person moving the piece
     * @param game The Game object that will have a HashMap<String, Piece> containing
     *             positions of all the pieces on the board.
     * @return 0 if true, 1 if no piece selected, 2 if piece of opposite team selected,
     * 3 if moving onto square of own piece, 4 if chess logic error.
     */
    public Pair<Integer, String> checkIfLegal(String fromLoc, String toLoc, Player player, Game game) {
        Piece selectedPiece = game.getPieceFromPositions(fromLoc);
        Piece targetPiece = game.getPieceFromPositions(toLoc);

        //Check if a Piece is selected.
        if (!isPieceSelected(selectedPiece))
            return new ImmutablePair<>(1, "A piece was not selected");

        //Check if the piece is the same team as the player.
        else if (!isSelectedPieceCorrectTeam(selectedPiece, game, player))
            return new ImmutablePair<>(2, "Selecting piece of opposite team is not allowed");

        //Check if the target location is already occupied by self.
        else if (isTargetSquareOccupiedBySelf(selectedPiece, targetPiece))
            return new ImmutablePair<>(3, "Cannot move onto square already occupied by own piece");

        /*
        Extensibility built-in.
        Can implement a number of additional checks, including logic specific to the type of piece, dynamically
        dispatched to the piece type.
         */

        return null;
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
     * Determine whether or not there is a piece selected.
     * @param selectedPiece The Piece (referenced from Game positions map)
     * @return True if there is a Piece, False if null.
     */
    boolean isPieceSelected(Piece selectedPiece) {
        return !(selectedPiece == null);  //Boolean Zen!
    }

    /**
     * Method: Is Selected Piece Correct Team.
     * Determine if the piece selected is of the same team as the current player.
     * @param selectedPiece The piece selected for movement
     * @param game The current game context
     * @param player The player that is moving the piece
     * @return true if the piece is of the same player, false if not.
     */
    boolean isSelectedPieceCorrectTeam(Piece selectedPiece, Game game, Player player) {
        return (game.getLightPlayer() == player && selectedPiece.getTeam() == 0) ||
                (game.getDarkPlayer() == player && selectedPiece.getTeam() == 1);
    }

    /**
     * Method: Is Target Square Occupied By Self
     * @param selectedPiece The Piece that is being moved.
     * @param targetPiece The piece on the square that is targeted.
     * @return true if there is a piece of the same team there. false if not.
     */
    boolean isTargetSquareOccupiedBySelf(Piece selectedPiece, Piece targetPiece) {
        return targetPiece != null && targetPiece.getTeam() == selectedPiece.getTeam();
    }
}
