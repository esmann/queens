import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class CornerBoard extends Board2 {
	// This could be excessive storage, the recursion/iteration tree
	// should contain the full board along a path from root to leaf.

	private int bound1 = 2;
	private int Count8 = 0;

	/**
	 * Creates a new CornerBoard
	 * 
	 * @param size
	 */
	CornerBoard(int size) {
		super(size);

		setAndIncLine(1); // First queen is in right corner

	}

	public Collection<Board2> init() {
		Collection<Board2> boards = new LinkedList<Board2>();
		CornerBoard bnew;
		for (bound1 = 2; bound1 < sizee; bound1++) {
			NQueenBoards.dout("" +bound1);
			try {
				bnew = (CornerBoard) this.clone();
			} catch (CloneNotSupportedException e) {
				// should never happen
				e.printStackTrace();
				throw new InternalError(e.toString());
			}
			bnew.setAndIncLine(1 << bound1); // Af few more boards
			boards.add(bnew);
		}
		return boards;
	}
	public String toString() {
		
		StringBuffer bout = new StringBuffer(super.toString());
		bout.append("BOUND1: " + bound1);
		bout.append("COUNT8: " + Count8);
		return bout.toString();
	}
	public boolean isSolution() {
		return ((Board[currentLine]) != 0);
	}

	public boolean checkBounds() {
		if (currentLine < bound1) {
			nextPossible |= 2;
			nextPossible ^= 2;
		}
		// Cornerboard is always true
		return true;
	}

	public void setBound(int bound) {
		this.bound1 = bound;
	}

	public void backtrackCorner(int y, int left, int down, int right) {
		NQueenBoards.dout("BTCORNER y: " + y);
		
		int bitmap, bit;

		bitmap = this.MASK & ~(left | down | right);
		NQueenBoards.dout("BTCORNER bitmap: " +  Integer.toBinaryString(bitmap));
		if (y == size-1) {
			if (bitmap != 0) {
				Board[y] = bitmap;
				this.Count8++;
			}
		} else {
			if (y < bound1) {
				bitmap |= 2;
				bitmap ^= 2;
			}
			while (bitmap != 0) {
				bit = -bitmap & bitmap;
				Board[y] = bit;
				bitmap ^= Board[y];
				backtrackCorner(y + 1, (left | bit) << 1, down | bit,
						(right | bit) >> 1);
			}
		}
	}

	@Override
	void backtrack() {
		
		backtrackCorner(currentLine, leftDiagonal, horizontal, rightDiagonal);
	}

	@Override
	int getTotal() {
		return Count8 * 8;
	}

	@Override
	int getUnique() {
		return Count8;
	}

}
