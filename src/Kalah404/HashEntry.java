package Kalah404;

/**
 * Created by mbax2md2 on 09/12/15.
 */
public class HashEntry {

    public enum Flag { EXACT, LOWER, UPPER };

    public int depth;
    public Flag flag;
    public int score;

//    public HashEntry(int zobrist, int depth, boolean actualValue, int score,
//                     int ancient){
//        this.zobrist = zobrist;
//        this.depth = depth;
//        this.actualValue = actualValue;
//        this.score = score;
//        this.ancient = ancient;
//    }

    public HashEntry(int depth, int score){
        this.depth = depth;
        this.score = score;
    }

}
