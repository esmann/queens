
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
	//Evuhl hack to get the applet to accept the objects..
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
	@SuppressWarnings("unused")
	private static void createMRSL(String input) throws IOException  {
		File f = new File("mrsl.TEMPLATE");
		HashMap<String,String> vars = new HashMap<String,String>();
		vars.put("$INPUTFILE",input + ".obj");
		StringBuilder sb = new StringBuilder();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(f));		
		String str;
		
		while ((str = in.readLine()) != null) {			
			sb.append(str + "\n");			
		}		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			in.close();
		}
		String template = sb.toString();
		
		for (Entry<String,String> var: vars.entrySet()) {			
			template = template.replace(var.getKey(), var.getValue());
		}		
		
		PrintWriter out = new PrintWriter( new FileOutputStream(input + ".mrsl"));
		out.write(template);
		out.close();
		
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args)  {
		
		//size = Integer.parseInt(args[0]);
		//maxSteps = Integer.parseInt(args[1]);

		size = 9;
		maxSteps = 1;

		CornerBoard cboard = new CornerBoard(size);
		cboard.setRecursive(true);
		boards.addAll(cboard.init());
		//dout("After INIT #cornerboards=" + boards.size());
		MiddleBoard mboard = new MiddleBoard(size);
		mboard.setRecursive(true);
		boards.addAll(mboard.init());
		//dout("After INIT #middleboards=" + boards.size());
		for (steps = 0; steps < maxSteps; steps++) {
			iterateOnetime();
		}
		

		MiGClient client = new MiGClient();
		client.submitAndExtract(boards, "boards/", "test.zip", 0);
		/*

		String basename = "board";
		FileOutputStream fos;
		ObjectOutputStream out;
		try {
		int count = 0;
		
		
		for (Board2 board : boards) {
			//client.upload(board, "boards/", basename + "-" + size + "-" + maxSteps + "-" + count++);
			//client.upload(board, "boards/", "zomghats");
			System.exit(0);
			String name = basename + "-" + size + "-" + maxSteps + "-" + count++;
			fos = new FileOutputStream(name + ".obj");
			out = new ObjectOutputStream(fos);			
			out.writeObject(board);	
			createMRSL(name);
			fos.close();
		}
		 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();

		}*/
		//int count = 0;
		/*for (Board2 board : boards) {
			//client.upload(board, "test/", "board-blah-" + count++);
			board.setRecursive(false);
			board.backtrack();			
			total += board.getTotal();			
			unique += board.getUnique();
		//	dout("-----");
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
		//System.out.println("After step: " + steps + "\n boards:"+ boards.size());
	}
}