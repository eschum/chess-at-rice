package edu.rice.comp610.model.piece;

public class Bishop extends Piece {

    public Bishop(String location, int team) {
        super(location, team);

        if (team == 0) {
            setImage("blt60.png");
        } else {
            setImage("bdt60.png");
        }
    }


}


