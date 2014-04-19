package tests.com.dhs.portglass;

import com.dhs.portglass.services.AccountManager;

public class paper {
	public static void main(String[] args) throws InterruptedException{
		//String result =(AccountManager.getInstance().generateRecoveryLink("manuel.saldana@upr.edu"));
		// System.out.println(result);
		System.out.println(AccountManager.getInstance().isValidRecoveryLink("73bd5b77b80b2aa7d3b03d85c9c2af36d94cd06cf65b768a65cb1d54a8fc0f84"));
	}
}
