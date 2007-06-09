import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Timer;

import org.junit.Test;

public class CheckPointTest implements Serializable {
	private static final int CHECKPOINT_INTERVAL = 10 * 1000;

	class CheckPointActionMock implements CheckPointAction {
		public CheckPointActionMock() {}
		public boolean doCheckpoint() {
			System.out.println("Doing fake local Checkpoint to disk");
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;

		}

	}

	@Test
	public void testCheckpoint() throws FileNotFoundException, IOException, ClassNotFoundException {

		testHelper(16, 13027600, 1628075, 734, 32);

	}

	class MiddleBoardMock extends MiddleBoard implements Serializable {

		private static final long serialVersionUID = -6306621746790068955L;

		public MiddleBoardMock(int size) {
			super(size);
		}

		void enableExitAfterCheckPoint() {
			exitAfterCheckpoint = true;
		}

	}

	void testHelper(int... args) throws FileNotFoundException, IOException, ClassNotFoundException {
		if (args.length != 5)
			fail("Must get five ints");

		int size = args[0];
		int expectedSolutions = args[1];
		int expectedCount8 = args[2];
		int expectedCount4 = args[3];
		int expectedCount2 = args[4];

		System.out.println("Testing SIZE,EXPECTS,RECURSIVE: " + size + ","
				+ expectedSolutions);

		MiddleBoardMock mboard = new MiddleBoardMock(size);
		mboard.setRecursive(false);
		mboard.enableExitAfterCheckPoint(); // TESTING PURPOSES ONLY

		BigInteger total = BigInteger.ZERO, count2 = BigInteger.ZERO, count4 = BigInteger.ZERO, count8 = BigInteger.ZERO;
		int i = 0;
		for (Board2 b : mboard.init()) {
			while (!b.isComplete()) {
				Timer t = new Timer("Boards timer nr: " + i++);
				
				t.schedule(new CheckPointer(b,new CheckPointActionMock()),
						CHECKPOINT_INTERVAL, CHECKPOINT_INTERVAL);
				
				b.backtrack();			
				t.cancel();
				
				boolean result = true;
				// Write the object....
				ObjectOutputStream oos;
				oos = new ObjectOutputStream(new FileOutputStream(
						"checkpointed-board.ser"));
				oos.writeObject((MiddleBoardMock)b);
				oos.close();
				
//				read it back.. duh :)
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream("checkpointed-board.ser"));				
				b = (MiddleBoardMock) ois.readObject();
				ois.close();
			
			}

			total = total.add(b.getTotal());
			count2 = count2.add(((MiddleBoard) b).getCount2());
			count4 = count4.add(((MiddleBoard) b).getCount4());
			count8 = count8.add(((MiddleBoard) b).getCount8());
		}
		assertEquals(expectedCount8, count8);
		assertEquals(expectedCount4, count4);
		assertEquals(expectedCount2, count2);
		assertEquals(expectedSolutions, total);
		System.out.println("DONE");
	}
}
