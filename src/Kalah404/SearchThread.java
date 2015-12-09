package Kalah404;

/**
 * Created by gmtuca on 04/12/15.
 */
public class SearchThread extends Thread {

    private final Node node;

    public SearchThread(Node node){
        this.node = node;
    }

    @Override
    public void run() {
        //int score = 0;
        int score = MemoryTestDriver.IterativeDeepening(node, 6);

        //int score = Minimax.alphabeta(node, 10, Integer.MIN_VALUE, Integer.MAX_VALUE);
        //int score =;
        //score += node.getScore();

        //another core for this?
        //score += Heuristics.monteCarlo(node, 100);

        synchronized (node){
            //double monteCarloScore = node.getScore(); //previous montecarlo score
            //System.err.println("PREV MONTECARLO SCORE: " + monteCarloScore);
            node.setScore(score);
        }

        //System.err.println("THREAD RETURNING > " + node.getScore());
    }
}
