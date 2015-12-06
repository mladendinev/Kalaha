package MKAgent;

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
	public static final int DEPTH = 1;

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
		System.err.println("Sending**** " + msg);
		//System.err.println("SENT AT: " + System.currentTimeMillis());

		System.out.println(msg);
		//System.out.flush();
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
	private static void redirectSystemErr(){
		try{
			String filePath = System.getProperty("user.dir") + "/KalahaLog.log";
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
		redirectSystemErr();

		try {
			Board board = new Board(7,7);

			KalahaNode root = new KalahaNode(board, Side.SOUTH); //South always starts
			//root.createChildren(DEPTH);

			boolean canSwap = true;

			String s;
			while (true)
			{
				s = recvMsg(); //todo Thread while waiting for response
				System.err.println("Received: " + s);

				try {
					MsgType mt = Protocol.getMessageType(s);
					switch (mt)
					{
						case START: System.err.println("A start.");
							boolean first = Protocol.interpretStartMsg(s);
							System.err.println("Starting player? " + first);

							Side.mySide = first ? Side.SOUTH : Side.NORTH;

							if(first){
								canSwap = false;
								//sendMsg(Protocol.move(root.getBestMove()));
								sendMsg(Protocol.move(2));
							}
							break;
						case STATE: System.err.println("A state.");
							Protocol.MoveTurn r = Protocol.interpretStateMsg (s, board);
							System.err.println("This was the move: " + r.move);
							System.err.println("Is the game over? " + r.end);
							if (!r.end) System.err.println("Is it our turn again? " + r.again);
							System.err.print("The board as we got it:\n" + board);

							if(canSwap && r.move <= 2){
								sendMsg(Protocol.swap());
								Side.mySide = Side.mySide.opposite();
								root.setSide(Side.mySide);
							}
							else{
								if(r.move == -1){ //SWAP
									Side.mySide = Side.mySide.opposite();
								}

								//root = root.getChild(r.move);
								//root.addNewLayer();
								//root = root.generateChild(r.move);
								System.err.print("The board as we think:\n" + root.getBoard());

								if (r.again) {
									root.setSide(Side.mySide);
									sendMsg(Protocol.move(root.getBestMove()));
								}
								else{
									root.setSide(Side.mySide.opposite());
								}
							}

							canSwap = false;


							break;
						case END: System.err.println("An end. Bye bye!"); return;
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