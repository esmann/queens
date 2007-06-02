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
	//private static final long serialVersionUID = -6693387052709608747L;

	private static final int CHECKPOINT_INTERVAL = 30 * 1000;

	private static Board2 board;
	public boolean checkpoint() {
		return super.checkpoint();
	}
	@Override
	public void MiG_main(String[] argv) {
		if (argv.length > 0) {
			String filename = argv[0];
			File infile = null;
			ObjectInputStream in = null;

			byte[] readbuf;
			int byte_counter = 0;
			int i;
			int bufsize = 1000;

			long starttime, endtime, boardtime;
			// out("starting up :-)");
			try {
				starttime = System.currentTimeMillis();
				infile = this.open_file(argv[0], File.R);
				if (infile.getMode() != File.R) {
					throw new FileException("Could'nt open file for read: "
							+ argv[0]);
				}
				// out("File opened");
				/*
				 * Mig file IO is extremely crappy... this could be a
				 * oneliner.....!
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
				Timer t = new Timer();
				if (board instanceof CornerBoard) 
					System.out.println("CornerBoard wooo !");
										
				
				if (board.useCheckpointing())					
					t.schedule(new CheckPointer(board,new CheckPointActionMiG(this)),
							CHECKPOINT_INTERVAL, CHECKPOINT_INTERVAL);
				
				
				// out("File read");
				// Actual work is done in the board class :-)

				boardtime = System.currentTimeMillis();
				board.backtrack();
				endtime = System.currentTimeMillis();

				if (board.useCheckpointing())
					t.cancel(); 

				boardtime = endtime - boardtime;

				String output = "\n" + filename + "\ntotal: "
						+ board.getTotal() + "\nunique: " + board.getUnique()
						+ "\nboardtime: " + boardtime + "\ntime: "
						+ (endtime - starttime);

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
			} 

		} else {

			System.out.println("\nUsage: NQueenJob serialized-board-filename");
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
