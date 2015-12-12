package Kalah404;

/**
 * Created by mbax2md2 on 09/12/15.
 */
public class HashEntry {

    public enum Flag { EXACT, LOWER, UPPER };


    public int zobrist;
    public int depth;
    public Flag flag;
    public boolean actualValue;
    public int score;
    public int ancient;

//    public HashEntry(int zobrist, int depth, boolean actualValue, int score,
//                     int ancient){
//        this.zobrist = zobrist;
//        this.depth = depth;
//        this.actualValue = actualValue;
//        this.score = score;
//        this.ancient = ancient;
//    }

    public HashEntry(int zobrist, int depth, int score,int ancient){
        this.zobrist = zobrist;
        this.depth = depth;
        this.score = score;
        this.ancient = ancient;
    }

}
