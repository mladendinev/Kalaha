package MKAgent;

/**
 * Created by gmtuca on 04/12/15.
 */
public class MemoryTestDriver {

    public static int IterativeDeepening(KalahaNode root, int d){
        int firstGuess = 0;

        for(int i = 0; i < d; i++){
            firstGuess = MTDf(root, firstGuess, d);
        }

        return firstGuess;
    }

    public static int MTDf(KalahaNode root, int f, int d){
        int g = f;
        int upperBound = Integer.MAX_VALUE;
        int lowerBound = Integer.MIN_VALUE;

        while(lowerBound < upperBound){
            int beta;

            if(g == lowerBound){
                beta = g+1;
            }
            else{
                beta = g;
            }

            g = Minimax.alphabeta(root,  d, beta -1, beta);

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
