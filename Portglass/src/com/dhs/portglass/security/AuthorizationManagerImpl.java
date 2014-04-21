package com.dhs.portglass.security;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import com.dhs.portglass.dto.Account;



/**
 * Default Implementation of AuthorationManager. Loads mappings from
 * a properties file and compares URI.
 * @author Manuel R Saldana
 *
 */
public class AuthorizationManagerImpl implements AuthorizationManager
{
	private Properties roleMappings;
	
	/**Load mappings from a properties file on the file system.*/
	public AuthorizationManagerImpl()
	{
		//Read properties file containing role mappings
		this.roleMappings = new Properties();
		try
		{
			this.roleMappings.load(new FileInputStream( System
					.getProperty("file.separator")+ "mapping.properties"));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	

	@Override
	/**
	 * Returns a boolean indicating whether the user has the 
	 * appropriate clearance, according to role, for the specified
	 * URI.
	 */
	public boolean isAuthorized(Account user, String uri) {
		boolean matchFound = false;
		boolean authorized = false;
		
		@SuppressWarnings("rawtypes")
		Iterator i = roleMappings.entrySet().iterator();
		//Loop through roles, if a match is found or finished
		//iterating the file, then exit.
		while((!authorized) && (i.hasNext()))
		{
			@SuppressWarnings("rawtypes")
			Map.Entry me = (Map.Entry) i.next();
			//Pattern match. Use '*' to represent any ASCII character.
			String mapPattern = ((String)me.getValue()).replaceAll("\\*",".*");
			matchFound = Pattern.matches(mapPattern,  uri);
			if (matchFound && user.getType().equals(me.getKey()))
			{
				authorized = true;
			}
		}
		return authorized;
	}
}


