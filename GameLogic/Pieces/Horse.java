package GameLogic.Pieces;

import GameLogic.Move;

/**
 * Horse Piece
 */
public class Horse extends Piece {
    public Horse(Side side) {
        super(side);
        this.type = "Horse";
        this.canWinAlone = true;
    }

    @Override

    public void checkPattern(Move move) {
        super.checkPattern(move);

        if (!((Math.abs(move.getDx()) == 1 && Math.abs(move.getDy()) == 2) || (Math.abs(move.getDx()) == 2 && Math.abs(move.getDy()) == 1))) {
            move.setValid(false);
        }
    }
}
