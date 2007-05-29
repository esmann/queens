import static org.junit.Assert.*;


import java.util.LinkedList;
import java.util.Queue;

import org.junit.Test;

public class MiddleboardTest {

	@Test(timeout = 5000)
	public void testMiddleBoard() {

		boolean isRecursive = false;
		testHelper(4, 2, isRecursive);
		testHelper(5, 2, isRecursive);
		testHelper(6, 4, isRecursive);
		testHelper(7, 24, isRecursive);
		testHelper(8, 76, isRecursive);
		testHelper(9, 240, isRecursive);
		
		isRecursive = true;
		testHelper(4, 2, isRecursive);
		testHelper(5, 2, isRecursive);
		testHelper(6, 4, isRecursive);
		testHelper(7, 24, isRecursive);
		testHelper(8, 76, isRecursive);
		testHelper(9, 240, isRecursive);
		testHelper(12, 12200, isRecursive);
	}

	@Test 
	public void testParrallel() {
		MiddleBoard mboard = new MiddleBoard(12);
		mboard.setRecursive(false);
		
		Queue<Board2> boards = new LinkedList<Board2>();
		
		boards.addAll(mboard.init());
		
		int maxSteps = 10; 
		for (int steps = 0; steps < maxSteps; steps++) {
			int n = boards.size();
			for (int i = 1; i <= n; i++) {				
				Board2 board = boards.poll();
				boards.addAll(board.iterateLine());
			}
		}
		int total = 0;
		for (Board2 board: boards) {
			board.backtrack();
			total += board.getTotal();
		}
		
		assertEquals(12200, total);
	}
	
	
	void testHelper(int size, int expectedSolutions, boolean recursive) {
		System.out.println("SIZE: " + size);
		MiddleBoard mboard = new MiddleBoard(size);
		mboard.setRecursive(recursive);

		int total = 0;
		for (Board2 b : mboard.init()) {
			b.backtrack();
			total += b.getTotal();
		}
		assertEquals(expectedSolutions, total);
	}

}
