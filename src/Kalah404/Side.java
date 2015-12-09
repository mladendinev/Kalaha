package Kalah404;

/**
 * The side of the Kalah404.Kalah board a player can choose.
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

	public static Side mySide = Side.SOUTH;

	public static boolean myTurn(Side s){
		return s == mySide;
	}
}
