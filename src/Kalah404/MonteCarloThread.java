package Kalah404;

/**
 * Created by gmtuca on 04/12/15.
 */
public class MonteCarloThread extends Thread {

    private final Node node;
    private boolean running = false;

    public MonteCarloThread(Node node){
        this.node = node;
    }

    @Override
    public void run() {
        this.running = true;

        double runs = 0;
        double average = 0;//todo double

        while(running) {
            int value = Heuristics.monteCarlo(node);

            average = (value + (average * runs)) / (runs + 1);
            runs++;

            synchronized (node) {
                node.setScore((int)average);
            }
        }

        System.err.println("Montecarlo returning " + average + " after playing " + runs + " games!");
    }

    public void requestStop(){
        this.running = false;
    }
}
