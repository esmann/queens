package MiG.oneclick;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.io.ObjectInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import javax.net.ssl.HttpsURLConnection;

public class Checkjob implements Runnable
{
   private final int SLEEPTIME_FACTOR = 1000;
   
   private boolean alive;
   private boolean job_active;
   private String server;
   private String iosessionid;
   private long sleep_period;
   
   public Checkjob(String server, String iosessionid)
     {
	this.alive = true;
	this.job_active = true;
	this.server = server;
	this.iosessionid = iosessionid;
	this.sleep_period = 60;
     }
   
   public boolean jobActive()
     {
	return this.job_active;
     }
   
      
   // We have been stopped
   public void stop()
     {
	this.alive = false;
     }
   
   // We have been destroyed
   public void destroy()
     {
	this.alive = false;
     }
   
   // Retrieves the files needed and executes the vmplayer 
   public void run()
     {
	int checkjob_rc;
	String checkjob_url;
	String value;
	HttpsConnection checkjob_conn;
	String readline;
	
	try
	  {
	     checkjob_url = this.server + "/cgi-sid/isjobactive.py?iosessionid=" + this.iosessionid;
	     System.out.println("Job check URL:" + checkjob_url);
	     
	     while (this.alive && this.job_active)
	       {
		  // Request jobstatus
		  checkjob_conn = new HttpsConnection(checkjob_url);
		  checkjob_conn.open();
	     
		  // Check if request went ok
		  if (checkjob_conn.getResponseCode() == HttpsConnection.HTTP_OK )
		    {
		       readline = checkjob_conn.readLine();
		       //System.out.println("readline: " + readline);
		       if (Integer.parseInt(readline) == HttpsConnection.MIG_CGI_OK)
			 {
			    // checkjob requestet ok, retrive status and sleep_period.
			    readline = checkjob_conn.readLine();
			    while( readline != null )
			      {
				 //System.out.println("readline: " + readline);
				 if ( readline.indexOf("jobactive: ") == 0 )
				   {
				      value = readline.substring(readline.indexOf(": ")+2, readline.length());
				      if (value.indexOf("true") != 0)
					{
					   this.job_active = false;
					}
				   }
				 if ( readline.indexOf("sleep_period: ") == 0 )
				   {
				      value = readline.substring(readline.indexOf(": ")+2, readline.length());
				      this.sleep_period = Long.parseLong(value);
				   }
				 readline = checkjob_conn.readLine();
			      }
		
			 }
		    }
		  checkjob_conn.close();
		  Thread.sleep(this.sleep_period * this.SLEEPTIME_FACTOR);
	       }
	  }
	
	catch (java.lang.Exception e)
	  {
	     e.printStackTrace();
	  }
     }
}


   
   
