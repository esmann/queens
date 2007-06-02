import java.util.LinkedList;
import java.util.Queue;

public class NQueenBoards {
	static Queue<Board2> boards = new LinkedList<Board2>();

	private static int size;

	private static int total;

	private static int unique;

	private static int steps;

	private static int maxSteps;
	
	private static boolean isRecursive;

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

		size = 17;
		maxSteps = 1;
		isRecursive = true;
		
		if(args.length > 2){
			if(Integer.parseInt(args[0]) > 0){
				size = Integer.parseInt(args[0]);
			}
			
			if(Integer.parseInt(args[1]) >= 0){
				maxSteps = Integer.parseInt(args[1]);
			}
			
			if(args[2] !=  "")
				isRecursive = Boolean.parseBoolean(args[2]);
		}
		
			
		long starttime, boardtime, endtime;
		starttime = System.currentTimeMillis();
		
		CornerBoard cboard = new CornerBoard(size);
		cboard.setRecursive(isRecursive);
		cboard.setCheckpointing(true);
		boards.addAll(cboard.init());
		//dout("After INIT #cornerboards=" + boards.size());
		MiddleBoard mboard = new MiddleBoard(size);
		mboard.setRecursive(isRecursive);
		mboard.setCheckpointing(true);
		boards.addAll(mboard.init());
		//dout("After INIT #middleboards=" + boards.size());
		for (steps = 0; steps < maxSteps; steps++) {
			iterateOnetime();
		}
		
		/*if(args[3] != ""){
			MiGClient client = new MiGClient();
			client.submitAndExtract(boards, "boards/", "test.zip", 0);
		}*/
		boardtime = System.currentTimeMillis();
		//boardtime er den tid det tager at generere, pakke og uploade boards.
		boardtime = boardtime - starttime;
		System.out.println("boardtime: " + boardtime);
		for (Board2 board : boards) {
			board.setRecursive(isRecursive);
			board.backtrack();
			total += board.getTotal();
			unique += board.getUnique();
			// dout("-----");
		}
		endtime = System.currentTimeMillis();
		System.out.println("time: " + (endtime-starttime));
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
