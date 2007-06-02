import static org.junit.Assert.*;


import java.util.LinkedList;
import java.util.Queue;

import org.junit.Test;

public class MiddleboardTest {

	@Test(timeout = 5000)
	public void testMiddleBoard() {

		boolean isRecursive = false;
		testHelper(4, 2, isRecursive,0,0,1);
		testHelper(5, 2, isRecursive,0,0,1);
		testHelper(6, 4, isRecursive,0,1,0);
		testHelper(7, 24, isRecursive,2,2,0);
		testHelper(8, 76, isRecursive,9,1,0);
		testHelper(9, 240, isRecursive,28,4,0);

		isRecursive = true;
		testHelper(4, 2, isRecursive,0,0,1);
		testHelper(5, 2, isRecursive,0,0,1);
		testHelper(6, 4, isRecursive,0,1,0);
		testHelper(7, 24, isRecursive,2,2,0);
		testHelper(8, 76, isRecursive,9,1,0);
		testHelper(9, 240, isRecursive,28,4,0);
	}

	@Test
	public void testParrallel() {
		MiddleBoard mboard = new MiddleBoard(12);
		mboard.setRecursive(false);

		Queue<Board2> boards = new LinkedList<Board2>();

		boards.addAll(mboard.init());

		int maxSteps = 6;
		for (int steps = 0; steps < maxSteps; steps++) {
			int n = boards.size();
			for (int i = 1; i <= n; i++) {
				Board2 board = boards.poll();
				boards.addAll(board.iterateLine());
			}
		}
		int total = 0, count2 = 0, count4 = 0, count8 = 0;
		for (Board2 board : boards) {
			board.setRecursive(true);
			board.backtrack();
			total += board.getTotal();			
			count2 += ((MiddleBoard)board).getCount2();
			count4 += ((MiddleBoard)board).getCount4();
			count8 += ((MiddleBoard)board).getCount8();
		}
		assertEquals(1515,count8);
		assertEquals(18,count4);
		assertEquals(4,count2);
		assertEquals(12200, total);
	}

	void testHelper(int size, int expectedSolutions, boolean recursive,int expectedCount8, int expectedCount4,int expectedCount2) {
		System.out.println("Testing SIZE,EXPECTS: " + size + "," + expectedSolutions);
		
		MiddleBoard mboard = new MiddleBoard(size);
		mboard.setRecursive(recursive);

		int total = 0, count2 = 0, count4 = 0, count8 = 0;
		for (Board2 b : mboard.init()) {
			b.backtrack();
			total += b.getTotal();
			count2 += ((MiddleBoard)b).getCount2();
			count4 += ((MiddleBoard)b).getCount4();
			count8 += ((MiddleBoard)b).getCount8();
		}
		assertEquals(expectedCount8, count8);
		assertEquals(expectedCount4, count4);
		assertEquals(expectedCount2, count2);
		assertEquals(expectedSolutions, total);
	}

}
