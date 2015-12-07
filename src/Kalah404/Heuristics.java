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

    public static int getScore(Node node) {
        Board var0 = node.getBoard();
        Side var1 = node.getSide();
        int var2 = 0;
        int var4 = var0.getSeedsInStore(var1);
        int var6 = var0.getSeedsInStore(var1.opposite());
        if((var4 != 0.0D || var6 != 0.0D) && var4 != var6) {
            int var8;
            int var10;
            if(var4 > var6) {
                var8 = var4;
                var10 = var6;
            } else {
                var8 = var6;
                var10 = var4;
            }

            var2 = (1 / var8 * (var8 - var10) + 1) * var8;
            if(var6 > var4) {
                var2 *= -1.0D;
            }
        }

        int var12;
        for(var12 = 1; var12 <= var0.getNoOfHoles(); ++var12) {
            if(var0.getSeeds(var1, var12) == 0 && isSeedable(var0, var1, var12)) {
                var2 += (var0.getSeedsOp(var1, var12) / 2);
            }
        }

        for(var12 = 1; var12 <= var0.getNoOfHoles(); ++var12) {
            if(var0.getNoOfHoles() - var12 + 1 == var0.getSeeds(var1, var12)) {
                ++var2;
            }
        }

        var12 = 0;

        int var9;
        for(var9 = 1; var9 <= var0.getNoOfHoles(); ++var9) {
            var12 += var0.getSeeds(var1, var9);
        }

        var9 = 0;

        int var13;
        for(var13 = 1; var13 <= var0.getNoOfHoles(); ++var13) {
            var9 += var0.getSeeds(var1.opposite(), var13);
        }

        var13 = var12 - var9;
        var2 += (var13 / 2);

        for(int var11 = 1; var11 <= var0.getNoOfHoles(); ++var11) {
            if(var0.getSeeds(var1.opposite(), var11) == 0 && isSeedable(var0, var1.opposite(), var11)) {
                var2 -= (var0.getSeedsOp(var1.opposite(), var11) / 2);
            }
        }

        if(var1 != Side.mySide){
            var2 *= -1.0D;
        }

        return var2;
    }

    public static boolean isSeedable(Board var0, Side var1, int var2) {
        boolean var3 = false;

        for(int var4 = var2 - 1; var4 > 0; --var4) {
            if(var2 - var4 == var0.getSeeds(var1, var4)) {
                var3 = true;
                break;
            }
        }

        return var3;
    }


/*
    public static int getScore(Node node) {

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
    */
}
