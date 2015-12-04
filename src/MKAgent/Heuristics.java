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

            //if(validHoles.isEmpty()){
            //    break; //todo ????
            // }

            int randomHole = validHoles.get(random.nextInt(validHoles.size()));

            Move move = new Move(side, randomHole);

            side = Kalah.makeMove(board, move);
        }

        return board.getSeedsInStore(Side.mySide) - board.getSeedsInStore(Side.mySide.opposite()); //todo have this outside
    }
}
