package MKAgent;

import java.util.*;


public class  KalahaNode {

    private final Board board;

    public void setSide(Side side) {
        this.side = side;
    }

    private Side side;
    //private Map<Integer, KalahaNode> children;

    //public int upperBound = Integer.MAX_VALUE;
    //public int lowerBound = Integer.MIN_VALUE;

    //private int payoff;

    public int getBestMove(){
        int bestIndex = -1;
        int bestValue = Integer.MIN_VALUE;

        System.err.println("Getting best move from: ");

        for(Map.Entry<Integer, KalahaNode> e : generateChildren().entrySet()){
            KalahaNode child = e.getValue();

            //int childValue = Minimax.alphabeta(child, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            int childValue = MemoryTestDriver.IterativeDeepening(child, 3);

            System.err.println("-> " + childValue);

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
       // this.children   = null;
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
/*
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
    */

    public Map<Integer,KalahaNode> generateChildren(){
        List<Integer> validHoles = board.getValidHoles(side);

        Map<Integer,KalahaNode> moves = new HashMap<Integer,KalahaNode>(validHoles.size());

        for(int i : validHoles){
            moves.put(i, generateChild(i));
        }

        return moves;
    }

    private KalahaNode generateChild(int i){
        Board childBoard = new Board(board);
        Move move = new Move(side, i);
        Side childSide = Kalah.makeMove(childBoard,move);

        return new KalahaNode(childBoard, childSide);
    }
/*
    public void createChildren() {
        List<Integer> validHoles = board.getValidHoles(side);

        this.children = new HashMap<Integer, KalahaNode>(validHoles.size());
        for (int i : validHoles) {
            this.children.put(i,generateChild(i));
        }
    }

    public KalahaNode getChild(int i){
        return children.get(i);
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KalahaNode that = (KalahaNode) o;

        if (!board.equals(that.board)) return false;
        return side == that.side;
    }

    @Override
    public int hashCode() {
        int result = board.hashCode();
        result = 31 * result + side.hashCode();
        return result;
    }
}
