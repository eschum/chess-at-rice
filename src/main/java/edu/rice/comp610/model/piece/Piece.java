package edu.rice.comp610.model.piece;

import edu.rice.comp610.model.DispatchAdapter;

import java.awt.*;

public class Piece {
    private Point loc;
    private String boardLoc;
    private String image;
    private int team;  //0 for light, 1 for dark.

    public Piece(String location, int team) {
        this.loc = new Point(0, 0);
        updateLoc(location);
        this.team = team;
    }

    /**
     * Populate the X and Y canvas coordinates for the piece.
     * 0-indexed, starting from upper left.
     * @param boardLocation
     */
    public void updateLoc(String boardLocation) {
        this.boardLoc = boardLocation;

        int horizontal = (int) boardLoc.charAt(0) - 97;
        int vertical = Integer.parseInt(boardLoc.charAt(1)+"");

        this.loc.x = horizontal * (DispatchAdapter.side / 8) + DispatchAdapter.side / 16;
        this.loc.y = (8 - vertical) * (DispatchAdapter.side / 8) + DispatchAdapter.side / 16;
    }

    public void setImage(String img) {
        this.image = img;
    }

    /**
     * Accessor method to return the team of the piece.
     * 0 for light team, 1 for dark team
     * @return
     */
    public int getTeam() {
        return team;
    }
}
