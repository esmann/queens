import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;


public abstract class Board2 {
	

	protected int Board[];

	private int leftDiagonal, rightDiagonal, horizontal = 0;


	protected int currentLine = 0; // zero indexed nxn board
	private static int MASK;
	

	protected final int size;
	
	protected int nextPossible;
	
	public Board2(int size) {
		this.size = size;
		Board = new int[size];
		MASK = (1 << size) - 1;
	}
	
	abstract boolean isSolution();
	abstract Collection<Board2> init();
	abstract void checkBounds();
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		CornerBoard cb =  (CornerBoard) super.clone();
		cb.Board = new int[this.size];
		System.arraycopy(this.Board, 0, cb.Board, 0, this.size);
		return (Object) cb;
	}
	
	
	protected void setLine(int selectedbit) {
		Board[currentLine] = selectedbit;
		
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
		
		for (int i = 0; i <= currentLine; i++) {
			bout.append(Integer.toBinaryString(Board[i]) + "\n");
		}
		return bout.toString();
	}
	
	
	// Create a new linkedlist every time... Slow?
	public Queue<Board2> iterateLine() {
		Queue<Board2> boards = new LinkedList<Board2>();
		try {			
			int selection;
			int tmp = nextPossible;
			while ((selection = Integer.lowestOneBit(tmp)) != 0) {
				tmp ^= selection; // Set the selected bit to 0 in the original
									// pattern
				Board2 bnew =  (Board2) this.clone();
				bnew.setLine(selection);
				bnew.checkBounds();
				if (bnew.isSolution()) {
					// FIXME
				} else {
					boards.add(bnew);
				}
			}
		} catch (CloneNotSupportedException e) {
			//foooooooo does not happen....
			e.printStackTrace();
		}
		return boards;
	}
}
