package MKAgent;

import java.util.ArrayList;
import java.util.List;


public class  KalahaNode {

    private List<KalahaNode> children;

    //TODO do we need to store the whole board??
    private final Kalah currentKalah;

    // Create a new class for the board
    private int evaluationFunction;

    // Contains the player and the move made
    private Move moveChosen;

    private final boolean isLeafNode;

    private KalahaNode parent;

    private boolean myTurn;

    public KalahaNode(Kalah currentKalah, boolean myTurn) {
        this.currentKalah = currentKalah;
        evaluationFunction = 0;
        isLeafNode = false;
        this.children = new ArrayList<>();
        this.myTurn = myTurn;
    }

    public KalahaNode(Kalah currentKalah,boolean isLeafNode,boolean myTurn) {
        this.currentKalah = currentKalah;
        this.isLeafNode = isLeafNode;
        evaluationFunction = 0;
        this.myTurn = myTurn;
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

    public boolean isLeafNode() {
        return isLeafNode;
    }

    public List<KalahaNode> getChildren() {
        return children;
    }

    public void addChildren() {

        for(int i = 0; i < 7; i++){
            addChild(i);
        }
    }

    public void addChild(int i) {
        Board currentBoard = currentKalah.getBoard();
        Kalah childKalah = new Kalah(new Board(currentBoard));
        KalahaNode child = new KalahaNode(childKalah, side.opposite());
        Move move = new Move(side, i + 1);
        childKalah.makeMove(move);
        System.err.println("------------------" + "\n" + currentBoard.toString());

        // Save the move we have made
        child.setMoveChosen(move);
        children.add(child);
    }

    public KalahaNode getChild(int i) {
        return children.get(i);
    }

    public void saveOpponentsMove(Move moveMade)  {
        currentKalah.makeMove(moveMade);
        Board currentBoard = currentKalah.getBoard();
        System.err.println("------------------" + "\n" + currentBoard.toString());
        Kalah childKalah = new Kalah(new Board(currentBoard));
        KalahaNode child = new KalahaNode(childKalah, moveMade.getSide());
        // Save the move we have made
        children.add(child);
    }

}
