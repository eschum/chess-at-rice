package edu.rice.comp610.model.piece;

public class King extends Piece {

    public King(String location, int team) {
        super(location, team);

        if (team == 0) {
            setImage("klt60.png");
        } else {
            setImage("kdt60.png");
        }
    }


}


