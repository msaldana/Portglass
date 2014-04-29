package tests.com.dhs.portglass;



import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


import com.dhs.portglass.services.AccountManager;

public class paper {
	public static void main(String[] args) throws InterruptedException{
		//String result =(AccountManager.getInstance().generateRecoveryLink("manuel.saldana@upr.edu"));
		// System.out.println(result);
		System.out.println(System.getProperty("user.dir")+"/WebContent/WEB-INF/data/monitored/images/");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		System.out.println(dateFormat.format(date));
		
       
	}
}
