package MKAgent;

/**
 * Created by gmtuca on 04/12/15.
 */
public class SearchThread extends Thread {

    private final KalahaNode node;

    public SearchThread(KalahaNode node){
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

        node.setScore(score);
        //System.err.println("THREAD RETURNING > " + node.getScore());
    }
}
