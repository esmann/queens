import java.util.TimerTask;

import MiG.oneclick.Job;

/**
 * 
 * @author frej Checkpointer suspends backtracking for one board while doing the
 *         checkpoint.
 * 
 * This only handles one concurrent board.
 * 
 */

public class CheckPointer extends TimerTask {
	private Board2 board;
	private CheckPointAction action;

	/**
	 * 
	 * @param board
	 *            the board to suspend while doing checkpoint
	 * @param action
	 *            The specified action to run while checkpoint
	 * @param interval
	 *            time to wait for each checkpoint, in seconds
	 */
	CheckPointer(Board2 board, CheckPointAction action) {
		this.board = board;
		this.action = action;
	}

	public void run() {
		boolean result;
		System.out.println("CHECKPOINT TASK STARTED");
		board.suspendBacktrack(true);
		synchronized (board) { // We don't want backtrack to corrup our snapshot
			System.out.println("DOING CHECKPOINT");
			result = action.doCheckpoint();
			System.out.println("Checkpointing finished with: "
					+ (result ? "sucess" : "failure"));
			board.suspendBacktrack(false);
			board.notify();
		}
	}
}
