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

    private Side side;

    public KalahaNode(Kalah currentKalah, Side side) {
        this(currentKalah, side, false);
    }

    public KalahaNode(Kalah currentKalah, Side side, boolean isLeafNode) {
        this.currentKalah = currentKalah;
        this.isLeafNode = isLeafNode;

        Board b = currentKalah.getBoard();

        evaluationFunction = b.getSeedsInStore(Side.mySide) - b.getSeedsInStore(Side.mySide.opposite());
        this.children = new ArrayList<>();
        this.side = side;
    }

    public Move getMoveChosen() {
        return moveChosen;
    }

    public void setMoveChosen(Move moveChosen) {
        this.moveChosen = moveChosen;
    }

    public Side getSide() {
        return side;
    }

    public int getEvaluationFunction() {
        return evaluationFunction;
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

    public List<KalahaNode> addChildren() {
        Board b = currentKalah.getBoard();

        for(int i = 0; i < 7; i++){
            if(b.getSeeds(side, i+1) > 0) {
                addChild(i);
            }
        }

        return children;
    }

    public void addChild(int i) {

        //TODO Check side of the player and then do the move
        Board currentBoard = currentKalah.getBoard();
        Kalah childKalah = new Kalah(new Board(currentBoard));
        KalahaNode child = new KalahaNode(childKalah, side.opposite());
        Move move = new Move(side, i + 1);
        childKalah.makeMove(move);
        //System.err.println("------------------" + "\n" + currentBoard.toString());

        // Save the move we have made
        child.setMoveChosen(move);
        children.add(child);
    }

    public KalahaNode getChild(int i) {
        return children.get(i);
    }

    private static String repeat(String s, int times){
        StringBuilder sb = new StringBuilder();

        for(int i=0; i < times; i++){
            sb.append(s);
        }

        return sb.toString();
    }

    public String toString(int depth){
        StringBuilder sb = new StringBuilder();

        sb.append(repeat("\t\t", depth))
          .append(side + "   ")
          .append(currentKalah.getBoard().toString());

        for(KalahaNode n : children){
            sb.append(n.toString(depth + 1));
        }

        return sb.toString();

    }

    public static void createChildren(KalahaNode node, int depth) {
        if(depth == 0){
            return;
        }

        List<KalahaNode> newChildren = node.addChildren();

        for(KalahaNode child : newChildren){
            createChildren(child, depth-1);
        }
    }

}
