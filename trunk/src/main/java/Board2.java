import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Board2 implements Cloneable, Serializable {

	// ONLY USED FOR TESTING CHECKPOINTING DO NOT USE THIS ELSEWHERE!
	protected boolean exitAfterCheckpoint = false;

	protected boolean complete = false;

	protected int size;

	protected long time = 0;

	// Using arrays for the iterative part
	protected int board[], isOccupiedLeftDiagonal[], isOccupiedRightDiagonal[],
			isOccupiedHorizontal[], possiblePlacements[];

	protected int currentBoardLine = 0;

	protected static int MASK;

	abstract int getBound1();

	abstract int getBound2();

	abstract String getType();

	abstract String getLastMask();

	abstract String getEndBit();

	public static void dout(String s) {
		// System.out.println(s);
	}

	protected int sizee = 0;

	protected boolean suspendBacktrack = false;

	private boolean checkpointing = true;
	Algo algorithm = Algo.RECURSIVE; 
	protected int top = -1;

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

	public void setAlgo(Algo a) {
		this.algorithm = a;
	}

	abstract Collection<Board2> init();

	abstract boolean checkBounds();
	
	abstract void backtrackRecursive(int top, int leftDiagonal, int horizontal,
			int rightDiagonal);
	abstract void backtrackDoubleLinkedList();
    abstract void backtrackLinkedList();
	abstract void backtrackIterative();

	abstract void countBacktrack();

	public void backtrack() {

		if (top == -1) {
			top = currentBoardLine;
		} else {
			System.out.println("Board detected resume");
			System.out.println("Has already spent cputime (ms):" + time);
			if (algorithm == Algo.RECURSIVE)
				throw new Error("Can not resume when backtracking recursively");
		}			
		
		switch (algorithm) {
		case DOUBLELINKEDLIST:
			backtrackDoubleLinkedList();
			break;
		case LINKEDLIST:
			backtrackLinkedList();
			break;
		case RECURSIVE:
			backtrackRecursive(top, isOccupiedLeftDiagonal[top],
					isOccupiedHorizontal[top], isOccupiedRightDiagonal[top]);
			break;
		case ARRAY:
			backtrackIterative();
			break;
		default:
			break;
		}

		// System.out.println("Total Backtracking took(ms) :" + this.time);
	}

	
	
	abstract long getUnique();

	abstract long getTotal();
	
	
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
		// System.out.println("Setting line:" + currentBoardLine);
		board[currentBoardLine] = selected;

		currentBoardLine++;

		isOccupiedHorizontal[currentBoardLine] = selected;

		isOccupiedLeftDiagonal[currentBoardLine] = selected << 1;
		isOccupiedRightDiagonal[currentBoardLine] = selected >> 1;

		possiblePlacements[currentBoardLine] = MASK & ~(selected);

	}

	private void setLine(int selectedbit) {
		board[currentBoardLine] = selectedbit;
	}

	protected void setAndIncLine(int selectedbit) {
		setLine(selectedbit);

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
			bout.append(Integer.toBinaryString((isOccupiedLeftDiagonal[i]))
					+ "\n");
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
				// bnew.countBacktrack();
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

	/**
	 * A board could be resumed, if isComplete() == true no further computation
	 * can be done
	 * 
	 */
	public boolean isComplete() {
		return complete;
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
