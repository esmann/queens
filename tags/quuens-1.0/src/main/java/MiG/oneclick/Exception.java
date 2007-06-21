package MiG.oneclick;

public class Exception extends java.lang.Exception
{
   // Constructor
   public Exception(String message)
     {
	super(message);
     }
   
   
    // Return the stacktrace as a string
   public static String dumpStackTrace( java.lang.Exception e )
     {
	int i;
	String result;
	
	result = "\n--------------- Begin stacktrace ----------------\n" + e + "\n";
	for( i=0; i<e.getStackTrace().length; i++ )
	  {
	     result = result + e.getStackTrace()[i].toString() + "\n";
	  }
	result = result + "\nCaused by:\n";
	
	if (e.getCause() != null )
	  {
	     for( i=0; i<e.getCause().getStackTrace().length; i++ )
	       {
		  result = result + e.getCause().getStackTrace()[i].toString() + "\n";
	       }  
	  }
	result = result + "\n---------------- End stacktrace -----------------\n";
	return result;
     }

}
