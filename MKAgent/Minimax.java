package MKAgent;

import java.util.List;
import java.util.Random;

/**
 * Created by mbax2md2 on 03/12/15.
 */
public class Minimax {

    public static int minimax(KalahaNode node, int depth){
        int bestValue = 0;
        int value = 0;
        if (depth == 0 || node.isLeafNode()){
            return node.getEvaluationFunction();
        }

        if (Side.myTurn(node.getSide())){
            bestValue = Integer.MIN_VALUE;

            for (KalahaNode child: node.getChildren().values()){
                value = minimax(child,depth -1);
                bestValue = Math.max(bestValue,value);

            }
            return bestValue;
        }
        else{
            bestValue = Integer.MAX_VALUE;

            for (KalahaNode child: node.getChildren().values()){
                value = minimax(child,depth -1);
                bestValue = Math.min(bestValue, value);
            }
            return bestValue;
        }
    }

    static Random random = new Random();

    public static int averageMonteCarlo(KalahaNode node, int timesRun){

        int average = 0;

        for(int i = 0; i < timesRun; i++){
            average += monteCarlo(node);
        }

        average /= timesRun;

        return average;
    }

    public static int monteCarlo(KalahaNode node){

        Board board = new Board(node.getBoard());
        Side side = node.getSide();

        while(!Kalah.gameOver(board)){

            List<Integer> validHoles = board.getValidHoles(side);

            //if(validHoles.isEmpty()){
            //    break; //todo ????
           // }

            int randomIndex = random.nextInt(validHoles.size());
            int randomHole = validHoles.get(randomIndex);

            Move move = new Move(side, randomHole);

            side = Kalah.makeMove(board, move);
        }
        //System.err.println(board);

        return board.getSeedsInStore(Side.mySide) - board.getSeedsInStore(Side.mySide.opposite()); //todo have this outside
    }

    public static int alphabeta(KalahaNode node, int depth, int alpha, int beta) {

        if (depth == 0 || node.isLeafNode()){
            return averageMonteCarlo(node, 1);
            //return node.getEvaluationFunction();
        }

        int value = 0;

        if (Side.myTurn(node.getSide())) {
            value = Integer.MIN_VALUE;
            for (KalahaNode child: node.getChildren().values()){
                value = Math.max(value, alphabeta(child,depth -1, alpha, beta));
                alpha = Math.max(alpha, value);
                if ( beta <= alpha) {
                    break;
                }

            }
            return value;
        }
        else {
            value = Integer.MAX_VALUE;
            for (KalahaNode child: node.getChildren().values()){
                value = Math.min(value, alphabeta(child, depth - 1, alpha, beta));
                alpha = Math.min(beta, value);
                if ( beta <= alpha) {
                    break;
                }

            }
            return value;

        }

    }
}
