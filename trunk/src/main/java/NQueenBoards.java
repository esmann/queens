import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import java.io.PrintWriter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Map.Entry;

public class NQueenBoards {
	static Queue<Board2> boards = new LinkedList<Board2>();

	private static int size;

	private static int total;

	private static int unique;

	private static int steps;

	private static int maxSteps;

	// Evuhl hack to get the applet to accept the objects..
	/**
	 * 
	 */
	static final long serialVersionUID = 71874518354897178L;

	/**
	 * @param args
	 */
	public static void dout(String s) {
		System.out.println(s);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// size = Integer.parseInt(args[0]);
		// maxSteps = Integer.parseInt(args[1]);

		size = 18;
		maxSteps = 0;

		
		CornerBoard cboard = new CornerBoard(size);
		cboard.setRecursive(false);
		cboard.setCheckpointing(true);
		boards.addAll(cboard.init());
		//dout("After INIT #cornerboards=" + boards.size());
		MiddleBoard mboard = new MiddleBoard(size);
		mboard.setCheckpointing(true);
		mboard.setRecursive(false);
		boards.addAll(mboard.init());
		//dout("After INIT #middleboards=" + boards.size());
		for (steps = 0; steps < maxSteps; steps++) {
			iterateOnetime();
		}

		MiGClient client = new MiGClient();
		client.submitAndExtract(boards, "boards/", "test.zip", 0);
		/*for (Board2 board : boards) {
			// client.upload(board, "test/", "board-blah-" + count++);
			board.setRecursive(false);
			board.backtrack();
			total += board.getTotal();
			unique += board.getUnique();
			// dout("-----");
		}*/

		System.out.println("total: " + total);
		System.out.println("Unique: " + unique);

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
		}
	}
}
