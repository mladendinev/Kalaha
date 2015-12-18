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

		return message.toString();
	}

	private static void redirectSystemErr(boolean first){
		try{
			String filePath = 	System.getProperty("user.dir") +
								"/KalahLog_" + first + "_" +
								System.getProperty("java.version") + ".log";

			OutputStream output = new FileOutputStream(filePath);

			PrintStream printOut = new PrintStream(output);
			System.setErr(printOut);
		}
		catch(Exception e){
			System.err.println("Exception " + e.getMessage());
		}
	}

	private static void makeBestMove(Node node){
		sendMsg(Protocol.move(node.getBestMove()));
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

				try {
					MsgType mt = Protocol.getMessageType(s);
					switch (mt)
					{
						case START: //System.err.println("A start.");
							boolean first = Protocol.interpretStartMsg(s);

							Side.mySide = first ? Side.SOUTH : Side.NORTH;

							// If I am the starting player
							if(first){
								canSwap = false;
								sendMsg(Protocol.move(2)); //Our opening move is 2
							}
							break;
						case STATE:
							Protocol.MoveTurn r = Protocol.interpretStateMsg (s, board);

							// If I am the starting player and the opponent swapped
							// I need to change sides and regenerate the root
							if (r.again && r.move == Protocol.SWAP) {
								Side.mySide = Side.mySide.opposite();
								root = new Node(new Board(board), Side.mySide);
								root.addNewLayer();

								makeBestMove(root);
							}
							// If it is my turn and I can swap, then I should consider swapping
							else if (r.again && canSwap) {
								// If we swap change the sides and continue
								if (r.move <= 2) {
									Side.mySide = Side.mySide.opposite();
									// Now it's his move
									root = new Node(new Board(board), Side.mySide.opposite());
									root.addNewLayer();

									sendMsg(Protocol.swap());
								}
								// If I don't swap, I need to make a move
								else {
									root = root.getChild(r.move);
									root.addNewLayer();

									makeBestMove(root);
								}
							}

							// If it's my turn
							else if (r.again) {
								// We need to record his move
								root = root.getChild(r.move);
								root.addNewLayer();

								makeBestMove(root);
							}

							// The opponent made a move
							else {
								root = root.getChild(r.move);
								root.addNewLayer();
							}

							canSwap = false;

							break;
						case END:
							return;
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