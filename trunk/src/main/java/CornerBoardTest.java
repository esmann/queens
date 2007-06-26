import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Timer;

import org.junit.Test;

public class CornerBoardTest {

	static void dout(String string) {
		System.out.println(Thread.currentThread().getId() + ": " + string);

	}

	private static final int CHECKPOINT_INTERVAL = 100 * 1000;

	class CheckPointActionMock implements CheckPointAction {

		public boolean doCheckpoint() {
			System.out.println("Doing fake Checkpoint");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}

	}

	// @Test(timeout = 5000)
	@Test
	public void testIterative() {

		//testHelper(18, 1744912, false);
		for (Board2.Algo a : Board2.Algo.values()) {
			testHelper(6, 0,a);
			testHelper(5, 8,a);
			//testHelper(7, 2 * 8,a);
			//testHelper(8, 2 * 8,a);
			testHelper(9, 14 * 8,a); // Up from 2 to 14 unique!
		
		}
	}


	void testHelper(int size, int expectedSolutions, Board2.Algo a) {
		System.out.println("SIZE: " + size);
		CornerBoard cboard = new CornerBoard(size);
		cboard.setAlgo(a);

		long total = 0;
		for (Board2 b : cboard.init()) {

			/*
			 * t.schedule(new CheckPointer(b,new CheckPointActionMock()),
			 * CHECKPOINT_INTERVAL, CHECKPOINT_INTERVAL);
			 */
			b.backtrack();
			// t.cancel(); // Doesn't kill currently running task...
			total += b.getTotal();

		}
		assertNotNull(total);
		
		assertNotNull(expectedSolutions);
		
		assertTrue("total: " + total ,(expectedSolutions == total));
	}
}

