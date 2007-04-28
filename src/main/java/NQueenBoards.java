import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class NQueenBoards {
	static Queue<Board2> boards = new LinkedList<Board2>();

	private static int size;

	private static int total;
	
	private static int unique;
	
	private static int steps;

	private static int maxSteps;

	/**
	 * @param args
	 */
	public static void dout(String s) {
		System.out.println(s);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// size = Integer.parseInt(args[0]);
		// maxSteps = Integer.parseInt(args[1]);

		size = 6;
		maxSteps = 0;
		//CornerBoard cboard = new CornerBoard(size);		
		//boards.addAll(cboard.init());
		//dout("After INIT #cornerboards=" + boards.size());
		MiddleBoard mboard = new MiddleBoard(size);
		boards.addAll(mboard.init());
		dout("After INIT #middleboards=" + boards.size());
		for (steps = 0; steps < maxSteps; steps++) {
			iterateOnetime();
		}
		
		for (Board2 board : boards) {
		
			dout("Before backtrack\n"  + board.toString());
		
			board.backtrack();
			dout("After backtrack\n"  + board.toString());
			total += board.getTotal();
			
			unique += board.getUnique();
			dout("-----");
		}
		
		System.out.println("\ntotal: " + total);
		System.out.println("\nUnique: " + unique);
		
		
		
		/*
		 * for (CornerBoard cb : boards) { System.out.println("------");
		 * System.out.println(cb); }
		 */

	}

	public static void iterateOnetime() {
		int n = boards.size();
		// Dodgy code

		for (int i = 1; i <= n; i++) {
			Board2 board = boards.poll();
			boards.addAll(board.iterateLine());
			System.out.println("Boards: " + boards.size());
		}

		System.out.println("After step: " + steps + "\n boards:"
				+ boards.size());

	}

}
