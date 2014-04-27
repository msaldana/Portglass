package tests.com.dhs.portglass;



import java.sql.Timestamp;

import com.dhs.portglass.services.AccountManager;

public class paper {
	public static void main(String[] args) throws InterruptedException{
		//String result =(AccountManager.getInstance().generateRecoveryLink("manuel.saldana@upr.edu"));
		// System.out.println(result);
		System.out.println(System.getProperty("user.dir")+"/WebContent/WEB-INF/data/monitored/images/");
		
		java.util.Date date= new java.util.Date();
		 System.out.println(new Timestamp(date.getTime()));      
       
	}
}
