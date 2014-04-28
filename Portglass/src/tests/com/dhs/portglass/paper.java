package tests.com.dhs.portglass;



import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dhs.portglass.services.AccountManager;

public class paper {
	public static void main(String[] args) throws InterruptedException{
		//String result =(AccountManager.getInstance().generateRecoveryLink("manuel.saldana@upr.edu"));
		// System.out.println(result);
		System.out.println(System.getProperty("user.dir")+"/WebContent/WEB-INF/data/monitored/images/");
		
		java.util.Date date= new java.util.Date();
		 System.out.println(new Timestamp(date.getTime()));
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 String test = "2014-04-27 23:23:35.666";
		 
		 String[] ts = test.split(" ");
		 System.out.println(ts[0].trim());
		 System.out.println((ts[1].trim()));
		 
		
       
	}
}
