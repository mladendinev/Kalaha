package MKAgent;

import java.util.HashMap;
import java.util.Map;


public class  KalahaNode {

    private Map<Integer, KalahaNode> children;

    private final Board board;

    private Side side;

    //private int payoff;

    public int getBestMove(){
        int currentBest = -1;
        int bestValue = Integer.MIN_VALUE;

        for(Map.Entry<Integer, KalahaNode> e : children.entrySet()){
            KalahaNode child = e.getValue();

            int childValue = Minimax.alphabeta(child, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            if(childValue > bestValue){
                bestValue = childValue;
                currentBest = e.getKey();
            }
        }

        return currentBest;
    }

    public KalahaNode(Board board, Side side) {
        this.board = board;
        this.children = new HashMap<Integer, KalahaNode>();
        this.side = side;
        //this.payoff = 0;
    }


    public Side getSide() {
        return side;
    }

    public int getEvaluationFunction() {
        return board.getSeedsInStore(Side.mySide) - board.getSeedsInStore(Side.mySide.opposite());
    }

    public Map<Integer, KalahaNode> getChildren() {
        return children;
    }

    public void addChildren() {
        for (int i : board.getValidHoles(this.getSide())) {
                addChild(i);
        }
    }

    public Board getBoard(){
        return board;
    }

    public void addChild(int i) {

        //TODO Check side of the player and then do the move
        Board childBoard = new Board(board);
        Move move = new Move(side, i);
        Side nodeSide = Kalah.makeMove(childBoard,move);
        KalahaNode child = new KalahaNode(childBoard, nodeSide);
        //System.err.println("------------------" + "\n" + currentBoard.toString());

        children.put(i,child);
    }

    public KalahaNode getChild(int i) {
        return children.get(i);
    }

    public void addNewLayer(){
        if(children.isEmpty()) {
            addChildren();
        }
        else{
            for(KalahaNode child : children.values()) {
                child.addNewLayer();
            }
        }
    }

    public void createChildren(int depth) {
        if(depth == 0){
            return;
        }

        addChildren();

        for(KalahaNode child : children.values()){
            child.createChildren(depth-1);
        }
    }

}
