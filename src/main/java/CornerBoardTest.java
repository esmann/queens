import static org.junit.Assert.*;

import org.junit.Test;

public class CornerBoardTest {

	static void dout(String string) {
		System.out.println(Thread.currentThread().getId() + ": " + string);

	}

	private static final int CHECKPOINT_INTERVAL = 15*1000;

	private class CheckPointThreadTest extends Thread {

		private Board2 board;

		CheckPointThreadTest(Board2 b) {
			this.board = b;
		}

		public void run() {
			try {
				synchronized (board) {
					int count = 0;
					dout("CHECKPOINT STARTS");
					while (this.isAlive()) {
						dout("CHECKPOINT SLEEPS");
						board.wait(CHECKPOINT_INTERVAL);
						board.doCheckpoint(true);
						dout("DOING CHECKPOINT nr: " + ++count);
						Thread.sleep(5000);
						board.doCheckpoint(false);						
						dout("DONE CHECKPOINT nr: " + count);
						
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	// @Test(timeout = 5000)
	@Test
	public void testIterative() {

		testHelper(18, 1744912, false);

		// testHelper(6,0,false);
		// testHelper(5,8,false);
		// testHelper(7,2*8,false);
		// testHelper(8,2*8,false);
		// testHelper(9,14*8,false); // Up from 2 to 14 unique!
		//				
		// testHelper(6,0,true);
		// testHelper(5,8,true);
		// testHelper(7,2*8,true);
		// testHelper(8,2*8,true);
		// testHelper(9,14*8,true); // Up from 2 to 14 unique!
	}

	void testHelper(int size, int expectedSolutions, boolean recursive) {
		System.out.println("SIZE: " + size);
		CornerBoard cboard = new CornerBoard(size);
		cboard.setRecursive(recursive);

		int total = 0;
		for (Board2 b : cboard.init()) {
			(new CheckPointThreadTest(b)).start();
			b.backtrack();
			total += b.getTotal();
			break;
		}
		assertEquals(expectedSolutions, total);
	}

}
