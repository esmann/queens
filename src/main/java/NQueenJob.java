import MiG.oneclick.FileException;
import MiG.oneclick.Job;
import MiG.oneclick.File;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Timer;

public class NQueenJob extends Job {

	/**
	 * 
	 */
	// private static final long serialVersionUID = -6693387052709608747L;
	private static final int CHECKPOINT_INTERVAL = 5 * 1000;

	private Board2 board;

	public boolean checkpoint() {
		return super.checkpoint();
	}

	private Board2 getBoard(String filename) throws FileException, IOException,
			ClassNotFoundException {
		Board2 board;
		byte[] readbuf;
		int byte_counter = 0;
		int i;
		int bufsize = 1000;

		File infile = null;
		ObjectInputStream in = null;

		infile = this.open_file(filename, File.R);
		if (infile.getMode() != File.R) {
			throw new FileException("Could'nt open file for read: " + filename);
		}
		// out("File opened");
		/*
		 * Mig file IO is extremely crappy... this could be a oneliner.....!
		 */
		readbuf = new byte[bufsize];
		i = infile.read();
		while (i != -1) {
			if (byte_counter == bufsize) {
				byte[] newbuf = new byte[bufsize * 2];
				System.arraycopy(readbuf, 0, newbuf, 0, bufsize);
				readbuf = newbuf;
				bufsize = bufsize * 2;
			}
			readbuf[byte_counter] = (byte) i;
			byte_counter = byte_counter + 1;
			i = infile.read();
		}
		infile.close();
		// Make sure instream is java filestream
		in = new ObjectInputStream(new ByteArrayInputStream(readbuf));
		board = (Board2) in.readObject();
		in.close();
		return board;
	}

	@Override
	public void MiG_main(String[] argv) {

		Timer t = new Timer();

		if (argv.length != 1)
			System.out.println("\nUsage: NQueenJob serialized-board-filename");

		long starttime, endtime, boardtime;
		// out("starting up :-)");
		try {
			starttime = System.currentTimeMillis();
			if (board == null) {
				board = getBoard(argv[0]);
			} else {
				System.out.println("Board loaded from checkpoint");
				System.out.println(board.toString());
			}

			if (board.useCheckpointing())
				t.schedule(new CheckPointer(board,
						new CheckPointActionMiG(this)), CHECKPOINT_INTERVAL,
						CHECKPOINT_INTERVAL);

			// out("File read");
			// Actual work is done in the board class :-)

			boardtime = System.currentTimeMillis();
			board.backtrack();
			endtime = System.currentTimeMillis();

			if (board.useCheckpointing())
				t.cancel();

			boardtime = endtime - boardtime;

			String output = "\n" + argv[0] + "\ntotal: " + board.getTotal()
					+ "\nunique: " + board.getUnique() + "\nboardtime: "
					+ boardtime + "\ntime: " + (endtime - starttime);

			System.out.println(output);
			out(output);
		} catch (IOException ex) {
			ex.printStackTrace();
			err("error: ioexception");
			err(ex.toString());
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			err("error: class not found");
			err(ex.toString());
		} catch (FileException ex) {
			err("error: fileexception");
			err(ex.toString());
		} finally {			
			if (board.useCheckpointing())
				t.cancel();				
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
