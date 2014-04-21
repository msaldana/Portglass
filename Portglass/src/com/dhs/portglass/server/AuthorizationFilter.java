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
	private String errorPage;
    
	/**
     * Default constructor. 
     */
    public AuthorizationFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see Filter#init(FilterConfig) 
	 * Filter is configured with a system login page. This
	 * pages should be set on the web.xml configuration under
	 * the context parameter error.page and login.page.
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		if (fConfig != null)
		{
			errorPage = fConfig.getInitParameter("error.page");
		}
	}
    
	/**
	 * @see Filter#destroy() 
	 */
	public void destroy() { /* TODO Auto-generated method stub*/};
	
	private void returnError(ServletRequest request, ServletResponse response,
			String[] errorDetails) throws ServletException, IOException
	{
		request.setAttribute("details", errorDetails);
		request.getRequestDispatcher(errorPage).forward(request, response);
	}
	  
	 
	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 * Obtains current Account (current user) from the current session and
	 * invokes a singleton AuthorizationManager to determine if  user is 
	 * authorized for the requested resource. If not, user is redirected
	 * to Login Page.
	 */
	public void doFilter(ServletRequest request, ServletResponse response, 
			FilterChain chain) throws IOException, ServletException 
	{
		//Verify that login page is set up correctly
		if(errorPage == null)
		{
			String[] details = {"AuthorizationFilter not properly configured! " +
					"Contact Administrator."};
					
			returnError(request, response, details);
		}
		// Return the current session associated with this request
		// If there is no session, do not create one.
		HttpSession session = ((HttpServletRequest)request).getSession(false);
		// Retrieve the session's user.
		Account currentUser = (Account)session.getAttribute("user");
		
		if (currentUser == null)
		{
			String[] details = {"Please login to access data."};
			returnError(request, response, details);
		}
		
		else
		{
			//Get requested URI
			String URI = ((HttpServletRequest)request).getRequestURI();
			
			//See if user is authorized
			boolean authorized = AuthorizationManager.getInstance().isAuthorized
					(currentUser, URI);
			if (authorized) 
			{
				chain.doFilter(request,response);
			}
			else
			{
				String[] details = {"You don't have access to this resource."};
				returnError(request, response, details);
			}
			
		}
				
	}
	
}
