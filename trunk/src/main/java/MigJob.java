import java.util.HashMap;
import java.util.Iterator;

/*
 * MigJob represents a mRSL file, or should at least serialize to this
 */
public class MigJob {
	private HashMap<String, String> mrsl = new HashMap<String, String>();

	public MigJob(String execute) {
		mrsl.put("EXECUTE", execute);
		mrsl.put("RUNTIMEENVIRONMENT", "JVM_BYTECODE");
		mrsl.put("SANDBOX", "1");
		mrsl.put("CPUTIME", "100000");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String var : mrsl.keySet()) {
			sb.append("::" + var + "::\n");
			sb.append(mrsl.get(var) + "\n");
			sb.append("\n"); // Must have newline...crappy mrsl format
		}
		return sb.toString();
	}

}
