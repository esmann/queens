import MiG.oneclick.FileException;
import MiG.oneclick.Job;
import MiG.oneclick.File;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

public class NQueenJob extends Job {

	@Override
	public void MiG_main(String[] argv) {
		System.out.println("test");
		if(argv.length > 0) {
        	String filename = argv[0];
            Board2 board = null;
            File infile = null;
            ObjectInputStream in = null;
            FileOutputStream fos = null;
            byte[] readbuf;
            int byte_counter = 0;
            int i;
            int bufsize = 1000;
			long starttime, endtime, boardtime;
			//out("starting up :-)");
            try {
            	starttime = System.currentTimeMillis();
               	infile = this.open_file(argv[0], File.R);
               	if (infile.getMode() != File.R) {
               		throw new FileException("Could'nt open file for read: " + argv[0]);
               	}
               	//out("File opened");
               	readbuf = new byte[bufsize];
               	i=infile.read();
                while( i!= -1 ) {
                	if(byte_counter == bufsize) {
                		byte[] newbuf = new byte[bufsize*2];
                		System.arraycopy(readbuf, 0, newbuf, 0, bufsize);
                		readbuf = newbuf;
                		bufsize = bufsize*2;
                	}
                	readbuf[byte_counter] = (byte)i;
                	byte_counter = byte_counter + 1;
                	i=infile.read();
                }
                infile.close();
               	// Make sure instream is java filestream
                in = new ObjectInputStream(new ByteArrayInputStream(readbuf));
            	board = (Board2)in.readObject();
            	in.close();
            	//out("File read");
            	// Actual work is done in the board class :-)
                boardtime = System.currentTimeMillis();
            	board.backtrack();
                endtime = System.currentTimeMillis();
                boardtime = endtime - boardtime;
                String total = Integer.toString(board.getTotal());
                String unique = Integer.toString(board.getUnique());
                String output = "\n" + filename + "\ntotal: " + total + "\nunique: " + unique;
                System.out.println(output);
                out(output);
  /*              fos = new FileOutputStream(filename + "_result");
                fos.write(total.getBytes());
                fos.write('\n');
                fos.write(unique.getBytes());
                fos.write('\n');
                fos.write((int)boardtime);
                fos.write('\n');
                fos.write((int)(endtime - starttime));
                fos.close();
    */                        
            } catch(IOException ex) {
            	ex.printStackTrace();
            } catch(ClassNotFoundException ex) {
            	ex.printStackTrace();
            } catch(FileException ex) {
            	out(ex.toString());
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
