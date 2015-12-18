package Kalah404;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mbax2md2 on 03/12/15.
 */
public class Minimax {

    private enum Flag { EXACT, LOWER, UPPER }

    private static class TranspositionalTableEntry {
        int depth;
        Flag flag;
        int score;

        public TranspositionalTableEntry(int depth, int score){
            this.depth = depth;
            this.score = score;
        }
    }

    /* At the end of the game, the trasp table seems to have about 20,000 entries
       We initialize it to 60,000 to reduce the number of hash conflicts */
    public static final Map<Board, TranspositionalTableEntry> transpositionalTable
                            = new ConcurrentHashMap<Board, TranspositionalTableEntry>(60000);

    public static int alphabeta(Node node, int depth, int alpha, int beta) {

        Board board = node.getBoard();

        /*  Check if we have already visited the node
            (either in a previous move or at a previous step of
            Iterative Deepening with MTDf)  */
        TranspositionalTableEntry e = transpositionalTable.get(board);

        if(e != null){
            if (e.depth >= depth) {
                if (e.flag == Flag.LOWER) {
                    if (e.score >= beta) {
                        return e.score;
                    }
                } else if (e.flag == Flag.UPPER) {
                    if (e.score <= alpha) {
                        return e.score;
                    }
                }

                alpha = Math.max(alpha, e.score);
                beta = Math.min(beta, e.score);
            }
        }

        if (Kalah.gameOver(board)) {
            //This is a leaf node
            return node.getEvaluationFunction();
        }

        if (depth == 0) {
            //We have reached our depth limit
            return node.getHeuristicScore();
        }

        int g;

        if (Side.myTurn(node.getSide())) {
            g = Integer.MIN_VALUE;
            int a = alpha;
            /* Take into account first the children we are most likely to take.
               ie. sort children based on their heuristics values, reversed. */
            for (Node child : node.getChildrenSorted()) {
                g = Math.max(g, alphabeta(child, depth - 1, a, beta));
                a = Math.max(a, g);
                if (a >= beta) {
                    break;
                }
            }
        } else {
            g = Integer.MAX_VALUE;
            int b = beta;
            /* Take into account first the children our opponent is most likely to take.
               ie. sort children based on their heuristics values, reversed. */
            List<Node> children = node.getChildrenSorted();
            Collections.sort(children, Collections.reverseOrder());
            for (Node child : children) {
                g = Math.min(g, alphabeta(child, depth - 1, alpha, b));
                b = Math.min(b, g);
                if (b <= alpha) {
                    break;
                }
            }
        }

        if (e == null) {
            //Store new knowledge in the transp table
            e = new TranspositionalTableEntry(depth, g);

            if (g <= alpha) {
                e.flag = Flag.UPPER;
            } else if (g > alpha && g < beta) {
                e.flag = Flag.EXACT;
            } else if (g >= beta) {
                e.flag = Flag.LOWER;
            }

            transpositionalTable.put(board, e);
        }

        return g;
    }
}
