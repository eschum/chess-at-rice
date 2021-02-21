package edu.rice.comp610.model.piece;

public class Pawn extends Piece {

    public Pawn(String location, int team) {
        super(location, team);

        if (team == 0) {
            setImage("plt60.png");
        } else {
            setImage("pdt60.png");
        }
    }


}


