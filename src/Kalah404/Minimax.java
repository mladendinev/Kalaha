package Kalah404;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

    public static final Map<Node, TableEntry> transpositionalTable = new HashMap<Node, TableEntry>();

    public static int alphabeta(Node node, int depth, int alpha, int beta) {
        //System.err.println("Performing at depth" + depth);
        TableEntry te;
        synchronized (transpositionalTable){
            te = transpositionalTable.get(node);
        }

        if(te != null){
            System.err.println("Entry exits" + te);

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

        if(Kalah.gameOver(node.getBoard())){
            //System.err.println("Game over - leaf node");
            return node.getEvaluationFunction() * 10;
        }

        if (depth == 0){
            //return (int)Heuristics.getScore(node);
           return node.getEvaluationFunction();
        }



        int g;

        if (Side.myTurn(node.getSide())) {
            g = Integer.MIN_VALUE;
            int a = alpha;
           // System.err.println("Maxnode at depth" + depth);
            for (Node child: node.getChildrenSorted()){
                g = Math.max(g, alphabeta(child, depth -1, a, beta));
                a = Math.max(a, g);
                if (g >= beta) {
                    break;
                }
            }
        }
        else {
          //  System.err.println("Minnode at depth" + depth);
            g = Integer.MAX_VALUE;
            int b = beta;
            List<Node> children = node.getChildrenSorted();
            Collections.sort(children, Collections.reverseOrder());
            for (Node child: children){
                g = Math.min(g, alphabeta(child, depth - 1, alpha, b));
                b = Math.min(b, g);
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
