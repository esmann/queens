import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class CornerBoard implements Cloneable {
	// This could be excessive storage, the recursion/iteration tree
	// should contain the full board along a path from root to leaf.
	private int Board[];

	private int leftDiagonal, rightDiagonal, horizontal = 0;

	private int MASK;

	private int currentLine = 0; // zero indexed nxn board

	private int bound1 = 2;

	private int size;
	
	private int nextPossible;

	/**
	 * Creates a new border
	 * 
	 * @param size
	 */
	CornerBoard(int size) {
		this.size = size;
		Board = new int[size];
		// MASK is the amount of ones from the left in the bitstring
		MASK = (1 << size) - 1;

		setAndIncrline(1); // First queen is in right corner

	}
	public String toString() {
		StringBuffer bout = new StringBuffer();
		
		for (int i = 0; i <= currentLine; i++) {
			bout.append(Integer.toBinaryString(Board[i]) + "\n");
		}
		return bout.toString();
	}
	public Collection<CornerBoard> init() {
		Collection<CornerBoard> boards = new LinkedList<CornerBoard>();
		CornerBoard bnew;
		for (bound1 = 2; bound1 < size - 1; bound1++) {
			
			try {
				bnew = (CornerBoard) this.clone();
			} catch (CloneNotSupportedException e) {
				// should never happen
				e.printStackTrace();
				throw new InternalError(e.toString());
			}
			bnew.setAndIncrline(1 << bound1); // Af few more boards
			boards.add(bnew);
		}
		return boards;
	}

	private boolean isSolution() {
		return (size == (currentLine + 1) && ((Board[currentLine]) != 0));
	}

	void setAndIncrline(int selectedbit) {
		Board[currentLine] = selectedbit;
		// FIXME if y>size

		currentLine++;
		// Calculate occupied possitions of previous queen placements

		leftDiagonal = (leftDiagonal | selectedbit) << 1;
		rightDiagonal = (rightDiagonal | selectedbit) >> 1;
		horizontal = horizontal | selectedbit;

		// Possible choiches for next line
		nextPossible = MASK & ~(leftDiagonal | horizontal | rightDiagonal);
		
		if (currentLine < bound1) {
			nextPossible |= 2;  
			nextPossible ^= 2;
		}
	}

	// spawnNewBoards()

	@Override
	public Object clone() throws CloneNotSupportedException {
		CornerBoard cb =  (CornerBoard) super.clone();
		cb.Board = new int[this.size];
		System.arraycopy(this.Board, 0, cb.Board, 0, this.size);
		return (Object) cb;
	}

	// Create a new linkedlist every time... Slow?
	public Queue<CornerBoard> iterateLine() {
		Queue<CornerBoard> boards = new LinkedList<CornerBoard>();
		try {			
			int selection;
			int tmp = nextPossible;
			while ((selection = Integer.lowestOneBit(tmp)) != 0) {
				tmp ^= selection; // Set the selected bit to 0 in the original
									// pattern

				CornerBoard bnew = (CornerBoard) this.clone();
				bnew.setAndIncrline(selection);
				boards.add(bnew);
			}
		} catch (CloneNotSupportedException e) {
			//foooooooo does not happen....
			e.printStackTrace();
		}
		return boards;
	}

	public void setBound(int bound) {
		this.bound1 = bound;

	}

}
