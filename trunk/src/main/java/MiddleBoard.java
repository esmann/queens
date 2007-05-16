import java.util.Collection;
import java.util.LinkedList;

public class MiddleBoard extends Board2 {
	//removed static
	private int TOPBIT;
	//removed final
	private int SIDEMASK;
	private int LASTMASK, ENDBIT;

	private int bound1, bound2;

	private int Count2, Count8, Count4;

	public MiddleBoard(int size) {
		super(size);
		
		TOPBIT = 1 << sizee; //size-1
		SIDEMASK = TOPBIT | 1;
		LASTMASK = TOPBIT | 1;
		ENDBIT = TOPBIT >> 1;
	}

	@Override
	Collection<Board2> init() {
		Collection<Board2> boards = new LinkedList<Board2>();
		MiddleBoard bnew;
		for (bound1 = 1, bound2 = size - 2; bound1 < bound2; bound1++, bound2--) {
			try {
				bnew = (MiddleBoard) this.clone();
			} catch (CloneNotSupportedException e) {
				// should never happen
				e.printStackTrace();
				throw new InternalError(e.toString());
			}
			bnew.setAndIncLine(1 << bound1); // Af few more boards
			boards.add(bnew);
			LASTMASK |= LASTMASK >> 1 | LASTMASK << 1;
			ENDBIT >>= 1;
		}
		return boards;
	}

	@Override
	boolean isSolution() {
		if (nextPossible != 0) {
			if ((nextPossible & LASTMASK) == 0) {
				setLine(nextPossible);
				return true;
				// Globals.Board.SetBoard(y, bitmap);
				// Her skal Board med som argument
				// Check();
			}
		}
		return false;
	}

	public String toString() {

		StringBuffer bout = new StringBuffer(super.toString());
		return bout.toString();
	}

	@Override
	public boolean checkBounds() {

		if (getLine() < bound1) {
			nextPossible |= SIDEMASK;
			nextPossible ^= SIDEMASK;
		} else if (getLine() == this.bound2) {

			if ((horizontal & SIDEMASK) == 0)
				// This board can never become a solution
				return false;

			if ((horizontal & SIDEMASK) != SIDEMASK)
				nextPossible &= SIDEMASK;
		}

		return true;
	}

	public void backtrackMiddle(int y, int left, int down, int right) {
	
		//System.out.println("backtrackMiddle");
		
		int bitmap;
		int bit;
		
		bitmap = MASK & ~(left | down | right);
		
		if (y == sizee) {
			if (bitmap != 0) {
				if ((bitmap & LASTMASK) == 0) {
					Board[y] = bitmap;
					//System.out.println("b2: " + y + ", " + left + ", " + down + ", " + right);
					this.Check();
				}
			}
		} else {
			if (y < bound1) {
				bitmap |= SIDEMASK;
				bitmap ^= SIDEMASK;
			} else if (y == bound2) {
				
				if ((down & SIDEMASK) == 0) return;

				if ((down & SIDEMASK) != SIDEMASK) {					
					bitmap &= SIDEMASK;					
				}
			}
			while (bitmap != 0) {		
				bit = -bitmap & bitmap;				
				Board[y] = bit;
				bitmap ^= Board[y];
				backtrackMiddle(y + 1, (left | bit) << 1, down | bit,
						(right | bit) >> 1);
			}
			NQueenBoards.dout("<");			
		}
	}

	@Override
	void backtrack() {
		//System.out.println("MASK: " + this.MASK);
		backtrackMiddle(currentLine, leftDiagonal, horizontal, rightDiagonal);
	}

	public void Check() {
		int own, you, bit, ptn;
		
		/* 90-degree rotation */		
		if (Board[bound2] == 1) {
			for (ptn = 2, own = 1; own <= sizee; own++, ptn <<= 1) {
				bit = 1;
				for (you = sizee; Board[you] != ptn && Board[own] >= bit; you--) {
					bit <<= 1;
				}
				if (Board[own] > bit)
					return;
				if (Board[own] < bit)
					break;
			}
			if (own > sizee) {
				Count2++;
				return;
			}
		}

		/* 180-degree rotation */
		if (Board[sizee] == ENDBIT) {
			for (you = sizee - 1, own = 1; own <= sizee; own++, you--) {
				bit = 1;
				for (ptn = TOPBIT; ptn != Board[you] && Board[own] >= bit; ptn >>= 1) {
					bit <<= 1;
				}
				if (Board[own] > bit)
					return;
				if (Board[own] < bit)
					break;
			}
			if (own > sizee) {
				Count4++;
				return;
			}
		}

		/* 270-degree rotation */

		if (Board[bound1] == TOPBIT) {
			for (ptn = TOPBIT >> 1, own = 1; own <= sizee; own++, ptn >>= 1) {
				bit = 1;
				for (you = 0; Board[you] != ptn && Board[own] >= bit; you++) {
					bit <<= 1;
				}
				if (Board[own] > bit)
					return;
				if (Board[own] < bit)
					break;
			}
		}
		Count8++;

	}

	@Override
	int getTotal() {
		return this.Count2 * 2 + this.Count4 * 4 + this.Count8 * 8;
	}

	@Override
	int getUnique() {
		//System.out.println("b2(" + this.Count2 + ", " + this.Count4 + ", " + this.Count8 + ")");
		return this.Count2 + this.Count4 + this.Count8;
	}
}
