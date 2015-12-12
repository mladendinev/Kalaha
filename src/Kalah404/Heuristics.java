package Kalah404;

import java.util.List;
import java.util.Random;

/**
 * Created by gmtuca on 04/12/15.
 */
public class Heuristics {

    private static final int KALAHA_LOCATION = 8;

    public static int getScore(Node node) {

        Board board = node.getBoard();
        Side side = node.getSide();

        int numberOfSeedsInMyKalaha = board.getSeedsInStore(side);
        int numberOfSeedsInHisKalaha = board.getSeedsInStore(side.opposite());

        int score = numberOfSeedsInMyKalaha - numberOfSeedsInHisKalaha;

        int captures = holeCapture(board,side);

        score += captures;

        int extraTurns = canGetExtraTurn(board, side);

        score += extraTurns;


        if (side != Side.mySide) {
            score *= -1.0D;
        }

        return score;
    }

    public static int canGetExtraTurn(Board board, Side side) {
        int numberOfExtraTurns = 0;
        for (int hole = 1; hole < KALAHA_LOCATION; hole++) {
            if (board.getSeeds(side, hole) == (KALAHA_LOCATION - hole)) {
                numberOfExtraTurns++;
            }
        }
        return numberOfExtraTurns;
    }


    public static int holeCapture(Board board, Side side) {

        int capturesPossible = 0;
        for (int index = 1; index < KALAHA_LOCATION; index++) {
            // Check to see if there are any holes which have 0 seeds in them
            if (board.getSeeds(side, index) == 0 && canPutLastSeedHere(board, side, index)) {
                capturesPossible += (board.getSeedsOp(side, index) / 2);
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

    private static int numberOfSeedsOnSide(Board board, Side side) {
        int numberOfSeeds  = 0;
        for(int index = 1; index <= board.getNoOfHoles(); ++index) {
            numberOfSeeds += board.getSeeds(side, index);
        }
        return numberOfSeeds;
    }

}
