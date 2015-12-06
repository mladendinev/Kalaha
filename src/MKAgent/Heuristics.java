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

        int mySeeds = board.getSeedsInStore(Side.mySide);
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
        score += (board.getSeedsOnSide(Side.mySide) - board.getSeedsOnSide(Side.mySide.opposite()))/ 3.0;

        //int monte = monteCarlo(node, 10);

        //score += monte;

        return score;
    }

    public static boolean canGetExtraTurn(Board board, Side side){
        return isSeedable(board, side, 0);
    }

    public static boolean canCapture(Board board, Side side, int hole){
        return board.getSeeds(side, hole) == 0 && isSeedable(board, side, hole);
    }

    public static boolean isSeedable(Board board, Side side, int hole) {

        for(int i = hole - 1; i >= 1; --i) {
            if(hole - i == board.getSeeds(side, i)) {
                return true;
            }
        }

        return false;
    }
}
