package com.dhs.portglass.security;

import com.dhs.portglass.dto.Account;


/**
 * Manages authorization to the system. Authorization refers to
 * verification that a user has been authenticated by the service
 * and has clearance to access the requested resource.
 * @author Manuel R Saldana
 *
 */
public interface AuthorizationManager {

	/**
	 * Verifies that the user is authorized to access the 
	 * resource.
	 * @param user Object representing user of the session
	 * @param uri Universal Resource Identifier to be verified
	 * for authorization
	 * @return States if user is authorized to access resource
	 */
	public boolean isAuthorized(Account user, String uri);
}
