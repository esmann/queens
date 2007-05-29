import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
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
		
	
		testHelper(62672, 240, isRecursive);
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
