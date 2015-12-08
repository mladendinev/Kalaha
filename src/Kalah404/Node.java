package Kalah404;

import java.util.*;


public class Node implements Comparable<Node> {

    private final Board board;
    private Side side;
    public Map<Integer, Node> children;
    private int evaluationFunction = 0;

    private int score = 0;

    public int getBestMove(){

        System.err.println("Getting best move from: ");

        List<Node> childrenSorted = getChildrenSorted();
        List<Thread> searchThreads = new ArrayList<Thread>(childrenSorted.size());

        for(Node child : childrenSorted){
            searchThreads.add(new SearchThread(child));
        }

        for(Thread t : searchThreads){
            t.start();
        }

        for(Thread t : searchThreads){
            try {
                t.join();
            }
            catch(InterruptedException e){
                System.err.println(e.getMessage());
                break;
            }
        }

        int bestIndex = -1;
        int bestValue = Integer.MIN_VALUE;

        for(Node child : childrenSorted){

            int score;
            synchronized (child){
                score = child.getScore();
            }

            System.err.println("-> " + score);

            if(score > bestValue){
                bestValue = score;
                bestIndex = getChildIndex(child);
            }
        }

        System.err.println("best value is MOVE;" + bestIndex + " with value: " + bestValue);


        return bestIndex;
    }

    private int getChildIndex(Node node){
        for(Map.Entry<Integer, Node> e : children.entrySet()){
            if(e.getValue().equals(node)){
                return e.getKey();
            }
        }

        return -1;
    }

    public Node(Board board, Side side) {
        this.board      = board;
        this.side       = side;
        this.children   = null;
        this.evaluationFunction = board.getSeedsInStore(Side.mySide) - board.getSeedsInStore(Side.mySide.opposite());
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
        return new ArrayList<Node>(getChildren().values());
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setSide(Side side) {
        this.side = side;
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
        //todo instance variable for the score instead
        return Heuristics.getScore(this) > Heuristics.getScore(node) ? -1 : 1;
    }

    @Override
    public String toString() {
        return "{" +
                "side=" + side +
                ", board=\n" + board +
                '}';
    }
}
