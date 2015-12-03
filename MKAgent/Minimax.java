package MKAgent;

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

            for (KalahaNode child: node.getChildren()){
                value = minimax(child,depth -1);
                bestValue = Math.max(bestValue,value);

            }
            return bestValue;
        }
        else{
            bestValue = Integer.MAX_VALUE;

            for (KalahaNode child: node.getChildren()){
                value = minimax(child,depth -1);
                bestValue = Math.min(bestValue, value);
            }
            return bestValue;
        }
    }

    public static int alphabeta(KalahaNode node, int depth, int alpha, int beta) {

        int value = 0;
        if (depth == 0 || node.isLeafNode()){
            return node.getEvaluationFunction();
        }

        if (Side.myTurn(node.getSide())) {
            value = Integer.MIN_VALUE;
            for (KalahaNode child: node.getChildren()){
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
            for (KalahaNode child: node.getChildren()){
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
