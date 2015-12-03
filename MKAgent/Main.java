package MKAgent;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * The main application class. It also provides methods for communication
 * with the game engine.
 */
public class Main
{
	public static final int DEPTH = 4;


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

		return message.toString();
	}

	/**
	 * The main method, invoked when the program is started.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args)
	{
		try {
			String filePath = System.getProperty("user.dir");
			filePath += "/KalahaLog.log";
			OutputStream output = new FileOutputStream(filePath);

			PrintStream printOut = new PrintStream(output);
			System.setErr(printOut);
			System.err.println(filePath);

			// Create a board
			Board b = new Board(7,7);
			Kalah kalahGame = new Kalah(b);
			// Create the root of the tree
			KalahaNode root = null;

			int moveMade = 2;

			String s;
			while (true)
			{
				System.err.println();
				s = recvMsg();
				System.err.print("Received: " + s);
				System.err.flush();
				// Create a game using this board

				try {
					MsgType mt = Protocol.getMessageType(s);
					switch (mt)
					{
						case START: System.err.println("A start.");
							boolean first = Protocol.interpretStartMsg(s);
							System.err.println("Starting player? " + first);

							Side.mySide = first ? Side.SOUTH : Side.NORTH;

							root = new KalahaNode(kalahGame, Side.SOUTH);
							KalahaNode.createChildren(root, DEPTH);

							//System.err.println("MINIMAX of the ROOT: " + Minimax.minimax(root, 6));

							System.err.println("Alpha beta prunning ROOT: " + Minimax.alphabeta(root, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE));

//							for(KalahaNode rootChild : root.getChildren()){
//								//System.err.println("MINIMAX of the child: " + Minimax.minimax(rootChild, 5));
//								System.err.println("Alpha beta prunning: " + Minimax.alphabeta(rootChild, 8, Integer.MIN_VALUE, Integer.MAX_VALUE));
//
//							}



							sendMsg("MOVE;" + moveMade);
							break;
						case STATE: System.err.println("A state.");
							Protocol.MoveTurn r = Protocol.interpretStateMsg (s, kalahGame.getBoard());
							System.err.println("This was the move: " + r.move);
							System.err.println("Is the game over? " + r.end);
							if (!r.end) System.err.println("Is it our turn again? " + r.again);
							System.err.print("The board:\n" + kalahGame.getBoard());
							if(r.again){
								//we make a move on our root
								System.err.println("This was our move: " + r.move);


								root = root.getChild(r.move);
								System.err.println(root.getChildren().toString());
								System.err.println(root.toString(0));


							}
							else{
								System.err.println("This was the move: " + r.move);


								root = root.getChild(r.move);
								System.err.println(root.getChildren().toString());
								System.err.println(root.toString(0));

							}









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