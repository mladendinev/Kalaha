package Kalah404;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mbax2md2 on 03/12/15.
 */
public class Minimax {

    public static final Map<Board, HashEntry> transpositionalTable = new ConcurrentHashMap<>(60000);

    public static int alphabeta(Node node, int depth, int alpha, int beta) {

        Board board = node.getBoard();

        HashEntry e = transpositionalTable.get(board);

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

        if (Kalah.gameOver(board)) {
            return node.getEvaluationFunction();
        }

        if (depth == 0) {
            return node.getHeuristicScore();
        }

        int g;

        if (Side.myTurn(node.getSide())) {
            g = Integer.MIN_VALUE;
            int a = alpha;
            for (Node child : node.getChildrenSorted()) {
                g = Math.max(g, alphabeta(child, depth - 1, a, beta));
                a = Math.max(a, g);
                if (g >= beta) {
                    break;
                }
            }
        } else {
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

            if (g <= alpha) {
                e.flag = HashEntry.Flag.UPPER;
            } else if (g > alpha && g < beta) {
                e.flag = HashEntry.Flag.EXACT;
            } else if (g >= beta) {
                e.flag = HashEntry.Flag.LOWER;
            }

            transpositionalTable.put(board, e);
        }

        return g;
    }
}
