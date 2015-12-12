package Kalah404;

/**
 * Created by gmtuca on 04/12/15.
 */
public class SearchThread extends Thread {

    private final Node node;
    private final int depth;

    public SearchThread(Node node, int depth){
        this.node = node;
        this.depth = depth;
    }

    @Override
    public void run() {
        node.setScore(MemoryTestDriver.IterativeDeepening(node, depth));
    }
}
