package edu.rice.comp610.model.piece;

import edu.rice.comp610.model.game.Player;

/**
 * Class: Piece
 * Superclass for all chess piece objects.
 * Implements the IPiece interface
 */
public class Piece implements IPiece{
    private String boardLoc;
    private String image;
    private final int team;  //0 for light, 1 for dark.

    /**
     * Public constructor
     * @param location The location of the piece in chess notation
     * @param team Integer of the team, 0 for light, 1 for dark
     */
    public Piece(String location, int team) {
        updateLoc(location);
        this.team = team;
    }

    /**
     * Method: If Taken
     * Defines behavior the piece should have if it is taken by the opponent.
     * Delegates to the subclasses to take individual action.
     * @param opponent The Piece that has taken the subject piece.
     */
    public void ifTaken(Piece opponentPiece, Player opponent) {}

    /**
     * Method: UpdateLoc
     * Store the chess location notation in a string.
     * The view will perform relevant calculations for rendering. In the Model, we just
     * need to store the location in chess notation.
     * @param boardLocation The new location of the piece, in chess notation.
     */
    public void updateLoc(String boardLocation) {
        this.boardLoc = boardLocation;
    }

    /**
     * Method: Set Image
     * Accessor method to set the image string.
     * @param img String that stores the file location for the piece.
     */
    public void setImage(String img) {
        this.image = img;
    }

    /**
     * Accessor method to return the team of the piece.
     * 0 for light team, 1 for dark team
     * @return an integer of the team (0 - light, 1 - dark)
     */
    public int getTeam() {
        return team;
    }
}
