package MKAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class  KalahaNode {

    private Map<Integer, KalahaNode> children;

    //TODO do we need to store the whole board??
    private final Kalah currentKalah;

    // Create a new class for the board
    private int evaluationFunction;

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
        this.children = new HashMap<Integer, KalahaNode>();
        this.side = side;
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

    public Map<Integer, KalahaNode> getChildren() {
        return children;
    }

    public void addChildren() {
        Board b = currentKalah.getBoard();
        for (int i : b.getValidHoles(this.getSide())) {
                addChild(i);
        }
    }

    public Board getBoard(){
        return currentKalah.getBoard();
    }

    public void addChild(int i) {

        //TODO Check side of the player and then do the move
        Board currentBoard = currentKalah.getBoard();
        Kalah childKalah = new Kalah(new Board(currentBoard));
        Move move = new Move(side, i);
        Side nodeSide = childKalah.makeMove(move);
        KalahaNode child = new KalahaNode(childKalah, nodeSide);
        //System.err.println("------------------" + "\n" + currentBoard.toString());

        children.put(i,child);
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

        for(Map.Entry<Integer, KalahaNode> e : children.entrySet()){
            sb.append("KEY: " + e.getKey())
              .append(e.getValue().toString(depth + 1));
        }

        return sb.toString();

    }

    public static void createChildren(KalahaNode node, int depth) {
        if(depth == 0){
            return;
        }

        node.addChildren();

        for(KalahaNode child : node.getChildren().values()){
            createChildren(child, depth-1);
        }
    }

}
