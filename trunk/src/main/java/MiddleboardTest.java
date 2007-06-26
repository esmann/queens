import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Test;
public class MiddleboardTest {
	// format is size,total solutions,count8,count4,count2 { 4, 2, 0, 0, 1 },
	static int[][] solutions = { { 5, 2, 0, 0, 1 }, { 6, 4, 0, 1, 0 },
			{ 7, 24, 2, 2, 0 }, { 8, 76, 9, 1, 0 }, { 9, 240, 28, 4, 0 },
			{ 12, 12200, 1515, 18, 4 } };

	@Test(timeout = 10000)
	public void testMiddleBoard() {
		
		for (Board2.Algo a : Board2.Algo.values()) {
			for (int[] solution : solutions)
				testHelper(a, solution);	
		}
		

	}

	// @Test
	public void testParrallel() {
		System.out.println("Parrallel test");
		MiddleBoard mboard = new MiddleBoard(12);
		mboard.setAlgo(Board2.Algo.LINKEDLIST);
		int maxSteps = 9;

		Queue<Board2> boards = new LinkedList<Board2>();

		boards.addAll(mboard.init());
		for (int steps = 0; steps < maxSteps; steps++) {
			System.out.println("Step nr: " + steps);
			int n = boards.size();
			for (int i = 1; i <= n; i++) {
				Board2 board = boards.poll();
				boards.addAll(board.iterateLine());
			}

			int total = 0, count2 = 0, count4 = 0, count8 = 0;
			int i = 0;
			// Do a backtrack for each step
			for (Board2 board : boards) {
				board.setAlgo(Board2.Algo.ARRAY);
				System.out.println("Board nr: " + i++ + " is backtracking:");
				board.backtrack();
				/*total += board.getTotal();
				 count2 += ((MiddleBoard) board).getCount2();
				 count4 += ((MiddleBoard) board).getCount4();
				 count8 += ((MiddleBoard) board).getCount8();*/
			}
			assertEquals(1515, count8);
			assertEquals(18, count4);
			assertEquals(4, count2);
			assertEquals(12200, total);
		}
	}

	void testHelper(Board2.Algo a, int... args) {
		if (args.length != 5)
			fail("Must get five ints");

		int size = args[0];
		long expectedSolutions = args[1];
		long expectedCount8 = args[2];
		long expectedCount4 = args[3];
		long expectedCount2 = args[4];

		System.out.println("Testing SIZE,EXPECTS,RECURSIVE: " + size + ","
				+ expectedSolutions + ", " + a);

		MiddleBoard mboard = new MiddleBoard(size);
		mboard.setAlgo(a);

		long total = 0, count2 = 0, count4 = 0, count8 = 0;

		for (Board2 b : mboard.init()) {
			b.backtrack();

			total = total +(b.getTotal());
			count2 = count2 +(((MiddleBoard) b).getCount2());
			count4 = count4 + (((MiddleBoard) b).getCount4());
			count8 = count8 +(((MiddleBoard) b).getCount8());

		}
		assertTrue(expectedCount8 == count8);
		assertTrue(expectedCount4 == count4);
		assertTrue(expectedCount2 == count2);
		assertTrue(expectedSolutions == total);
		System.out.println("DONE");
	}
}
