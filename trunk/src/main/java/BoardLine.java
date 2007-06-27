
public class BoardLine {
    int horisontal;
    int rightDiagonal;
    int leftDiagonal;
    int queenPlacement;
    int possiblePlacements;
    int lineNumber;
    BoardLine prev;
    BoardLine next;
    
    public BoardLine(int horisontal, int rightDiagonal, int leftDiagonal, int lineNumber) {
    	this.horisontal = horisontal;
		this.rightDiagonal = rightDiagonal;
		this.leftDiagonal = leftDiagonal;
		this.prev = null;
		this.lineNumber = lineNumber;
		this.possiblePlacements = Board2.MASK & ~(leftDiagonal | horisontal | rightDiagonal);
        //System.out.println("possible: " + this.possiblePlacements);
        //System.out.println("left      : " + this.leftDiagonal);
        //System.out.println("right     : " + this.rightDiagonal);
        //System.out.println("horisontal: " + this.horisontal);
    	}
    
    public BoardLine(int horisontal, int rightDiagonal, int leftDiagonal, BoardLine prev) {
		this.horisontal = horisontal;
		this.rightDiagonal = rightDiagonal;
		this.leftDiagonal = leftDiagonal;
		this.prev = prev;
		this.lineNumber = prev.lineNumber + 1;
		this.possiblePlacements = Board2.MASK & ~(leftDiagonal | horisontal | rightDiagonal);
        //System.out.println("possible  : " + this.possiblePlacements);
        //System.out.println("left      : " + this.leftDiagonal);
        //System.out.println("right     : " + this.rightDiagonal);
        //System.out.println("horisontal: " + this.horisontal);
		//assert(prev != this);
	}
    
    public BoardLine(BoardLine prev) {
    	this.lineNumber = prev.lineNumber + 1; 
    	this.prev = prev;
    	prev.setnextBoardline(this);
	}

	public boolean hasPossiblePlacements() {
    	return possiblePlacements != 0;
    }
    public void setnextBoardline(BoardLine bl) {
    	this.next  = bl;
    }
    public void printBoard() {
    	
    	if (this.prev != null)
    		prev.printBoard();
    	System.out.println(lineNumber + ": " + Integer.toBinaryString(possiblePlacements));
    	
    }

	public void setBoard(int[] board) {
		BoardLine line = this;
		while (line.prev != null) {
			board[line.lineNumber] = line.possiblePlacements;
			line = line.prev;
		}
		
	}


}
