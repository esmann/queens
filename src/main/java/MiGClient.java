import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.ObjectOutputStream;
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
			String filename, int splitsize) {

		PutMethod httpput = new PutMethod("https://mig-1.imada.sdu.dk/"
				+ destination + filename);

		ZipOutputStream out = null;

		try {
			//create zipfile on disk (do we really _have_ to do that?)
			//out = new ZipOutputStream(new FileOutputStream("temp.zip"));

			// ByteArrayOutStream is a stream backed by a bytearray (duh? ;))
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			out = new ZipOutputStream(bout);

			int count = 0;
			for (Board2 board : boards) {
				// add board object to zipfile
				out.putNextEntry(new ZipEntry("board" + count + ".obj"));
				ObjectOutputStream objout = new ObjectOutputStream(out);
				objout.writeObject(board);
				out.closeEntry();

				// create and add mrsl file to zip file
				MigJob job = new MigJob("NQueenJob boards/board" + count
						+ ".obj", "board" + count + ".mrsl");
				out.putNextEntry(new ZipEntry("board" + count + ".mrsl"));

				out.write(job.toString().getBytes());
				out.closeEntry();
				
				count++;
			}

			out.close();

			InputStreamRequestEntity entity = null;

			//entity = new InputStreamRequestEntity(new FileInputStream("temp.zip"));
			entity = new InputStreamRequestEntity(new ByteArrayInputStream(bout
					.toByteArray()));
						
			
			httpput.setRequestEntity(entity);
			httpput.setRequestHeader("Content-Type", "submitandextract");
			httpclient.executeMethod(httpput);
			
			//response = httpput.getResponseBodyAsString();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			//System.out.println(response);
			httpput.releaseConnection();
		}
	}
}
