import java.util.Collection;
import java.util.LinkedList;

public class MiddleBoard extends Board2 {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7968646024953795581L;
	
	private static int TOPBIT;

	private int SIDEMASK;
	private int LASTMASK, ENDBIT;

	private int bound1, bound2;

	private int count2, count8, count4;

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
					board[y] = bitmap;
					//System.out.println("b2: " + y + ", " + left + ", " + down + ", " + right);
					this.check();
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
				board[y] = bit;
				bitmap ^= board[y];
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

	public void check() {
		int own, you, bit, ptn;
		
		/* 90-degree rotation */		
		if (board[bound2] == 1) {
			for (ptn = 2, own = 1; own <= sizee; own++, ptn <<= 1) {
				bit = 1;
				for (you = sizee; board[you] != ptn && board[own] >= bit; you--) {
					bit <<= 1;
				}
				if (board[own] > bit)
					return;
				if (board[own] < bit)
					break;
			}
			if (own > sizee) {
				count2++;
				return;
			}
		}

		/* 180-degree rotation */
		if (board[sizee] == ENDBIT) {
			for (you = sizee - 1, own = 1; own <= sizee; own++, you--) {
				bit = 1;
				for (ptn = TOPBIT; ptn != board[you] && board[own] >= bit; ptn >>= 1) {
					bit <<= 1;
				}
				if (board[own] > bit)
					return;
				if (board[own] < bit)
					break;
			}
			if (own > sizee) {
				count4++;
				return;
			}
		}

		/* 270-degree rotation */

		if (board[bound1] == TOPBIT) {
			for (ptn = TOPBIT >> 1, own = 1; own <= sizee; own++, ptn >>= 1) {
				bit = 1;
				for (you = 0; board[you] != ptn && board[own] >= bit; you++) {
					bit <<= 1;
				}
				if (board[own] > bit)
					return;
				if (board[own] < bit)
					break;
			}
		}
		count8++;

	}

	@Override
	int getTotal() {
		return this.count2 * 2 + this.count4 * 4 + this.count8 * 8;
	}

	@Override
	int getUnique() {
		//System.out.println("b2(" + this.Count2 + ", " + this.Count4 + ", " + this.Count8 + ")");
		return this.count2 + this.count4 + this.count8;
	}
}
