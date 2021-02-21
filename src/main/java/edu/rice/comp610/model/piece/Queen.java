package edu.rice.comp610.model.piece;

public class Queen extends Piece {

    public Queen(String location, int team) {
        super(location, team);

        if (team == 0) {
            setImage("qlt60.png");
        } else {
            setImage("qdt60.png");
        }
    }


}


