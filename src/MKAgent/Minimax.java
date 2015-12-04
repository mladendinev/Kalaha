package MKAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by mbax2md2 on 03/12/15.
 */
public class Minimax {

    static class TableEntry {
        public int lowerBound = Integer.MIN_VALUE;
        public int upperBound = Integer.MAX_VALUE;

        public TableEntry(){}
    }

    public static Map<KalahaNode, TableEntry> transpositionalTable = new HashMap<KalahaNode, TableEntry>();

    public static int alphabeta(KalahaNode node, int depth, int alpha, int beta) {

        TableEntry te = transpositionalTable.get(node);

        if(te != null){
            //System.err.println("Table hit!");
            if(te.lowerBound >= beta){
                return te.lowerBound;
            }
            if(te.upperBound <= alpha){
                return te.upperBound;
            }
            alpha = Math.max(alpha, te.lowerBound);
            beta = Math.min(beta, te.upperBound);
        }/*
        else{
            System.err.println("Table miss");
        }*/

        if (depth == 0){ //|| node.getChildren()
            return Heuristics.monteCarlo(node, 20);
        }

        int value;

        if (Side.myTurn(node.getSide())) {
            value = Integer.MIN_VALUE;
            for (KalahaNode child: node.generateChildren().values()){
                value = Math.max(value, alphabeta(child,depth -1, alpha, beta));
                alpha = Math.max(alpha, value);
                if (beta <= alpha) {
                    break;
                }
            }
        }
        else {
            value = Integer.MAX_VALUE;
            for (KalahaNode child: node.generateChildren().values()){
                value = Math.min(value, alphabeta(child, depth - 1, alpha, beta));
                alpha = Math.min(beta, value);
                if (beta <= alpha) {
                    break;
                }
            }
        }

        if(te == null){
            te = new TableEntry();
            transpositionalTable.put(node, te);
        }

        if(value <= alpha){
            te.upperBound = value;
        }
        else if(value > alpha && value < beta){
            te.lowerBound = value;
            te.upperBound = value;
        } else if (value >= beta) {
            te.lowerBound = value;
        }

        return value;
    }
}
