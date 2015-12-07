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
        int score = MemoryTestDriver.IterativeDeepening(node, 30);
        //int score =;
        //score += node.getScore();

        //another core for this?
        //score += Heuristics.monteCarlo(node, 2000);

        synchronized (node){
            double monteCarloScore = node.getScore(); //previous montecarlo score
            System.err.println("PREV MONTECARLO SCORE: " + monteCarloScore);
            node.setScore(score + (int)(monteCarloScore/2.0));
        }

        //System.err.println("THREAD RETURNING > " + node.getScore());
    }
}
