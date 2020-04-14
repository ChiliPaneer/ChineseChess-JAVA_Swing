package GameLogic;

import GameLogic.Pieces.Piece;

/**
 * @version 4/10/20
 */
public class MoveChecker {
    private Board board;
    private Move move;


    private int obstacleCount; //number of pieces in the way
    private boolean isClear; //if obstacles = 0
    private boolean isCheck; //checks the board for check (probably gonna make this a separate class at this point

    private boolean attack;

    private boolean legal;

    MoveChecker(Board board, Move move) {
        this.board = board;
        this.move = move;
        this.legal = true;

        //  1. first check if movement pattern is legal (ie horse moves 1 up 2 left)
        CheckPiece();

        //  2. check if we are doing an attack, and also check if the end point is blocked by a friendly piece
        if (legal) {
            isAttack();
        }

        //  3. Check if the path is clear, if not See if we're an attacking cannon
        obstacleStats();
        if(!isClear){
            if (board.getPoint(move.getOriginX(), move.getOriginY()).getPiece().toString().equals("Cannon")) {
                if (!(obstacleCount == 1 && attack)) {
                    legal = false;
                }
            } else {
                legal = false;
            }
        }


        //check if we have a clear path, and if not how many obstacles we have (cannon and horse special cases

        //check if the generals are facing each other. (make the general coordinates board member data? bruh wtf checking for check is so trippy
        /*brainstorming the check method thing:
         1. Obviously we have to check every enemy piece to see if it can put the current players king in check.
         2. We'll use another class called tryCheck that runs MoveChecker on every enemy piece for a move that could try and attack the king. If everything fails the MoveCheck(becasue pinned can check) then we're good.

         */
    }


    /**
     * Checks if the move pattern is legal
     * If not, terminates the process
     */
    public void CheckPiece() {
        board.getPoint(move.getOriginX(), move.getOriginY()).getPiece().doMove(move);
        //checkPiece = true;
        if (!move.isValid()) {
            this.legal = false;
        }
    }

    /**
     * Checks the destination piece to see if we're attacking or self blocked
     * If we're self blocked, we terminate the whole operation because it's definitely illegal
     */
    public void isAttack() {
        if (board.getPoint(move.getFinalX(), move.getFinalY()).getPiece() == null) {
            attack = false;
        } else {
            Piece.Side origin = board.getPoint(move.getOriginX(), move.getOriginY()).getPiece().getSide();
            Piece.Side dest = board.getPoint(move.getFinalX(), move.getFinalY()).getPiece().getSide();
            if (origin != dest) {
                attack = true;
            }
            if (origin == dest) {
                this.attack = false;
                this.legal = false;
                //this.isClear = false; //shouldn't matter because illegal anyway now
            }
        }

    }

    /**
     * Finds out if there are obstacles, and if so, how many?
     * Useful for seeing if we have one obstacle for the cannon
     * Useful for seeing if a piece is blocked, handles knights as well.
     */
    public void obstacleStats() {

        isClear = true;
        this.obstacleCount = 0;

        //vertical move
        if (move.isVertical()) {
            for (int y = move.getOriginY() + 1; y < move.getFinalY(); y++) {
                if (board.getPoint(move.getOriginX(), y).getPiece() != null) {
                    obstacleCount++;
                }
            }

        }
        //horizontal move
        else if (move.isHorizontal()) {
            for (int x = move.getOriginX() + 1; x < move.getFinalX(); x++) {
                if (board.getPoint(x, move.getOriginY()).getPiece() != null) {
                    obstacleCount++;
                }
            }
            //diagonal move
        } else if (move.isDiagonal()) {
            for (int x = move.getOriginX() + 1; x < move.getFinalX(); x++) {
                for (int y = move.getOriginY() + 1; y < move.getFinalY(); y++) {
                    if (board.getPoint(x, y).getPiece() != null) {
                        obstacleCount++;
                    }
                }
            }
        }
        //only for knights, as only knights have non linear moves. Knights are only blocked by the nearest pieces.
        else {

            if (move.getDx() == 2) {
                if (board.getPoint(move.getOriginX() + 1, move.getOriginY()).getPiece() != null) {
                    obstacleCount++;
                }
            }
            else if (move.getDx() == -2) {
                if (board.getPoint(move.getOriginX() - 1, move.getOriginY()).getPiece() != null) {
                    obstacleCount++;
                }
            }
            else if (move.getDy() == 2) {
                if (board.getPoint(move.getOriginX(), move.getOriginY() + 1).getPiece() != null) {
                    obstacleCount++;
                }
            }
            else if (move.getDy() == -2) {
                if (board.getPoint(move.getOriginX() + 1, move.getOriginY() - 1).getPiece() != null) {
                    obstacleCount++;
                }
            }


        }
        if (obstacleCount != 0) {
            isClear = false;
        }



    }
}