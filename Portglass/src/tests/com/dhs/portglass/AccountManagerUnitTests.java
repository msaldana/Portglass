package tests.com.dhs.portglass;

import static org.junit.Assert.*;

import org.junit.Test;

import com.dhs.portglass.dto.Account;
import com.dhs.portglass.services.AccountManager;

public class AccountManagerUnitTests {
	
	Account testAccount = new Account("Manuel", "Saldana", "manuel.saldana@upr.edu",
			"password", "7877871234", "admin", true);

	@Test
	public void testGetUser() {
		Account result = AccountManager.getInstance().getUser(testAccount);
		assertEquals(testAccount.getEmail(), result.getEmail());
	}
	
	@Test
	public void testUsernameAvailability(){
		assertEquals(true, AccountManager.getInstance().isAvailable("test123@notvalid.com"));
		assertEquals(false, AccountManager.getInstance().isAvailable("manuel.saldana@upr.edu"));
	}
	
	
}
