package com.dhs.portglass.server;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import com.dhs.portglass.dto.Account;
import com.dhs.portglass.security.AuthorizationManager;

/**
 *  Filters all requests for application resources and uses an 
 *  AuthorizationManager to authorize access.
 * @author Manuel R Saldana 
 *
 */
@WebFilter("/AuthorizationFilter")
public class AuthorizationFilter implements Filter 
{
	// Class Instance variables - Forward Page
	private String loginPage;

	/**
	 * Default constructor. 
	 */
	public AuthorizationFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#init(FilterConfig) 
	 * The <Filter> init method is utilized to configure the 
	 * forward login class to the one defined in the web.xml 
	 * configuration of the Portglass System.  
	 * the context parameter app.login .
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		System.out.println("Filter Init");
		if (fConfig != null)
		{
			loginPage= fConfig.getInitParameter("app.login");

		}
	}

	/**
	 * @see Filter#destroy()
	 * Unimplemented. No action needed when the application 
	 * is shutdown, with regards to this <Filter>. 
	 */
	public void destroy() { /* TODO Auto-generated method stub*/};


	/**
	 * Utility method to redirect the <ServletRequest> and <ServletResponse>
	 * to the configured login page. See the web.xml of the Portglass 
	 * system for a definition of the configuration.
	 * @param request
	 * @param response
	 */
	public void returnLogin(ServletRequest request, ServletResponse response, String message){
		// use RequestDispatcher to forward request internally
		try {
			request.setAttribute("error", message);
			request.getRequestDispatcher(loginPage).forward(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}


	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 * Obtains current <Account> (current user) from the current <HttpSession>
	 * and invokes a singleton <AuthorizationManager> to determine if  user is 
	 * authorized for the requested resource. If not, user is redirected
	 * to Login Page.
	 */
	public void doFilter(ServletRequest request, ServletResponse response, 
			FilterChain chain) throws IOException, ServletException 
			{
		
		String message;


		//Verify that login page is set up correctly
		if(loginPage == null)
		{
			System.out.println("Authorization Filter not properly configured");
			//Redirect to login page (this means web.xml is not configured properly).

			// Write response body.
			message = "filter";
			returnLogin(request, response, message);

		}
		
		Account currentUser = null;
	
		// Return the current user associated with this request
		// If there is no session, do not create one, and keep using 
		// null as the currentUser.
		if(((HttpServletRequest) request).getSession(false) != null){
			HttpSession session = ((HttpServletRequest) request).getSession(false);
			if( session.getAttribute("user") != null){
				currentUser = (Account)session.getAttribute("user");
			}
		}

		//Get requested URI
		String URI = ((HttpServletRequest)request).getRequestURI();

		//See if user is authorized
		boolean authorized = AuthorizationManager.getInstance().isAuthorized
				(currentUser, URI);
		if (authorized) 
		{
			chain.doFilter(request,response);
			System.out.println("Authorized: "+authorized);
		}
		else
		{
			System.out.println("Authorized: "+authorized);
			// Write response body.
			message = "authorization";
			returnLogin(request, response, message);
		}


	}

}
