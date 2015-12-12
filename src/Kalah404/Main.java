package Kalah404;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * The main application class. It also provides methods for communication
 * with the game engine.
 */
public class Main
{
	/**
	 * Input from the game engine.
	 */
	private static Reader input = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * Sends a message to the game engine.
	 * @param msg The message.
	 */
	public static void sendMsg (String msg)
	{
		System.out.println(msg);
	}

	/**
	 * Receives a message from the game engine. Messages are terminated by
	 * a '\n' character.
	 * @return The message.
	 * @throws IOException if there has been an I/O error.
	 */
	public static String recvMsg() throws IOException
	{
		StringBuilder message = new StringBuilder();
		int newCharacter;

		do
		{
			newCharacter = input.read();
			if (newCharacter == -1)
				throw new EOFException("Input ended unexpectedly.");
			message.append((char)newCharacter);
		} while((char)newCharacter != '\n');

		//System.err.println("RECEIVED AT: " + System.currentTimeMillis());
		return message.toString();
	}

	//TODO remove this from production code
	private static void redirectSystemErr(boolean first){
		try{
			String filePath = System.getProperty("user.dir") + "/KalahaLog_" + first + ".log";
			OutputStream output = new FileOutputStream(filePath);

			PrintStream printOut = new PrintStream(output);
			System.setErr(printOut);
		}
		catch(Exception e){
			System.err.println("Exception " + e.getMessage());
		}
	}

	/**
	 * The main method, invoked when the program is started.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args)
	{
		try {
			Board board = new Board(7,7);

			// Create the root of the tree
			Node root = new Node(new Board(board), Side.SOUTH); // South always starts
			root.addNewLayer();

			boolean canSwap = true;

			String s;
			while (true)
			{
				s = recvMsg();
				//System.err.println("Received: " + s);

				try {
					MsgType mt = Protocol.getMessageType(s);
					switch (mt)
					{
						case START: //System.err.println("A start.");
							boolean first = Protocol.interpretStartMsg(s);
							//System.err.println("Starting player? " + first);

							if (first) {
								Side.mySide = Side.SOUTH;
							}
							else {
								Side.mySide = Side.NORTH;
							}

							redirectSystemErr(first);

							// If it is our turn make a move
							if(first){
								canSwap = false;
								sendMsg(Protocol.move(2));
							}
							break;
						case STATE: //System.err.println("A state.");

							Protocol.MoveTurn r = Protocol.interpretStateMsg (s, board);
							//System.err.println("This was the move: " + r.move);
							//System.err.println("Is the game over? " + r.end);
							//if (!r.end) System.err.println("Is it our turn again? " + r.again);
							//System.err.print("The board as we got it:\n" + board);

							// If we were the first player and the move was swap we need to change sides
							// and regenerate the root
							if (r.again && r.move == Protocol.SWAP) {
								Side.mySide = Side.mySide.opposite();
								root = new Node(new Board(board), Side.mySide);
								root.addNewLayer();
								int bestMove = root.getBestMove();
								sendMsg(Protocol.move(bestMove));
								//System.err.print("His turn , swap and board:\n" + root.getBoard());
							}

							// If it is our turn and we can swap then we should consider swapping
							else if (r.again && canSwap) {
								// If we swap change the sides and continue
								if (r.move <= 2) {
									Side.mySide = Side.mySide.opposite();
									// Now it's his move
									root = new Node(new Board(board), Side.mySide.opposite());

									root.addNewLayer();
									//System.err.println("We are now on side:" + root.getSide());
									sendMsg(Protocol.swap());
								}
								// If we don't swap we need to make a move
								else {
									root = root.getChild(r.move);
									root.addNewLayer();

									int bestMove = root.getBestMove();
									sendMsg(Protocol.move(bestMove));
								}
								//System.err.print("We can swap:\n" + root.getBoard());
							}

							// If it's my turn
							else if (r.again) {
								// We need to record his move
								root = root.getChild(r.move);
								root.addNewLayer();
								//System.err.print("Our turn and board:\n" + root.getBoard());
								int bestMove = root.getBestMove();
								sendMsg(Protocol.move(bestMove));
							}

							// It was his turn after we made ours
							else {
								root = root.getChild(r.move);
								root.addNewLayer();
								//System.err.print("Board recording turn and board:\n" + root.getBoard());
							}

							canSwap = false;

							break;
						case END:  return;
							//System.err.println("An end. Bye bye!");
					}
				} catch (InvalidMessageException e) {
					System.err.println(e.getMessage());
				}
			}
		}
		catch (IOException e)
		{
			System.err.println("This shouldn't happen: " + e.getMessage());
		}
	}
}