package Kalah404;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

			Node root = new Node(new Board(board), Side.SOUTH); // South always starts
			root.addNewLayer();

			List<MonteCarloThread> monteCarloThreads = new ArrayList<MonteCarloThread>(49);

			boolean canSwap = true;

			int depth = 0;

			String s;
			while (true)
			{
				s = recvMsg();
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
								sendMsg(Protocol.move(2));
							}
							break;
						case STATE: System.err.println("A state.");

							if(depth > 999) { //minimax while waiting instead???
								for(MonteCarloThread t : monteCarloThreads){
									t.requestStop();
									//t.interrupt();
								}

								/*
								for (MonteCarloThread t : monteCarloThreads) {
									try {
										t.join();
									} catch (InterruptedException e) {
										System.err.println("Interrupted!!!");
									}
								}*/

								monteCarloThreads.clear();
							}

							Protocol.MoveTurn r = Protocol.interpretStateMsg (s, board);
							System.err.println("This was the move: " + r.move);
							System.err.println("Is the game over? " + r.end);
							if (!r.end) System.err.println("Is it our turn again? " + r.again);
							System.err.print("The board as we got it:\n" + board);

							if(canSwap && r.move <= 2){
								Side.mySide = Side.mySide.opposite();

								root = root.getChild(r.move);
								if(r.move == 1){
									root.setSide(Side.mySide.opposite());
								}
								root.addNewLayer();

								sendMsg(Protocol.swap());
							}
							else{
								if(r.move == Protocol.SWAP){
									Side.mySide = Side.mySide.opposite();
								}
								else{
									depth++;

									root = root.getChild(r.move);
									root.addNewLayer();
								}

								if (r.again) {
									int bestMove = root.getBestMove();
									sendMsg(Protocol.move(bestMove));
								}
								else{
									// opponent's turn

									if(depth > 999) {
										List<Node> nextMoves = root.getChildrenSorted();
										Collections.reverse(nextMoves);

										int j = 0;

										for (Node nextMove : nextMoves) {
											if(++j == 3){
												break;
											}
											List<Node> nextNextMoves = nextMove.getChildrenSorted();

											int u = 0;
											for (Node node : nextNextMoves) {
												if(++u == 3){
													break;
												}
												monteCarloThreads.add(new MonteCarloThread(node));
											}
										}

										for (MonteCarloThread t : monteCarloThreads) {
											t.start();
										}
									}
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