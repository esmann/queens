import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Queue;
import java.util.zip.*;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;

public class MiGClient {
	private static HttpClient httpclient = null;

	@SuppressWarnings("deprecation")
	private String response;
	MiGClient() {
		try {
			Protocol.registerProtocol("https", new Protocol("https", new MiGSSLSocketFactory(), 443));
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpclient = new HttpClient();
	}

	public static void main(String[] args) throws HttpException, IOException, GeneralSecurityException 
	{
			MiGClient client = new MiGClient();
			//client.listDir("jvm");
			MigJob myjob = new MigJob("NQueenJob boards/zomghats", "zomghats.mrsl");
			client.submitJob(myjob);
	}
	
	public void listDir(String path) {

		GetMethod httpget = new GetMethod("https://mig-1.imada.sdu.dk/cgi-bin/ls.py?flags=a&with_html=false");

		try {
			httpclient.executeMethod(httpget);
			//response = httpget.getResponseBodyAsString();

		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			//System.out.println(httpget.getStatusLine());
			//System.out.println(response);
			httpget.releaseConnection();
		}
	}
	
	public void upload(Serializable object, String destination, String filename)
	{
		upload(object.toString(), destination, filename, "");
	}
	
	public void upload(String content, String destination, String filename, String contenttype) 
	{
		PutMethod httpput = new PutMethod("https://mig-1.imada.sdu.dk/" + destination + filename);
		StringRequestEntity entity = null;
		try {
			entity = new StringRequestEntity(content);
			httpput.setRequestEntity(entity);
			if(contenttype != "")
				httpput.setRequestHeader("Content-Type", contenttype);
			httpclient.executeMethod(httpput);
			//response = httpput.getResponseBodyAsString();
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
			//System.out.println(response);
			httpput.releaseConnection();
		}
	}
	

	public void submitJob(MigJob job) 
	{
		upload(job.toString(), "", job.getFilename(), "submitmrsl");

	}
	
	/*
	 * @param boards The list of boards to be zipped
	 * @param destination The destination directory
	 * @param filename The filename of the zipfile
	 * @param splitsize The number of max boards/jobs to put in each zip file (0=put all in 1 zip file) (not implemented yet)
	 */
	public void submitAndExtract(Queue<Board2> boards, String destination, String filename, int splitsize)
	{
		int noBoards = boards.size();
		int count = 0;
		int zipcount = 0;
		ZipOutputStream out = null;
		//PipedInputStream pin = null;
		//PipedOutputStream pout = null;
		String temp;
		byte[] buff;
		try {
			//create zipfile on disk (do we really _have_ to do that?)
			//out = new ZipOutputStream(new FileOutputStream(filename));
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			out = new ZipOutputStream(bout);
			
			//pipedout/input stream virker ikke
			//iflg api'en er det ikke hensigtsmæssigt at have både input og outputstream i samme tråd, da dette kan skabe deadlocks
			//hvilket det gør.. 
		    //pout = new PipedOutputStream();
		    //pin = new PipedInputStream(pout);
		    //out = new ZipOutputStream(pout);
			for (Board2 board : boards) 
			{
				try {
					
					//add board object to zipfile
					out.putNextEntry(new ZipEntry("board" + count + ".obj"));
					ObjectOutputStream objout = new ObjectOutputStream(out);
					objout.writeObject(board);
					//temp = board.toString();
					//buff = temp.getBytes();
					//out.write(buff);
					out.closeEntry();
					//create and add mrsl file to zip file
					MigJob job = new MigJob("NQueenJob boards/board" + count + ".obj", "board" + count + ".mrsl");
					out.putNextEntry(new ZipEntry("board" + count + ".mrsl"));
					temp = job.toString();
					buff = temp.getBytes();
					out.write(buff);
					out.closeEntry();
					count++;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			out.close();
			PutMethod httpput = new PutMethod("https://mig-1.imada.sdu.dk/" + destination + filename);
			InputStreamRequestEntity entity = null;
			try {
				entity = new InputStreamRequestEntity(new ByteArrayInputStream(bout.toByteArray()));
				httpput.setRequestEntity(entity);
				httpput.setRequestHeader("Content-Type", "submitandextract");
				httpclient.executeMethod(httpput);
				//response = httpput.getResponseBodyAsString();
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
				//System.out.println(response);
				httpput.releaseConnection();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
