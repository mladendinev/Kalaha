package MKAgent;

/**
 * The side of the MKAgent.Kalah board a player can choose.
 */
public enum Side
{
	NORTH, SOUTH;

	/**
	 * @return the side opposite to this one. 
	 */
	public Side opposite()
	{
		switch (this)
		{
			case NORTH: return SOUTH;
			case SOUTH: return NORTH;
			default: return NORTH;  // dummy
		}
	}

	public static Side mySide;

	public static boolean myTurn(Side s){
		return s == mySide;
	}
}
