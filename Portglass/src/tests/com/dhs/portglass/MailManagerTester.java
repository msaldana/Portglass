package tests.com.dhs.portglass;

import com.dhs.portglass.dto.Account;
import com.dhs.portglass.services.MailManager;


public class MailManagerTester
{
	/*
	 * Dummy data fields for tests.
	 */
	
    private Account testUser = null;
    
    /**
     * Constructor of MailControllerTester. 
     */
  	public MailManagerTester()
  	{
  		
  		testUser = new Account();
  		testUser.setEmail("noreply.portglass@gmail.com");
  	  	testUser.setFirstName("Noreply");
  	  	testUser.setPassword("password");
  	  	testUser.setType("General User");
  	  	testUser.setLastName("Portglass");
  	  	
  	}
  	
	
	
	
	/**
  	 * Test functionality of completing new account request email.
  	 */
  	public void testAccountCompletionEmail(){
  		MailManager.getInstance().sendNewAccountCompletionEmail(testUser);
  	}
  	
  	public void testNewAccountEmail(){
  		
  	}
  	
  	public void testNewAccountRecoveryLinkEmail(){
  		
  	}
  	
  	
  	public static void main(String[] args)
  	{
  		//MailManagerTester mailTester = new MailManagerTester();
  		//mailTester.testAccountCompletionEmail();
  		//mailTester.testNewAccountEmail();
  		//mailTester.testNewAccountRecoveryLinkEmail();
  	}
}
