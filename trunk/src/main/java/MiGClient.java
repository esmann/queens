import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;

public class MiGClient {
	private static HttpClient httpclient;
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

		
		GetMethod httpget = new GetMethod(
		// "https://mig-1.imada.sdu.dk/cgi-sid/oneclick/envtest.py");
				"https://mig-1.imada.sdu.dk/cgi-bin/ls.py?flags=a");
		try {
			httpclient.executeMethod(httpget);
			System.out.println(httpget.getStatusLine());
		} finally {
			httpget.releaseConnection();
		}
	}
	public void listDir(String path) {
		
		GetMethod httpget = new GetMethod(
		"https://mig-1.imada.sdu.dk/cgi-bin/ls.py?flags=a&with_html=false");

		try {
				
			httpclient.executeMethod(httpget);
			
		} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();		
		} finally {
			System.out.println(httpget.getStatusLine());
			httpget.releaseConnection();
		}
		}
	
	public void upload(Serializable object) {
		
	}
	public static void submitJob(MigJob job) {

	}
}
