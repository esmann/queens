import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class CornerBoard extends Board2{
	// This could be excessive storage, the recursion/iteration tree
	// should contain the full board along a path from root to leaf.

	private int bound1 = 2;

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
		for (bound1 = 2; bound1 < size - 1; bound1++) {
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

}
