package edu.rice.comp610.model.piece;

public class Rook extends Piece {

    public Rook(String location, int team) {
        super(location, team);

        if (team == 0) {
            setImage("rlt60.png");
        } else {
            setImage("rdt60.png");
        }
    }


}


