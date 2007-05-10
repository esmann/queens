
import java.awt.print.Printable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

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
		//System.out.println(s);
	}
	private static void createMRSL(String input) throws IOException  {
		File f = new File("mrsl.TEMPLATE");
		HashMap<String,String> vars = new HashMap<String,String>();
		vars.put("$INPUTFILE",input);
		StringBuilder sb = new StringBuilder();
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(f));
		
		String str;
		
		while ((str = in.readLine()) != null) {			
			sb.append(str + "\n");			
		}		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String template = sb.toString();
		
		for (String var: vars.keySet()) {			
			template = template.replace(var, vars.get(var));
		}		
		
		PrintWriter out = new PrintWriter( new FileOutputStream(input + ".mrsl"));
		out.write(template);
		out.close();
		
		
	}
	public static void main(String[] args)  {
		
		//size = Integer.parseInt(args[0]);
		//maxSteps = Integer.parseInt(args[1]);

		size = 5;
		maxSteps = 0;
		CornerBoard cboard = new CornerBoard(size);		
		boards.addAll(cboard.init());
		dout("After INIT #cornerboards=" + boards.size());
		MiddleBoard mboard = new MiddleBoard(size);
		boards.addAll(mboard.init());
		dout("After INIT #middleboards=" + boards.size());
		for (steps = 0; steps < maxSteps; steps++) {
			iterateOnetime();
		}
		
		
		String basename = "board";
		FileOutputStream fos;
		ObjectOutputStream out;
		try {
		int count = 0;
		for (Board2 board : boards) {			
			String name = basename + count++;
			fos = new FileOutputStream(name);
			out = new ObjectOutputStream(fos);			
			out.writeObject(board);	
			createMRSL(name);		
		}
		 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		/*
		for (Board2 board : boards) {	
			board.backtrack();			
			total += board.getTotal();			
			unique += board.getUnique();
			dout("-----");
		}*
		
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
		}

		System.out.println("After step: " + steps + "\n boards:"
				+ boards.size());

	}

}
