import junit.framework.TestCase;
import Kalah404.Board;
import Kalah404.Side;
import Kalah404.Heuristics;
/**
 * Created by mbax2vh2 on 07/12/15.
 */
public class HeuristicsTest extends TestCase {

    public void testGetScore() throws Exception {

    }

    public void testCanGetExtraTurn() throws Exception {

        Board board = new Board(7,7);
        Side side = Side.SOUTH;
        int result = Heuristics.canGetExtraTurn(board, side);
        assertEquals(1, result);
    }

    public void testHoleCapture() throws Exception {
        Board board = new Board(7,7);
        Side side = Side.SOUTH;
        board.setSeeds(side, 1, 1);
        board.setSeeds(side, 2, 0);
        int result = Heuristics.holeCapture(board, side);
        assertEquals(8, result);
    }
}