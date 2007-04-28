import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class NQueenBoards {
	static Queue<Board2> boards = new LinkedList<Board2>();
	private static int size;
	private static CornerBoard cboard;
	private static int count8;
	private static int steps;
	private static int maxSteps;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	//	size = Integer.parseInt(args[0]);
	//	maxSteps = Integer.parseInt(args[1]);
		
		
		size = 11;
		maxSteps = 12;
		cboard = new CornerBoard(size);
		boards.addAll(
					cboard.init()
					);
	
	   for(steps = 0; steps < maxSteps;steps++) {
		   iterateOnetime();
	   }
	   /*
	   for (CornerBoard cb : boards) {
		  System.out.println("------");
		  System.out.println(cb);
	   }*/
	   
	}

	public static void iterateOnetime() {		
		int n = boards.size();		
		// Dodgy code
	
		for (int i = 1; i <= n; i++) {
			Board2 board = boards.poll();
			boards.addAll(board.iterateLine());
			//System.out.println("Boards: " + boards.size());
		}
						
		System.out.println("After step: " + steps  +"\n boards:" + boards.size());
		
	}

}
