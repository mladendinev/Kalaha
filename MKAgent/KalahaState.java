package MKAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbax2vh2 on 01/12/15.
 */
public class KalahaState {

    private List<KalahaState> children;

    //TODO do we need to store the whole board??
    private final Kalah currentKalah;

    // Create a new class for the board
    private int evaluationFunction;

    // Contains the player and the move made
    private Move moveChosen;

    private int payoff;

    private final boolean isLeafNode;

    private Side side;

    public KalahaState(Kalah currentKalah, Side side) {
        this.currentKalah = currentKalah;
        evaluationFunction = 0;
        isLeafNode = false;
        this.side = side;

        this.children = new ArrayList<>(7);

    }

    public KalahaState(Kalah currentKalah, Side side, boolean isLeafNode) {
        this.currentKalah = currentKalah;
        this.isLeafNode = isLeafNode;
        evaluationFunction = 0;
        this.side = side;
    }

    public Move getMoveChosen() {
        return moveChosen;
    }

    public void setMoveChosen(Move moveChosen) {
        this.moveChosen = moveChosen;
    }

    public int getEvaluationFunction() {
        return evaluationFunction;
    }

    public void setEvaluationFunction(int evaluationFunction) {
        this.evaluationFunction = evaluationFunction;
    }

    public Kalah getCurrentKalah() {
        return currentKalah;
    }

    public int getPayoff() {
        return payoff;
    }

    public void setPayoff(int payoff) {
        this.payoff = payoff;
    }

    public boolean isLeafNode() {
        return isLeafNode;
    }

    public List<KalahaState> getChildren() {
        return children;
    }

    public void addChildren() {

        for(int i = 0; i < 7; i++){
            Board currentBoard = currentKalah.getBoard();
            Kalah childKalah = new Kalah(new Board(currentBoard));
            KalahaState child = new KalahaState(childKalah, side.opposite());
            Move move = new Move(side, i + 1);
            childKalah.makeMove(move);
            children.add(child);
        }
    }

}
