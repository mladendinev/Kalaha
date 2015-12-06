package MKAgent;

import java.util.List;
import java.util.Random;

/**
 * Created by gmtuca on 04/12/15.
 */
public class Heuristics {

    static Random random = new Random();

    public static int monteCarlo(KalahaNode node, int timesRun){

        int average = 0;

        for(int i = 0; i < timesRun; i++){
            average += monteCarlo(node);
        }

        average /= timesRun;

        return average;
    }

    private static int monteCarlo(KalahaNode node){

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

    public static int getScore(KalahaNode node) {

        Board board = node.getBoard();
        Side side = node.getSide();

        int score = 0;

        int[] heuristics = new int[4];
        heuristics[0] = node.getEvaluationFunction();

        heuristics[1] = holeCapture(board, side);

        heuristics[2] = canGetExtraTurn(board, side);

        heuristics[3] = howCloseIsMyOpponentToWinning(board, side);

        // {4,0,20} - draw
        double[] weights = {1.0,1.0,1.0, 0.0};

        for (int index = 0; index < weights.length; index++) {
            score += (double) (heuristics[index] * weights[index]);
        }



        /*int mySeeds = board.getSeedsInStore(Side.mySide);
        int oppSeeds = board.getSeedsInStore(Side.mySide.opposite());

        int seedsDiff = mySeeds - oppSeeds;

        int captureScore = 0;
        for(int hole = 1; hole <= board.getNoOfHoles(); hole++) {
            if(canCapture(board, side, hole)) {
                captureScore += (board.getSeedsOp(side, hole)) * 3;
            }
        }

        int extraTurnScore = 0;
        if(canGetExtraTurn(board, side)){
            extraTurnScore += 4;
        }

        if(side == Side.mySide.opposite()){
            captureScore *= -1;
            extraTurnScore *= -1;
        }

        score += seedsDiff;
        score += captureScore;
        score += extraTurnScore;
        score += (board.getSeedsOnSide(Side.mySide) - board.getSeedsOnSide(Side.mySide.opposite()))/ 3.0;*/

        //int monte = monteCarlo(node, 10);

        //score += monte;

        return score;
    }

    /*public static boolean canGetExtraTurn(Board board, Side side){
        return isSeedable(board, side, 0);
    }*/

    public static int canGetExtraTurn(Board board, Side side) {
        int kalahaLocation = 8;
        for (int hole = 1; hole < 8; hole++) {
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
        for (int index = 1; index < 8; index++) {
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
