package Kalah404;

/**
 * Created by gmtuca on 04/12/15.
 */
public class MemoryTestDriver {

    public static int IterativeDeepening(Node node, int d){
        int firstGuess = 0;

        /* Iterative Deepening in steps of 2 has shown to be
           very fast and still accurate. */
        for(int i = 1; i < d; i+=2) {
            firstGuess = MTDf(node, firstGuess, i);
        }

        return firstGuess;
    }

    public static int MTDf(Node root, int f, int d){
        int g = f;
        int upperBound = Integer.MAX_VALUE;
        int lowerBound = Integer.MIN_VALUE;

        int beta;
        while(lowerBound < upperBound){

            if(g == lowerBound){
                beta = g+1;
            }
            else{
                beta = g;
            }

            g = Minimax.alphabeta(root,  d, beta-1, beta);

            if(g < beta){
                upperBound = g;
            }
            else{
                lowerBound = g;
            }
        }

        return g;
    }
}
