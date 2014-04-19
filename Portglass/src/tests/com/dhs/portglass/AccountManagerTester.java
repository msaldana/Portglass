package tests.com.dhs.portglass;

import java.util.ArrayList;

import com.dhs.portglass.dto.Account;
import com.dhs.portglass.services.AccountManager;

public class AccountManagerTester {

	
	public static void newAccountTest(String name, String lastName, String email,
			String password, String phone, boolean isActive, String type)
	{
		Account account = new Account(name, lastName, email, password, phone, type, isActive);
		System.out.println("Added Account in DB: "+AccountManager.getInstance()
				.addAccount(account));
		System.out.println("Test Done!");
	}
	
	public static void getAdministratorListTest()
	{
		ArrayList<Account> admins = AccountManager.getInstance().getAdministratorList();
		for (int i = 0; i<admins.size(); i++)
		{
			System.out.println("Admin " +i+":"+ admins.get(i).getFirstName()+
					" " +admins.get(i).getLastName());
		}
	}
	
	
	public static void main(String[] args)
	{
		//newAccountTest("Manuel", "Saldana", "manuel.saldana@upr.edu", "password", 
			//	"7877871234", true, "admin");
		//getAdministratorListTest();
	}
}
