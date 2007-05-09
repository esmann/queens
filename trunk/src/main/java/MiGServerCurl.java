import java.io.*;

public class MiGServerCurl {

	private String ServerUrl, CaCertFile, CertFile, KeyFile, Password;
	
	public MiGServerCurl(String ServerUrl, String CaCertFileName, String CertFileName, String KeyFileName, String Password)
	{
		this.ServerUrl = ServerUrl;
		this.CaCertFile = " --cacert " + CaCertFileName + " ";
		this.CertFile = " --cert " + CertFileName + " ";
		this.KeyFile = " --key " + KeyFileName + " ";
		this.Password = " --pass " + Password + " ";
		
	}
	
	public void MiGTouch(String filename)
	{
		String command = "/usr/bin/curl --fail " + this.CertFile + this.KeyFile + this.CaCertFile + this.Password + " --url " + this.ServerUrl + "/cgi-bin/touch.py?path=" + filename + ";with_html=false";
		System.out.println(command);
		try
		{
			Process pTouch = java.lang.Runtime.getRuntime().exec(command);
			BufferedReader br = new BufferedReader(new InputStreamReader(pTouch.getInputStream()));
			String line;
			while((line = br.readLine()) != null)
			{
				System.out.println("output: " + line);
			}
		}
		catch(Exception e)
		{
			System.out.println("Error running curl: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void MiGLs(String path)
	{
		String command = "/usr/bin/curl --compressed --fail --silent " + this.CertFile + this.KeyFile + this.CaCertFile + this.Password + " --url " + this.ServerUrl + "/cgi-bin/ls.py?path=" + path + ";with_html=false";
		System.out.println(command);
		try
		{
			Process pTouch = java.lang.Runtime.getRuntime().exec(command);
			BufferedReader br = new BufferedReader(new InputStreamReader(pTouch.getInputStream()));
			String line;
			while((line = br.readLine()) != null)
			{
				System.out.println("output: " + line);
			}
		}
		catch(Exception e)
		{
			System.out.println("Error running curl: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		String path = "/home/alex/Alex_Esmann-certs/";
		String serverurl = "https://mig-1.imada.sdu.dk";
		String cacertfilename = path + "cacert.pem";
		String certfilename = path + "cert.pem";
		String keyfilename = path + "key.pem";
		String password = "flodhest";
		
		MiGServerCurl mig = new MiGServerCurl(serverurl, cacertfilename, certfilename, keyfilename, password);
		//mig.MiGTouch("touch_test");
		mig.MiGLs(".");
	}
}
