public class CheckPointActionMiG implements CheckPointAction {
	
	private NQueenJob job;

	public CheckPointActionMiG(NQueenJob job) {
		this.job = job;
	}
	
	public boolean doCheckpoint() {
		return job.checkpoint(); // From Mig.oneclick.Job
	}

}
