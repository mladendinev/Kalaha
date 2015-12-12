package Kalah404;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mbax2md2 on 03/12/15.
 */
public class Minimax {

    public static final Map<Board, HashEntry> transpositionalTable = new HashMap<Board, HashEntry>();


    public static int alphabeta(Node node, int depth, int alpha, int beta) {

        HashEntry e;
        synchronized (transpositionalTable) {
            e = transpositionalTable.get(node.getBoard());
        }

        if(e != null){

            if (e.depth >= depth) {
                if (e.flag == HashEntry.Flag.LOWER) {
                    if (e.score >= beta) {
                        return e.score;
                    }
                } else if (e.flag == HashEntry.Flag.UPPER) {
                    if (e.score <= alpha) {
                        return e.score;
                    }
                }

                alpha = Math.max(alpha, e.score);
                beta = Math.min(beta, e.score);
            }
        }

        if (Kalah.gameOver(node.getBoard())) {
            //System.err.println("Game over - leaf node");
            return node.getEvaluationFunction();
        }

        if (depth == 0) {
            return Heuristics.getScore(node);
            //return node.getEvaluationFunction();
        }

        int g;

        if (Side.myTurn(node.getSide())) {
            g = Integer.MIN_VALUE;
            int a = alpha;
            // System.err.println("Maxnode at depth" + depth);
            for (Node child : node.getChildrenSorted()) {
                g = Math.max(g, alphabeta(child, depth - 1, a, beta));
                a = Math.max(a, g);
                if (g >= beta) {
                    break;
                }
            }
        } else {
            //  System.err.println("Minnode at depth" + depth);
            g = Integer.MAX_VALUE;
            int b = beta;
            List<Node> children = node.getChildrenSorted();
            Collections.sort(children, Collections.reverseOrder());
            for (Node child : children) {
                g = Math.min(g, alphabeta(child, depth - 1, alpha, b));
                b = Math.min(b, g);
                if (g <= alpha) {
                    break;
                }
            }
        }

        if (e == null) {
            e = new HashEntry(depth, g);

            synchronized (transpositionalTable) {
                transpositionalTable.put(node.getBoard(), e);
            }

            if (g <= alpha) {
                e.flag = HashEntry.Flag.UPPER;
            } else if (g > alpha && g < beta) {
                e.flag = HashEntry.Flag.EXACT;
            } else if (g >= beta) {
                e.flag = HashEntry.Flag.LOWER;
            }
        }


        return g;
    }
}
