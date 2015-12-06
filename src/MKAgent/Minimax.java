package MKAgent;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by mbax2md2 on 03/12/15.
 */
public class Minimax {

    static class TableEntry {
        public int lowerBound = Integer.MIN_VALUE;
        public int upperBound = Integer.MAX_VALUE;

        public TableEntry(){}
    }

    public static final Map<KalahaNode, TableEntry> transpositionalTable = new HashMap<KalahaNode, TableEntry>();

    public static int alphabeta(KalahaNode node, int depth, int alpha, int beta) {

        TableEntry te;
        synchronized (transpositionalTable){
            te = transpositionalTable.get(node);
        }

        if(te != null){
            //System.err.println("H");
            if(te.lowerBound >= beta){
                return te.lowerBound;
            }
            if(te.upperBound <= alpha){
                return te.upperBound;
            }
            alpha = Math.max(alpha, te.lowerBound);
            beta = Math.min(beta, te.upperBound);
        }
        else{
            //System.err.println("-");
        }

        if (depth == 0){
            //return Heuristics.monteCarlo(node, 50);
            //return Heuristics.getScore(node);
            return node.getEvaluationFunction();

            //return node.getBoard().getSeedsInStore(Side.mySide) - 50;
        }

        if(Kalah.gameOver(node.getBoard())){
            return node.getEvaluationFunction();
            //return Heuristics.getScore(node);
        }

        int g;

        if (Side.myTurn(node.getSide())) {
            g = Integer.MIN_VALUE;
            int a = alpha;

            for (KalahaNode child: node.getChildrenSorted()){
                g = Math.max(g, alphabeta(child, depth -1, a, beta));
                a = Math.max(a, g);
                if (g >= beta) {
                    break;
                }
            }
        }
        else {
            g = Integer.MAX_VALUE;
            int b = beta;
            for (KalahaNode child: node.getChildrenSorted()){
                g = Math.min(g, alphabeta(child, depth - 1, alpha, b));
                alpha = Math.min(b, g);
                if (g <= alpha) {
                    break;
                }
            }
        }

        if(te == null){
            te = new TableEntry();
            synchronized (transpositionalTable){
                transpositionalTable.put(node, te);
            }
        }

        if(g <= alpha){
            te.upperBound = g;
        }
        else if(g > alpha && g < beta){
            te.lowerBound = g;
            te.upperBound = g;
        } else if (g >= beta) {
            te.lowerBound = g;
        }

        return g;
    }
}
