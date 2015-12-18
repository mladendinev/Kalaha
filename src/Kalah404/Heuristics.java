package Kalah404;

/**
 * Created by gmtuca on 04/12/15.
 */
public class Heuristics {

    private static final int KALAH_LOCATION = 8;

    private static final double EXTRA_TURN_WEIGHT  = 4.0;
    private static final double CAPTURE_WEIGHT     = 0.5;

    public static int getScore(Node node) {

        Board board = node.getBoard();
        Side side = node.getSide();

        int score = 0;

        score += holeCapture(board,side) * CAPTURE_WEIGHT;

        if(canGetExtraTurn(board, side)){
            score += EXTRA_TURN_WEIGHT;
        }

        if (side != Side.mySide) {
            score *= -1.0D;
        }

        score += node.getEvaluationFunction();

        return score;
    }

    public static boolean canGetExtraTurn(Board board, Side side) {
        for (int hole = 1; hole < KALAH_LOCATION; hole++) {
            if (board.getSeeds(side, hole) == (KALAH_LOCATION - hole)) {
                return true;
            }
        }
        return false;
    }

    public static int holeCapture(Board board, Side side) {
        //The sum of all stones which can be captured at this point

        int capturesPossible = 0;
        for (int i = 1; i < KALAH_LOCATION; i++) {
            // Check to see if there are any holes which have 0 seeds in them
            if (board.getSeeds(side, i) == 0 && canPutLastSeedHere(board, side, i)) {
                capturesPossible += board.getSeedsOp(side, i) + 1;
            }
        }

        return capturesPossible;
    }


    public static boolean canPutLastSeedHere(Board board, Side side, int hole) {
        for (int index = 1; index < hole; index++) {
            if (board.getSeeds(side, index) == (hole - index)) {
                return true;
            }
        }
        return false;
    }

}
