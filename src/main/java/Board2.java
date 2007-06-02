import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Board2 implements Cloneable, Serializable {

	protected int board[];

	// protected int leftDiagonal, rightDiagonal, horizontal = 0;

	// protected int currentLine = 0; // zero indexed nxn board

	protected int currentBoardLine = 0;

	private boolean recursive = false;

	protected int MASK;

	protected int size;

	// Used for iterative
	protected int isOccupiedLeftDiagonal[], isOccupiedRightDiagonal[],
			isOccupiedHorizontal[], possiblePlacements[];

	public static void dout(String s) {
		System.out.println(s);
	}

	// removed static
	/**
	 * import java.io.ObjectOutputStream;
	 */
	protected int sizee = 0;

	//protected int nextPossible;

	protected boolean suspendBacktrack = false;

	private boolean checkpointing = true;

	public Board2(int size) {

		this.size = size;
		this.sizee = size - 1;
		board = new int[size + 1];
		isOccupiedRightDiagonal = new int[size];
		isOccupiedLeftDiagonal = new int[size];
		isOccupiedHorizontal = new int[size];
		possiblePlacements = new int[size];
		MASK = (1 << size) - 1;
	}

	public void setRecursive(boolean b) {
		this.recursive = b;
	}

	protected boolean isRecursive() {
		return this.recursive;
	}

	abstract Collection<Board2> init();

	abstract boolean checkBounds();

	abstract void backtrackRecursive(int top, int leftDiagonal, int horizontal,
			int rightDiagonal);

	abstract void backtrackIterative();

	public void backtrack() {
		// System.out.println("MASK: " + this.MASK);

		if (isRecursive()) {
			System.out.println(this.toString());
			backtrackRecursive(currentBoardLine,
					isOccupiedLeftDiagonal[currentBoardLine],
					isOccupiedHorizontal[currentBoardLine],
					isOccupiedRightDiagonal[currentBoardLine]);
		} else {
			backtrackIterative();
		}

	}

	abstract int getUnique();

	abstract int getTotal();

	@Override
	public Object clone() throws CloneNotSupportedException {
		Board2 b = (Board2) super.clone();
		b.board = new int[this.size];
		b.isOccupiedHorizontal = new int[this.size];
		b.isOccupiedLeftDiagonal = new int[this.size];
		b.isOccupiedRightDiagonal = new int[this.size];
		b.possiblePlacements = new int[this.size];

		System.arraycopy(this.board, 0, b.board, 0, this.size);
		System.arraycopy(this.isOccupiedHorizontal, 0, b.isOccupiedHorizontal,
				0, this.size);
		System.arraycopy(this.isOccupiedLeftDiagonal, 0,
				b.isOccupiedLeftDiagonal, 0, this.size);
		System.arraycopy(this.isOccupiedRightDiagonal, 0,
				b.isOccupiedRightDiagonal, 0, this.size);
		System.arraycopy(this.possiblePlacements, 0, b.possiblePlacements, 0,
				this.size);
		return (Object) b;
	}

	protected void setFirstLine(int selected) {
		System.out.println("Setting line:"  + currentBoardLine);
		board[currentBoardLine] = selected;

		currentBoardLine++;
		
		isOccupiedHorizontal[currentBoardLine] = selected;
		
		isOccupiedLeftDiagonal[currentBoardLine] = selected  << 1;
		isOccupiedRightDiagonal[currentBoardLine] = selected >> 1;
		
		possiblePlacements[currentBoardLine] = MASK & ~(selected);
		
	}

	private void setLine(int selectedbit) {
		board[currentBoardLine] = selectedbit;
	}

	protected void setAndIncLine(int selectedbit) {
		setLine(selectedbit);

		System.out.println("Setting line:"  + currentBoardLine);
		System.out.println("with "  + Integer.toBinaryString(board[currentBoardLine]));

		currentBoardLine++;
		// Calculate occupied possitions based on previous queen placements
		isOccupiedLeftDiagonal[currentBoardLine] = (isOccupiedLeftDiagonal[currentBoardLine - 1] | selectedbit) << 1;

		isOccupiedRightDiagonal[currentBoardLine] = (isOccupiedRightDiagonal[currentBoardLine - 1] | selectedbit) >> 1;

		isOccupiedHorizontal[currentBoardLine] = (isOccupiedHorizontal[currentBoardLine - 1] | selectedbit);
		
		possiblePlacements[currentBoardLine] = MASK
				& ~(isOccupiedHorizontal[currentBoardLine]
						| isOccupiedRightDiagonal[currentBoardLine] | isOccupiedLeftDiagonal[currentBoardLine]);
		
		
	}

	protected int getLine() {
		return currentBoardLine;
	}

	protected int getBoardLine() {
		return board[getLine()];
	}

	public String toString() {
		StringBuffer bout = new StringBuffer();
		bout.append("--------------\n");
		bout.append("BOARD\n");
		
		for (int i = 0; i <= sizee; i++) {
			bout.append(Integer.numberOfTrailingZeros(board[i]) + "\n");
		}
		bout.append("NEXTPOSSIBLE\n");
		for (int i = 0; i <= sizee; i++) {
			bout.append(Integer.toBinaryString((possiblePlacements[i])) + "\n");
		}
		bout.append("LEFTDIAGONAL\n");
		for (int i = 0; i <= sizee; i++) {
			bout.append(Integer.toBinaryString((isOccupiedLeftDiagonal[i])) + "\n");
		}
		bout.append("--------------\n");
		return bout.toString();
	}

	// Create a new linkedlist every time... Slow?
	public Queue<Board2> iterateLine() {
		Queue<Board2> boards = new LinkedList<Board2>();
		try {
			int selection;
			if (!this.checkBounds())
				return boards;

			int tmp = possiblePlacements[currentBoardLine];
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

	public void suspendBacktrack(boolean b) {
		suspendBacktrack = b;
	}

	public void setCheckpointing(boolean b) {
		this.checkpointing = b;
	}

	public boolean useCheckpointing() {
		return this.checkpointing;
	}
}
