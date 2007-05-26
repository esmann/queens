import java.io.Serializable;
import java.util.Collection;
import java.util.Formattable;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Board2 implements Cloneable, Serializable {

	protected int Board[];

	protected int leftDiagonal, rightDiagonal, horizontal = 0;

	protected int currentLine = 0; // zero indexed nxn board

	private boolean recursive = false;

	// removed static
	protected int MASK;

	// removed static
	protected int size;

	// removed static
	/**
	 * 
	 */
	protected int sizee = 0;

	protected int nextPossible;

	public Board2(int size) {
		
		this.size = size;
		this.sizee = size - 1;
		Board = new int[size];
		MASK = (1 << size) - 1;
	}

	void setRecursive(boolean b) {
		this.recursive = b;
	}

	protected boolean isRecursive() {
		return  this.recursive;
	}

	abstract boolean isSolution();

	abstract Collection<Board2> init();

	abstract boolean checkBounds();

	abstract void backtrack();

	abstract int getUnique();

	abstract int getTotal();

	@Override
	public Object clone() throws CloneNotSupportedException {
		Board2 b = (Board2) super.clone();
		b.Board = new int[this.size];
		System.arraycopy(this.Board, 0, b.Board, 0, this.size);
		return (Object) b;
	}

	protected void setLine(int selectedbit) {
		Board[currentLine] = selectedbit;
	}

	protected void setAndIncLine(int selectedbit) {
		setLine(selectedbit);

		currentLine++;
		// Calculate occupied possitions of previous queen placements

		leftDiagonal = (leftDiagonal | selectedbit) << 1;
		rightDiagonal = (rightDiagonal | selectedbit) >> 1;
		horizontal = horizontal | selectedbit;
		nextPossible = MASK & ~(leftDiagonal | horizontal | rightDiagonal);

	}

	protected int getLine() {
		return currentLine;
	}

	protected int getBoardLine() {
		return Board[getLine()];
	}

	public String toString() {
		StringBuffer bout = new StringBuffer();

		for (int i = 0; i <= sizee; i++) {
			bout.append(Integer.numberOfTrailingZeros(Board[i]) + "\n");
		}

		return bout.toString();
	}

	public boolean isCompleteBoard() {
		return (currentLine == size - 1);
	}

	// Create a new linkedlist every time... Slow?
	public Queue<Board2> iterateLine() {
		Queue<Board2> boards = new LinkedList<Board2>();
		try {
			int selection;
			if (!this.checkBounds())
				return boards;

			int tmp = nextPossible;
			while ((selection = Integer.lowestOneBit(tmp)) != 0) {
				tmp ^= selection; // Set the selected bit to 0 in the original
				// pattern
				Board2 bnew = (Board2) this.clone();
				bnew.setAndIncLine(selection);
				/*
				 * if (bnew.isCompleteBoard()) { bnew.isSolution(); // FIXME we
				 * should count solutions continue; }
				 */

				// Bounds check might invalidate a board

				boards.add(bnew);
			}
		} catch (CloneNotSupportedException e) {
			// foooooooo does not happen....
			e.printStackTrace();
		}
		return boards;
	}
}
