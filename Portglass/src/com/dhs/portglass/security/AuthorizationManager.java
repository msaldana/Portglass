package com.dhs.portglass.security;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import com.dhs.portglass.dto.Account;



/**
 * Manages authorization to the system. Authorization refers to
 * verification that a user has been authenticated by the service
 * and has clearance to access the requested resource.
 * @author Manuel R Saldana
 *
 */
public class AuthorizationManager 
{
	private static final AuthorizationManager INSTANCE = new AuthorizationManager();
	private static final String PROPERTIES = "/Users/Manuel/git/Portglass/Portglass/WebContent/WEB-INF/mapping.properties";

	/**
	 * Returns only one instance of the Authorization Manager to the 
	 * thread that invokes it. Used to guarantee that the instance
	 * remains a singleton.
	 * @return
	 */
	public static synchronized final AuthorizationManager getInstance() {
		return AuthorizationManager.INSTANCE;
	}


	private Properties roleMappings;

	/**Load mappings from a properties file on the file system.*/
	private AuthorizationManager()
	{
		//Read properties file containing role mappings
		this.roleMappings = new Properties();
		try
		{
			this.roleMappings.load(new FileInputStream(PROPERTIES));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}


	/**
	 * Verifies that the user is authorized to access the 
	 * resource.
	 * @param user Object representing user of the session
	 * @param uri Universal Resource Identifier to be verified
	 * for authorization
	 * @return States if user is authorized to access resource
	 */
	public boolean isAuthorized(Account user, String uri) {
		boolean matchFound = false;
		boolean authorized = true;

		
			System.out.println("checking user");
			@SuppressWarnings("rawtypes")
			Iterator i = roleMappings.entrySet().iterator();
			//Loop through roles, if a match is found or finished
			//iterating the file, then exit.
			while((i.hasNext()))
			{
				@SuppressWarnings("rawtypes")
				Map.Entry me = (Map.Entry) i.next();
				//Pattern match. Use '*' to represent any ASCII character.
				String mapPattern = ((String)me.getValue()).replaceAll("\\*",".*");
				matchFound = Pattern.matches(mapPattern,  uri);
				if (matchFound && user ==null)
				{
					System.out.println("null user");
					authorized = false;
				}
				else if (matchFound && user != null)
				{
					System.out.println("user type is:"+user.getType()+".");
					System.out.println("entry:"+me.getKey().toString()+".");
					
					if (!me.getKey().toString().equals(user.getType())){
						authorized = false;
					}
					if (me.getKey().toString().equals("general") && user.getType().equals("admin")){
						authorized = true;
					}
				}
			}
		
		return authorized;
	}
}


