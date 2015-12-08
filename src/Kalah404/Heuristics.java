package Kalah404;

import java.util.List;
import java.util.Random;

/**
 * Created by gmtuca on 04/12/15.
 */
public class Heuristics {

    static Random random = new Random();

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

    public static double getScore(Node node) {
        Board board = node.getBoard();
        Side side = node.getSide();
        double score = 0.0D;
        double mySeedsInKalah = (double)board.getSeedsInStore(side);
        double opponentSeedsInKalah = (double)board.getSeedsInStore(side.opposite());

        if((mySeedsInKalah != 0.0D || opponentSeedsInKalah != 0.0D) && mySeedsInKalah != opponentSeedsInKalah) {
            double greaterSeedsInKalah;
            double smallerSeedsInKalah;
            if(mySeedsInKalah > opponentSeedsInKalah) {
                greaterSeedsInKalah = mySeedsInKalah;
                smallerSeedsInKalah = opponentSeedsInKalah;
            } else {
                greaterSeedsInKalah = opponentSeedsInKalah;
                smallerSeedsInKalah = mySeedsInKalah;
            }

            score = (1.0D / greaterSeedsInKalah * (greaterSeedsInKalah - smallerSeedsInKalah) + 1.0D) * greaterSeedsInKalah;
            if(opponentSeedsInKalah > mySeedsInKalah) {
                score *= -1.0D;
            }
        }

        int i;
        for(i = 1; i <= board.getNoOfHoles(); ++i) {
            if(board.getSeeds(side, i) == 0 && isSeedable(board, side, i)) {
                score += (double)(board.getSeedsOp(side, i) / 2);
            }
        }

        for(i = 1; i <= board.getNoOfHoles(); ++i) {
            if(board.getNoOfHoles() - i + 1 == board.getSeeds(side, i)) {
                ++score;
            }
        }

        i = 0;

        int var9;
        for(var9 = 1; var9 <= board.getNoOfHoles(); ++var9) {
            i += board.getSeeds(side, var9);
        }

        var9 = 0;

        int var13;
        for(var13 = 1; var13 <= board.getNoOfHoles(); ++var13) {
            var9 += board.getSeeds(side.opposite(), var13);
        }

        var13 = i - var9;
        score += (double)(var13 / 2);

        for(int var11 = 1; var11 <= board.getNoOfHoles(); ++var11) {
            if(board.getSeeds(side.opposite(), var11) == 0 && isSeedable(board, side.opposite(), var11)) {
                score -= (double)(board.getSeedsOp(side.opposite(), var11) / 2);
            }
        }

        if(side != Side.mySide){
            score *= -1;
        }

        return score;
    }

    /*public static boolean canGetExtraTurn(Board board, Side side){
        return isSeedable(board, side, 0);
    }*/

    public static int canGetExtraTurn(Board board, Side side) {
        int kalahaLocation = 8;
        for (int hole = 1; hole <= 7; hole++) {
            if (board.getSeeds(side, hole) == (kalahaLocation - hole)) {
                return 1;
            }
        }
        return 0;
    }

    /*public static boolean canCapture(Board board, Side side, int hole){
        return board.getSeeds(side, hole) == 0 && isSeedable(board, side, hole);
    }*/

    public static int holeCapture(Board board, Side side) {
        for (int index = 1; index <= 7; index++) {
            // Check to see if there are any holes which have 0 seeds in them
            if (board.getSeeds(side, index) == 0 ) {
                // If there are check to see if we can capture there
                for (int hole = 1; hole < index; hole ++) {
                    if (board.getSeeds(side, hole) == (index - board.getSeeds(side, hole))) {
                        return board.getSeeds(side.opposite(), index) + 1;
                    }
                }
            }
        }

        return 0;
    }

    public static boolean isSeedable(Board board, Side side, int hole) {

        for(int i = hole - 1; i >= 1; --i) {
            if(hole - i == board.getSeeds(side, i)) {
                return true;
            }
        }

        return false;
    }

    private static int howCloseIsMyOpponentToWinning(Board board, Side side) {
        int numberOfSeedsInHisKalah = board.getSeedsInStore(side.opposite());
        return 50 - numberOfSeedsInHisKalah;

    }
}
