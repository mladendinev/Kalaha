package MKAgent;

import java.util.List;
import java.util.Random;

/**
 * Created by mbax2md2 on 03/12/15.
 */
public class Minimax {

    public static int alphabeta(KalahaNode node, int depth, int alpha, int beta) {

        if (depth == 0){
            return Heuristics.monteCarlo(node, 30);
        }

        int value;

        if (Side.myTurn(node.getSide())) {
            value = Integer.MIN_VALUE;
            for (KalahaNode child: node.getChildren().values()){
                value = Math.max(value, alphabeta(child,depth -1, alpha, beta));
                alpha = Math.max(alpha, value);
                if ( beta <= alpha) {
                    break;
                }
            }
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
        }

        return value;
    }
}
