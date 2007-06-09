public class NQueensL
{
	public static void NQueensL(int size)//{{{
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
			// Her skal Bound1 gives med som argument saa man kan lave alle kaldene i for loopet parallelt. 
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
			// Her skal Bound1, Bound2 og Board med som argument
			BackTrack2(1, bit<<1, bit, bit>>1);
			Globals.LastMask |= Globals.LastMask >> 1 | Globals.LastMask << 1;
			Globals.EndBit >>= 1;
		}

		/* Unique and Total Solutions */
		Globals.Unique = Globals.Count8 + Globals.Count4 + Globals.Count2;
		Globals.Total = Globals.Count8*8 + Globals.Count4*4 + Globals.Count2*2;

	}//}}}

	public static void BackTrack1(int y, int left, int down, int right)//{{{
	{
		int bitmap, bit;
		//Globals.b1iter++;		
		//Mask bliver brugt til at fjerne muligheden for spejlinger (?)
		//left, down, right er de steder hvor der ikke kan staa noget (ie der staar allerede noget og spaerrer)
		//bitmap er de mulige pladser vi har til raadighed
		bitmap = Globals.Mask & ~(left|down|right);
		if(y==Globals.SizeE)
		{
			if(bitmap !=0) 
			{
				//Board bliver her kun brugt til display formaal!?
				//Globals.Board.SetBoard(y, bitmap);
				Globals.Count8++;
			}
		}
		else
		{
			// Begrænsninger af løsninger, bound1 bliver sat i 
			// anden iteration (første iteration sætter droningen i hjørnet)
			
			if(y < Globals.Bound1)
			{
				bitmap |= 2; 
				// 
				bitmap ^= 2;
				//
			}
			while(bitmap !=0) 
			{
				// Vi fjerner et 1-tal hver gang
				bit = -bitmap & bitmap;
				//Board bliver her kun brugt til display formaal!?
				//Globals.Board.SetBoard(y, bit);
				bitmap ^= bit;//Globals.Board.GetBoard(y);
				
				BackTrack1(y+1, (left|bit) << 1, down | bit, (right | bit) >> 1);
			}
		}		
	}//}}}

	public static void BackTrack2(int y, int left, int down, int right)//{{{
	{
		int bitmap;
		int bit;
		//Globals.b2iter++;		

		bitmap = Globals.Mask & ~(left|down|right);
		if(y==Globals.SizeE)
		{
			if(bitmap != 0) 
			{
				if((bitmap & Globals.LastMask) == 0)
				{
					Globals.Board.SetBoard(y, bitmap);
					//Her skal Board med som argument
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
	}//}}}

	public static void Check()//{{{
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

	}//}}}

	public static void main(String [] args)//{{{
	{
		long starttime;
		long stoptime;
		int size = 16;
		if(args.length > 0)
		{
			size = Integer.parseInt(args[0]);
		}
				starttime = System.currentTimeMillis();
				NQueensL(size);
				stoptime = System.currentTimeMillis();
				double time = stoptime-starttime;
				
				System.out.println(Globals.Size + ": " + Globals.Total + " " + Globals.Unique + " "  + (time/1000));
				//System.out.println(Globals.b1iter + ", " + Globals.b2iter);
	}//}}}
}
