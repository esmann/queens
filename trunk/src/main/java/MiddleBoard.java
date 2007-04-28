import java.util.Collection;
import java.util.LinkedList;


public class MiddleBoard extends Board2 {
	private static int SIDEMASK,TOPBIT;
	private int BOARD1,BOARD2,LASTMASK,ENDBIT;
	private int bound1,bound2;
	public MiddleBoard(int size) {
		super(size);
		TOPBIT = size << size - 1;
		SIDEMASK = TOPBIT | 1;
		LASTMASK = TOPBIT | 1;
		ENDBIT = TOPBIT >> 1;
	}
	@Override
	Collection<Board2> init() {
		Collection<Board2> boards = new LinkedList<Board2>();
		MiddleBoard bnew;
		for(bound1=1, bound2=size-2; bound1<bound2; bound1++, bound2--) {
			try {
				BOARD1 = bound1;
				BOARD2 = bound2;
				bnew = (MiddleBoard) this.clone();
			} catch (CloneNotSupportedException e) {
				// should never happen
				e.printStackTrace();
				throw new InternalError(e.toString());
			}
			bnew.setLine(1 << bound1); // Af few more boards
			
			boards.add(bnew);
			LASTMASK |= LASTMASK >> 1 | LASTMASK << 1;
			ENDBIT >>= 1;			
		}
		return null;
	}
	
	@Override
	boolean isSolution() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	void checkBounds() {
		
		
	}

	

	
	
}
