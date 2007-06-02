import java.util.Collection;
import java.util.LinkedList;

public class CornerBoard extends Board2 {

	// private static final long serialVersionUID = 8858183150323980473L;
	/**
	 * If line<bound => removes possible placements.
	 */
	private int bound1 = 2;

	/**
	 * Counts unique solutions that have 8 symetric solutions
	 */
	private int count8 = 0;

	/**
	 * Creates a new CornerBoard
	 * 
	 * @param size
	 */
	CornerBoard(int size) {
		super(size);
		// First queen is in the corner
		setFirstLine(1);

	}

	public Collection<Board2> init() {
		Collection<Board2> boards = new LinkedList<Board2>();
		CornerBoard bnew;
		for (bound1 = 2; bound1 < sizee; bound1++) {
			//dout("Bound1: " + bound1);

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
		bout.append("COUNT8: " + count8);
		return bout.toString();
	}

	@Override
	public boolean checkBounds() {
		if (currentBoardLine < bound1) {
			possiblePlacements[currentBoardLine] |= 2;
			possiblePlacements[currentBoardLine] ^= 2;
		}
		// Cornerboard is always true
		return true;
	}

	public void setBound(int bound) {
		this.bound1 = bound;
	}

	public synchronized final void backtrackIterative() {
		dout("iterative starts at: " + top);
		int bit;
		int bitmap; // used for minimizing array lookups
		
		while (currentBoardLine >= top) {
			//dout("CurrentBoardLine: " + currentBoardLine);
//			iteration++;
//			if ((iteration % 5000) == 0)
//				System.out.println("iteration nr: " + iteration);

			if (suspendBacktrack) {
				
						dout("DETECTED a suspend request");
					while (suspendBacktrack) {
						try { // We ignore interrupts......
							wait(); //Allow checkpointing to get the monitor
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
			}

			bitmap = this.MASK
					& ~(isOccupiedLeftDiagonal[currentBoardLine]
							| isOccupiedHorizontal[currentBoardLine] | isOccupiedRightDiagonal[currentBoardLine]);

			if (currentBoardLine == sizee) {
				if (bitmap != 0) {
					this.count8++;
					//dout("Solution " + currentBoardLine);
					bitmap = 0; // We take the only solution that exists
				}

			} else {
				if (currentBoardLine < bound1) {
					bitmap |= 2;
					bitmap ^= 2;
				}
			}
			possiblePlacements[currentBoardLine] = bitmap;

			// Go back up if no possibleplacements
			if (bitmap == 0) {

				//dout("No more possible solutions: " + currentBoardLine);
				while ((currentBoardLine >= top)
						&& (possiblePlacements[currentBoardLine]) == 0) {
					currentBoardLine--;
					//dout("Going Back " + currentBoardLine);
				}
			}

			/*
			 * Get next "sibling": Select first onbit (from right) Save our
			 * selected bit on the board Remove the selection from possible
			 * solutions (So it isn't chosen again)
			 */
			possiblePlacements[currentBoardLine] ^= board[currentBoardLine] = -possiblePlacements[currentBoardLine]
					& possiblePlacements[currentBoardLine];

			bit = board[currentBoardLine]; // Previously chosen

			isOccupiedLeftDiagonal[currentBoardLine + 1] = (isOccupiedLeftDiagonal[currentBoardLine] | bit) << 1;
			isOccupiedHorizontal[currentBoardLine + 1] = (isOccupiedHorizontal[currentBoardLine] | bit);
			isOccupiedRightDiagonal[currentBoardLine + 1] = (isOccupiedRightDiagonal[currentBoardLine] | bit) >> 1;

			if (currentBoardLine >= top) {
				currentBoardLine++; // Go to child/Next line
			}

		}

	}

	public final void backtrackRecursive(final int y, final int left,
			final int down, final int right) {

		//dout("BTCORNER y: " + y);

		/*
		 * System.out.println("size: " + size); System.out.println("bound1: " +
		 * bound1); System.out.println("MASK: " + this.MASK);
		 */

		int bitmap, bit;

		bitmap = this.MASK & ~(left | down | right);
		//dout("BTCORNER bitmap: " + Integer.toBinaryString(bitmap));

		if (y == sizee) {
			if (bitmap != 0) {
				// board[y] = bitmap;
				// System.out.println("b1: " + y + ", " + left + ", " + down +
				// ", " + right);
//				System.out.println("SOLUTION!\n");
				this.count8++;
			}
		} else {
			if (y < bound1) {
				bitmap |= 2;
				bitmap ^= 2;
			}
			while (bitmap != 0) {
				// System.out.println("bitmap != 0");
				bit = -bitmap & bitmap;
				board[y] = bit;
				bitmap ^= board[y];
				backtrackRecursive(y + 1, (left | bit) << 1, down | bit,
						(right | bit) >> 1);
			}
		}
	}

	@Override
	int getTotal() {
		return count8 * 8;
	}

	@Override
	int getUnique() {
		// System.out.println("b1(" + Count8 + ")");
		return count8;
	}

}
