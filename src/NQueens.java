public class NQueens extends MiG.oneclick.Job
{
	
	public void NQueens(int size)
	{
		int bit;
		/*Initialize*/
		Globals.Size = size;
		Globals.Count8 = Globals.Count4 = Globals.Count2 = 0;
		Globals.SizeE = Globals.Size - 1;
		Globals.Board.BoardE = Globals.SizeE;
		Globals.TopBit = 1 << Globals.SizeE;
		Globals.Mask = (1 << Globals.Size) -1;

		/* 0:000000001 */
		/* 1:011111100 */
		Globals.Board.SetBoard(0, 1);
		for(Globals.Bound1=2; Globals.Bound1<Globals.SizeE; Globals.Bound1++)
		{
			Globals.Board.SetBoard(1, bit = 1 << Globals.Bound1);
			BackTrack1(2, (2 | bit)  << 1, 1 | bit, bit >> 1);
		}

		/* 0:000001110 */
		Globals.SideMask = Globals.LastMask = Globals.TopBit | 1;
		Globals.EndBit = Globals.TopBit >> 1;
		for(Globals.Bound1=1, Globals.Bound2=Globals.Size-2; Globals.Bound1<Globals.Bound2; Globals.Bound1++, Globals.Bound2--)
		{
			Globals.Board.Board1 = Globals.Bound1; 
			Globals.Board.Board2 = Globals.Bound2;
			bit = 1 << Globals.Bound1;
			Globals.Board.SetBoard(0, bit);
			BackTrack2(1, bit<<1, bit, bit>>1);
			Globals.LastMask |= Globals.LastMask >> 1 | Globals.LastMask << 1;
			Globals.EndBit >>= 1;
		}

		/* Unique and Total Solutions */
		Globals.Unique = Globals.Count8 + Globals.Count4 + Globals.Count2;
		Globals.Total = Globals.Count8*8 + Globals.Count4*4 + Globals.Count2*2;

	}

	public  void BackTrack1(int y, int left, int down, int right)
	{
		int bitmap, bit;

		bitmap = Globals.Mask & ~(left|down|right);
		if(y==Globals.SizeE)
		{
			if(bitmap !=0) 
			{
				Globals.Board.SetBoard(y, bitmap);
				Globals.Count8++;
			}
		}
		else
		{
			if(y < Globals.Bound1)
			{
				bitmap |= 2;
				bitmap ^= 2;
			}
			while(bitmap !=0) 
			{
				bit = -bitmap & bitmap;
				Globals.Board.SetBoard(y, bit);
				bitmap ^= Globals.Board.GetBoard(y);
				BackTrack1(y+1, (left|bit) << 1, down | bit, (right | bit) >> 1);
			}
		}		
	}

	public  void BackTrack2(int y, int left, int down, int right)
	{
		int bitmap;
		int bit;

		bitmap = Globals.Mask & ~(left|down|right);
		if(y==Globals.SizeE)
		{
			if(bitmap != 0) 
			{
				if((bitmap & Globals.LastMask) == 0)
				{
					Globals.Board.SetBoard(y, bitmap);
					Check();
				}
			}
		}
		else
		{
			if(y < Globals.Bound1)
			{
				bitmap |= Globals.SideMask;
				bitmap ^= Globals.SideMask;
			}
			else if(y == Globals.Bound2)
			{
				if((down & Globals.SideMask) ==0 ) return;
				if((down & Globals.SideMask) != Globals.SideMask) bitmap &= Globals.SideMask;
			}
			while(bitmap != 0) 
			{
				bit = -bitmap & bitmap;
				Globals.Board.SetBoard(y, bit);
				bitmap ^= Globals.Board.GetBoard(y);
				BackTrack2(y+1, (left | bit) << 1, down | bit, (right | bit) >> 1);
			}
		}
	}

	public void Check()
	{
		int own, you, bit, ptn;

		/* 90-degree rotation */
		if(Globals.Board.GetBoard2(0) == 1)
		{
			for(ptn=2, own=1; own <=Globals.Board.BoardE; own++, ptn <<= 1)  
			{
				bit = 1;
				for(you=Globals.Board.BoardE; Globals.Board.GetBoard(you) != ptn && Globals.Board.GetBoard(own) >= bit; you--) 
				{
					bit <<=1;
				}
				if(Globals.Board.GetBoard(own) > bit) return; 
				if(Globals.Board.GetBoard(own) < bit) break; 
			}
			if(own > Globals.Board.BoardE) 
			{
				Globals.Count2++;
				return;
			}
		}

		/* 180-degree rotation */
		if(Globals.Board.GetBoardE(0) == Globals.EndBit)
		{
			for(you = Globals.Board.BoardE-1, own=1; own <= Globals.Board.BoardE; own++, you--)
			{
				bit = 1;
				for(ptn=Globals.TopBit; ptn!=Globals.Board.GetBoard(you) && Globals.Board.GetBoard(own) >= bit; ptn >>=1)
				{
					bit <<=1;
				}
				if(Globals.Board.GetBoard(own) > bit) return;
				if(Globals.Board.GetBoard(own) < bit) break;
			}
			if(own > Globals.Board.BoardE)
			{
				Globals.Count4++;
				return;
			}
		}

		/* 270-degree rotation */

	if(Globals.Board.GetBoard1(0) == Globals.TopBit)
		{
			for(ptn=Globals.TopBit>>1, own=1; own <= Globals.Board.BoardE; own++, ptn>>=1)
			{
				bit = 1;
				for(you=0; Globals.Board.GetBoard(you) != ptn&& Globals.Board.GetBoard(own) >= bit; you++)
				{
					bit <<= 1;
				}	
				if(Globals.Board.GetBoard(own) > bit) return;
				if(Globals.Board.GetBoard(own) < bit) break;
			}
		}
		Globals.Count8++;

	}

	public void MiG_main(String [] args)
	{
		long starttime;
		long stoptime;
		int size = Integer.parseInt(args[0]);
		if(size<2) size=8;
		err("size: " + size);
		out("size: " + size);
		starttime = System.currentTimeMillis();
		NQueens(size);
		stoptime = System.currentTimeMillis();
		err("\n" + Globals.Size + ": " + Globals.Total + " " + Globals.Unique + " " + (stoptime-starttime)/1000 + " seconds (" + Globals.Count2 + ", " + Globals.Count4 + ", " + Globals.Count8 + ")\n");
		out("\n" + Globals.Size + ": " + Globals.Total + " " + Globals.Unique + " " + (stoptime-starttime)/1000 + " seconds (" + Globals.Count2 + ", " + Globals.Count4 + ", " + Globals.Count8 + ")\n");
	}
}

class Globals
{
	public static int Size = 8;
	public static int SizeE;
	public static int Mask;
	public static int TopBit;
	public static int SideMask;
	public static int LastMask;
	public static int EndBit;
	public static Board Board = new Board(); 
	public static int Bound1;
	public static int Bound2;

	// Should be changed to BigInteger at some point
	public static int Count8;
	public static int Count4;
	public static int Count2;
	public static int Total;
	public static int Unique;
}

class Board
{

	private static int[] Board = new int[30];
	public static int BoardE;
	public static int Board1;
	public static int Board2;

	public static void Board()
	{
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
