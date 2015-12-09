package Kalah404;

import java.util.List;
import java.util.Random;

/**
 * Created by gmtuca on 04/12/15.
 */
public class Heuristics {

    static Random random = new Random();

    private static final int KALAHA_LOCATION = 8;

    public static int monteCarlo(Node node, int timesRun){

        int average = 0;

        for(int i = 0; i < timesRun; i++){
            average += monteCarlo(node);
        }

        average /= timesRun;

        return average;
    }

    public static int monteCarlo(Node node){

        Board board = new Board(node.getBoard());
        Side side = node.getSide();

        while(!Kalah.gameOver(board)){

            List<Integer> validHoles = board.getValidHoles(side);

            int randomHole = validHoles.get(random.nextInt(validHoles.size()));

            Move move = new Move(side, randomHole);

            side = Kalah.makeMove(board, move);
        }

        return board.getSeedsInStore(Side.mySide) - board.getSeedsInStore(Side.mySide.opposite()); //todo have this outside
    }

    public static int getScore(Node node) {

        Board board = node.getBoard();
        Side side = node.getSide();

        int score = 0;

        int numberOfSeedsInMyKalaha = board.getSeedsInStore(side);
        int numberOfSeedsInHisKalaha = board.getSeedsInStore(side.opposite());


        if ( (numberOfSeedsInMyKalaha != 0 || numberOfSeedsInHisKalaha != 0) && numberOfSeedsInHisKalaha != numberOfSeedsInMyKalaha ) {
            int winning;
            int losing;
            if(numberOfSeedsInMyKalaha > numberOfSeedsInHisKalaha) {
                winning = numberOfSeedsInMyKalaha;
                losing = numberOfSeedsInHisKalaha;
            } else {
                winning = numberOfSeedsInHisKalaha;
                losing = numberOfSeedsInMyKalaha;
            }

            score = (1 / winning * (winning - losing) + 1) * winning;

            // If I am losing make this a negative value
            if(numberOfSeedsInHisKalaha > numberOfSeedsInMyKalaha) {
                score *= -1.0D;
            }
        }

        int captures = holeCapture(board,side);

        score += captures;

        int extraTurns = canGetExtraTurn(board, side);

        score += extraTurns;

        // Make sure we have more seeds on our side

        int numberOfSeedsOnMySide = numberOfSeedsOnSide(board, side);
        int numberOfSeedsOnHisSide = numberOfSeedsOnSide(board,side.opposite());

        int scoreDifference = numberOfSeedsOnMySide - numberOfSeedsOnHisSide;

        score += (scoreDifference / 2);


        // Consider how many seeds he can steal
        int howManyHeCanSteal = holeCapture(board, side.opposite());
        score -= howManyHeCanSteal;

        if (side != Side.mySide)
        {
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
