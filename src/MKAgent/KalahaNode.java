package MKAgent;

import java.util.*;


public class  KalahaNode {

    private final Board board;
    private final Side side;
    private Map<Integer, KalahaNode> children;

    //private int payoff;

    public int getBestMove(){
        int bestIndex = -1;
        int bestValue = Integer.MIN_VALUE;

        for(Map.Entry<Integer, KalahaNode> e : children.entrySet()){
            KalahaNode child = e.getValue();

            int childValue = Minimax.alphabeta(child, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            if(childValue > bestValue){
                bestValue = childValue;
                bestIndex = e.getKey();
            }
        }

        return bestIndex;
    }

    public KalahaNode(Board board, Side side) {
        this.board      = board;
        this.side       = side;
        this.children   = null;
        //this.payoff = 0;
    }

    public Side getSide() {
        return side;
    }

    public int getEvaluationFunction() {
        return board.getSeedsInStore(Side.mySide) - board.getSeedsInStore(Side.mySide.opposite());
    }

    public Board getBoard(){
        return board;
    }

    public Collection<KalahaNode> getChildren(){
        if(children == null){
            return null;
        }
        else{
            return children.values();
        }
    }

    public void addNewLayer(){
        if(children == null) {
            createChildren();
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

        createChildren();

        //Recursively add children to the children
        for(KalahaNode child : children.values()){
            child.createChildren(depth-1);
        }
    }

    public void createChildren() {
        List<Integer> validHoles = board.getValidHoles(side);

        this.children = new HashMap<Integer, KalahaNode>(validHoles.size());
        for (int i : validHoles) {
            addChild(i);
        }
    }

    private void addChild(int i) {
        Board childBoard = new Board(board);
        Move move = new Move(side, i);
        Side childSide = Kalah.makeMove(childBoard,move);

        children.put(i,new KalahaNode(childBoard, childSide));
    }

    public KalahaNode getChild(int i){
        return children.get(i);
    }

}
