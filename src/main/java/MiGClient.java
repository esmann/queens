import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Queue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;

public class MiGClient {
	private static HttpClient httpclient = null;

	@SuppressWarnings("deprecation")
	private String response;

	MiGClient() {
		try {
			Protocol.registerProtocol("https", new Protocol("https",
					new MiGSSLSocketFactory(), 443));
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpclient = new HttpClient();
	}

	public static void main(String[] args) throws HttpException, IOException,
			GeneralSecurityException {
		MiGClient client = new MiGClient();
		// client.listDir("jvm");
		MigJob myjob = new MigJob("NQueenJob boards/zomghats", "zomghats.mrsl");
		client.submitJob(myjob);
	}

	public void listDir(String path) {

		GetMethod httpget = new GetMethod(
				"https://mig-1.imada.sdu.dk/cgi-bin/ls.py?flags=a&with_html=false");

		try {
			httpclient.executeMethod(httpget);
			// response = httpget.getResponseBodyAsString();

		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// System.out.println(httpget.getStatusLine());
			// System.out.println(response);
			httpget.releaseConnection();
		}
	}

	public void upload(Serializable object, String destination, String filename) {
		upload(object.toString(), destination, filename, "");
	}

	public void upload(String content, String destination, String filename,
			String contenttype) {
		PutMethod httpput = new PutMethod("https://mig-1.imada.sdu.dk/"
				+ destination + filename);
		StringRequestEntity entity = null;
		try {
			entity = new StringRequestEntity(content);
			httpput.setRequestEntity(entity);
			if (contenttype != "")
				httpput.setRequestHeader("Content-Type", contenttype);
			httpclient.executeMethod(httpput);
			// response = httpput.getResponseBodyAsString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// System.out.println(response);
			httpput.releaseConnection();
		}
	}

	public void submitJob(MigJob job) {
		upload(job.toString(), "", job.getFilename(), "submitmrsl");

	}

	/*
	 * @param boards The list of boards to be zipped @param destination The
	 * destination directory @param filename The filename of the zipfile @param
	 * splitsize The number of max boards/jobs to put in each zip file (0=put
	 * all in 1 zip file) (not implemented yet)
	 */
	public void submitAndExtract(Queue<Board2> boards, String destination,
			String filename, int maxsteps, String rec) {

		PutMethod httpput = new PutMethod("https://mig-1.imada.sdu.dk/"
				+ destination + filename);

		ZipOutputStream out = null;
		ObjectOutputStream objout = null;
		try {
			// create zipfile on disk (do we really _have_ to do that?)
			

			// ByteArrayOutStream is a stream backed by a bytearray (duh? ;))
			// ByteArrayOutputStream bout = new ByteArrayOutputStream();
			//out = new ZipOutputStream(bout);
			
			
			int count = 0;
			int filecount = 0;
			for (Board2 board : boards) {				
				if ((count %  1000) == 0) {
					filecount++;
					if (count != 0) {
						out.close();
					}					
					out = new ZipOutputStream(new FileOutputStream("NQ-" + board.size +"-" + maxsteps + "-" + filecount +  ".zip" ));
				
				}
				
				// add board object to zipfile
				out.putNextEntry(new ZipEntry("board" + board.size + "-" + maxsteps + "-" + rec + "-"
						+ count + ".obj"));
				objout = new ObjectOutputStream(out);				
				objout.writeObject(board);
				
				out.closeEntry();
				// create and add mrsl file to zip file
				MigJob job = new MigJob("NQueenJob boards/board" + board.size
						+ "-" + maxsteps + "-" + rec + "-" + count + ".obj", "board" + count + ".mrsl");
				out.putNextEntry(new ZipEntry("board" + board.size + "-"
						+ count + ".mrsl"));

				out.write(job.toString().getBytes());
				out.closeEntry();
				count++;
			
				
			}			
			out.close();
/*
			InputStreamRequestEntity entity = null;

			// entity = new InputStreamRequestEntity(new
			// FileInputStream("temp.zip"));
			entity = new InputStreamRequestEntity(new ByteArrayInputStream(bout
					.toByteArray()));

			httpput.setRequestEntity(entity);
			httpput.setRequestHeader("Content-Type", "submitandextract");
			httpclient.executeMethod(httpput);
			response = httpput.getResponseBodyAsString();
			FileOutputStream jobidout;

			jobidout = new FileOutputStream("test-"
					+ System.currentTimeMillis());
			String[] output = response.split("\n");
			for (String line : output) {
				if (line.contains("assigned")) {
					jobidout.write(line.split(" ")[0].getBytes());
					jobidout.write("\n".getBytes());
					// System.out.println(line.split(" ")[0]);

				}
			}
			*/

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// her skal outputtet parses, og jobids stoppes i en text fil.. til
			// senere brug.. der..

			// System.out.println(output[0]);
			// System.out.println(response);
			httpput.releaseConnection();
		}
		
	}
}
