import java.util.Collection;
import java.util.LinkedList;
import java.util.Stack;

public class CornerBoard extends Board2 {
	// This could be excessive storage, the recursion/iteration tree
	// should contain the full board along a path from root to leaf.

	private int bound1 = 2;

	private int Count8 = 0;

	protected int LeftDiagonal[], RightDiagonal[], Horizontal[], bitmap2[];

	protected int currentLine2;

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
			NQueenBoards.dout("" + bound1);
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

	/**
	 * 
	 * 
	 * 
	 * 
	 * For hver linie For hver et-bit i linien (While) check om y=n
	 * 
	 * @param rightDiagonal2
	 * @param horizontal2
	 * @param leftDiagonal2
	 * @param currentLine
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	public void nonrecursive() {
		int bit;

		currentLine2 = currentLine;

		while (currentLine2 >= currentLine) {

			bit = Board[currentLine2 - 1]; // Previously chosen

			LeftDiagonal[currentLine2] = (LeftDiagonal[currentLine2 - 1] | bit) << 1;
			Horizontal[currentLine2] = (Horizontal[currentLine2 - 1] | bit);
			RightDiagonal[currentLine2] = (RightDiagonal[currentLine2 - 1] | bit) >> 1;

			bitmap2[currentLine2] = this.MASK
					& ~(LeftDiagonal[currentLine2] | Horizontal[currentLine2] | RightDiagonal[currentLine2]);

			if (bitmap2[currentLine2] != 0) {
				// Select first onebit (from right)
				bit = -bitmap2[currentLine2] & bitmap2[currentLine2];
				// Save our current selection
				Board[currentLine2] = bit;
				// Remove this selection from bitmap2
				bitmap2[currentLine2] ^= Board[currentLine2];

				currentLine2++; // Go to child/Next line

				if (currentLine2 < bound1) {
					bitmap2[currentLine2] |= 2;
					bitmap2[currentLine2] ^= 2;
				}

			} else {
				if (currentLine2 - 1 == size) {
					this.Count8++;
				}

				while ((currentLine2 >= currentLine)
						&& (bitmap2[currentLine2]) == 0) {
					currentLine2--;
				}

				// Select first onebit (from right)

				bit = -bitmap2[currentLine2] & bitmap2[currentLine2];
				// Save our current selection
				Board[currentLine2] = bit;
				// Remove this selection from bitmap2
				bitmap2[currentLine2] ^= Board[currentLine2];

				currentLine2++;
			}

		}

	}

	public void backtrackCorner(int y, int left, int down, int right) {
		NQueenBoards.dout("BTCORNER y: " + y);
		/*
		 * System.out.println("size: " + size); System.out.println("bound1: " +
		 * bound1); System.out.println("MASK: " + this.MASK);
		 */

		int bitmap, bit;

		bitmap = this.MASK & ~(left | down | right);
		NQueenBoards.dout("BTCORNER bitmap: " + Integer.toBinaryString(bitmap));
		if (y == size - 1) {
			if (bitmap != 0) {
				Board[y] = bitmap;
				// System.out.println("b1: " + y + ", " + left + ", " + down +
				// ", " + right);
				this.Count8++;
			}
		} else {
			if (y < bound1) {
				bitmap |= 2;
				bitmap ^= 2;
			}
			while (bitmap != 0) {
				// System.out.println("bitmap != 0");
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
		// System.out.println("MASK: " + this.MASK);

		LeftDiagonal[currentLine - 1] = leftDiagonal;
		Horizontal[currentLine - 1] = horizontal;
		RightDiagonal[currentLine - 1] = rightDiagonal;
		nonrecursive();
	}

	@Override
	int getTotal() {
		return Count8 * 8;
	}

	@Override
	int getUnique() {
		// System.out.println("b1(" + Count8 + ")");
		return Count8;
	}

}
