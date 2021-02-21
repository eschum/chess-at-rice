package edu.rice.comp610.model.piece;

public class Knight extends Piece {

    public Knight(String location, int team) {
        super(location, team);

        if (team == 0) {
            setImage("nlt60.png");
        } else {
            setImage("ndt60.png");
        }
    }


}


