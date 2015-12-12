package Kalah404;

/**
 * Created by mbax2md2 on 09/12/15.
 */
public class HashEntry {

    public long zobrist;
    public int depth;
    public int flag;
    public int eval;
    public int ancient;
    public Move move;

    public HashEntry(long zobrist, int depth, int flag, int eval,
                     int ancient, Move move){
        this.zobrist = zobrist;
        this.depth = depth;
        this.flag = flag;
        this.eval = eval;
        this.ancient = ancient;
        this.move = move;
    }
}
