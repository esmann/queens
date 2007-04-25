public class Board
{
    
	private static int[] Board;
	public static int BoardE;
	public static int Board1;
	public static int Board2;

	public Board() {
		
	}
	
	public Board(int size)
	{
		Board = new int[size];
	}

	public static int GetBoard(int index)
	{
		return Board[index];
	}
	
	public static void SetBoard(int index, int value)
	{
		Board[index] = value;
	}

	public static int GetBoardE(int index)
	{
		return Board[BoardE+index];
	}
	
	public static void SetBoardE(int index, int value)
	{
		Board[BoardE+index] = value;
	}

	public static int GetBoard1(int index)
	{
		return Board[Board1+index];
	}
	
	public static void SetBoard1(int index, int value)
	{
		Board[Board1+index] = value;
	}

	public static int GetBoard2(int index)
	{
		return Board[Board2+index];
	}
	
	public static void SetBoard2(int index, int value)
	{
		Board[Board2+index] = value;
	}
}
