
public class BoardLine {
    int horisontal;
    int rightDiagonal;
    int leftDiagonal;
    int queenPlacement;
    int possiblePlacements;
    int lineNumber;
    BoardLine parent;
    
    public BoardLine(int horisontal, int rightDiagonal, int leftDiagonal, int lineNumber) {
    	this.horisontal = horisontal;
		this.rightDiagonal = rightDiagonal;
		this.leftDiagonal = leftDiagonal;
		this.parent = null;
		this.lineNumber = lineNumber;
		this.possiblePlacements = Board2.MASK & ~(leftDiagonal | horisontal | rightDiagonal);
        System.out.println(this.possiblePlacements);
	}
    
    public BoardLine(int horisontal, int rightDiagonal, int leftDiagonal, BoardLine parent) {
		this.horisontal = horisontal;
		this.rightDiagonal = rightDiagonal;
		this.leftDiagonal = leftDiagonal;
		this.parent = parent;
		this.lineNumber = parent.lineNumber + 1;
		this.possiblePlacements = Board2.MASK & ~(leftDiagonal | horisontal | rightDiagonal);
        System.out.println(this.possiblePlacements);
		assert(parent != this);
	}
    
    public boolean hasPossiblePlacements() {
    	return possiblePlacements != 0;
    }
    public void printBoard() {
    	/*
    	if (parent != null)
    		parent.printBoard();
    	System.out.println(lineNumber + ": " + Integer.toBinaryString(possiblePlacements));
    	*/
    	
    }
}
