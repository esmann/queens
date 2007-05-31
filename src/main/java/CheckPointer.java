import MiG.oneclick.Job;

/**
 * 
 * @author frej Checkpointer suspends backtracking for one board while doing the
 *         checkpoint.
 * 
 * This only handles one concurrent board.
 * 
 */

public class CheckPointer implements Runnable {
	private Board2 board;

	private boolean alive;

	private CheckPointAction action;

	private long checkpoint_interval;

	/**
	 * 
	 * @param board
	 *            the board to suspend while doing checkpoint
	 * @param action
	 *            The specified action to run while checkpoint
	 * @param interval
	 *            time to wait for each checkpoint, in seconds
	 */
	CheckPointer(Board2 board, CheckPointAction action, int interval) {
		this.board = board;
		this.action = action;
		this.checkpoint_interval = interval * 1000;
		this.alive = true;
	}

	public void stop() {
		alive = false;
	}

	public void run() {
		try {
			boolean result;
			System.out.println("CHECKPOINTTHREAD STARTED");
			while (alive) {												
				synchronized (board) {
					System.out.println("PREPARE CHECKPOINT");
					result = action.checkpoint();
					System.out.println("Checkpointing finished with: "
							+ (result ? "sucess" : "failure"));
					board.suspendBacktrack(false);
					board.notify();
					System.out.println("CHECKPOINT SLEEPS");				
					board.wait(checkpoint_interval);
					System.out.println("CHECKPOINT WOKEN");					
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
