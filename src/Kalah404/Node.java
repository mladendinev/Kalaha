package Kalah404;

import java.util.*;


public class Node implements Comparable<Node> {

    private final Board board;
    private Side side;

    public Map<Integer, Node> children;

    private int evaluationFunction = 0;
    private int score = Integer.MIN_VALUE;
    private int heuristicScore;

    // Depth that Iterative Deepening will look down on each child
    // We keep it 7 to allow it to be very quick! (can be increased for more accuracy)
    private static final int MAXIMUM_DEPTH = 7;

    public int getBestMove(){

        //Sort children
        List<Node> childrenSorted = getChildrenSorted();

        /* Create list of threads which will look down all children of this node.
           Each child will return a value representing the score of that child
           according to their Iterative Deepening value */

        List<Thread> scoreThreads = new ArrayList<Thread>(childrenSorted.size());

        for(Node child : childrenSorted){
            scoreThreads.add(new ScoreTrackingThread(child, MAXIMUM_DEPTH));
        }

        for(Thread t : scoreThreads){
            t.start();
        }

        for(Thread t : scoreThreads){
            try {
                t.join();
            }
            catch(InterruptedException e){
                System.err.println(e.getMessage());
            }
        }

        int bestIndex = -1;
        int bestValue = Integer.MIN_VALUE;

        for(Node child : childrenSorted){

            int score = child.getScore();

            if(score > bestValue){
                bestValue = score;
                bestIndex = getChildIndex(child);
            }
        }

        return bestIndex;
    }

    public Node(Board board, Side side) {
        this.board              = board;
        this.side               = side;
        this.children           = null;
        this.evaluationFunction = board.getSeedsInStore(Side.mySide) - board.getSeedsInStore(Side.mySide.opposite());
        this.heuristicScore     = Heuristics.getScore(this);
    }

    public Map<Integer, Node> getChildren() {
        if(children == null){
            children = generateChildren();
        }
        return children;
    }

    public Node getChild(int i){
        return children.get(i);
    }

    public void addNewLayer(){
        if(children == null) {
            this.children = getChildren();
        }
        else{
            for(Node child : children.values()) {
                child.addNewLayer();
            }
        }
    }

    public List<Node> getChildrenList(){
        List<Node> childrenList = new ArrayList<Node>(getChildren().values());
        Collections.reverse(childrenList);
        /* this is a fix for a problem we had with the HashMap values() call
           which returned differently ordered values for Java6 vs Java7 */
        return childrenList;
    }

    public List<Node> getChildrenSorted(){
        List<Node> children = getChildrenList();
        Collections.sort(children);
        return children;
    }

    private Map<Integer, Node> generateChildren(){
        List<Integer> validHoles = board.getValidHoles(side);

        Map<Integer, Node> moves = new HashMap<Integer, Node>(validHoles.size());

        for(int i : validHoles){
            moves.put(i, generateChild(i));
        }

        return moves;
    }

    private Node generateChild(int i){
        Board childBoard = new Board(board);
        Move move = new Move(side, i);
        Side childSide = Kalah.makeMove(childBoard,move);

        return new Node(childBoard, childSide);
    }

    private int getChildIndex(Node node){
        for(Map.Entry<Integer, Node> e : children.entrySet()){
            if(e.getValue().equals(node)){
                return e.getKey();
            }
        }

        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node that = (Node) o;

        if (!board.equals(that.board)) return false;
        return side == that.side;
    }

    @Override
    public int hashCode() {
        int result = board.hashCode();
        result = 31 * result + side.hashCode();
        return result;
    }

    @Override
    public int compareTo(Node node) {
        return this.heuristicScore > node.getHeuristicScore() ? -1 : 1;
    }

    @Override
    public String toString() {
        return "{" +
                "side=" + side +
                ", board=\n" + board +
                '}';
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Side getSide() {
        return side;
    }

    public Board getBoard(){
        return board;
    }

    public int getEvaluationFunction() {
        return evaluationFunction;
    }

    public int getHeuristicScore() {
        return heuristicScore;
    }
}
