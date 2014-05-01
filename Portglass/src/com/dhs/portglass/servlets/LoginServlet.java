package com.dhs.portglass.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dhs.portglass.dto.Account;
import com.dhs.portglass.services.AccountManager;

/**
 * Servlet implementation class for the 'login' Web Servlet. When the
 * post method is invoked, the request is accessed, in order to retrieve
 * an 'email' and 'password' attribute. If these parameters have been 
 * set, the <AccountManager> class is invoked in order to create an <Account>
 * DTO containing all database data on the given 'email'. If the <Account>
 * object is not null, then the request 'password' parameter is compared to 
 * the getPassword() method of the DTO. If they match, the <HttpSession> is 
 * retrieved from the <HttpServletRequest>, and the DTO is set as the current 
 * 'user' for the session. Upon reaching this point, the <HttpServletResponse>
 * is sent back with a 'successful' message; otherwise, the message reads 'failure'.
 *  @author Manuel R Saldana 
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * Verifies if 'email' and 'password' are defined parameters of the <HttpServletResponse>,
	 * and uses these values to fetch a user from the 'Account' database table. If
	 * a match is found, the password value from the table is compared to the provided
	 * parameter. If both passwords match, a session scoped variable "user" is defined
	 * with the retrieved <Account> object. The response writer receives 'successful'
	 * upon reaching this point, or 'failure' otherwise.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		
		// Set content type of the response so that jQuery knows what to expect.
		response.setContentType("text/plain");   
		response.setCharacterEncoding("UTF-8"); 
		
		
		//Only enter if an 'email' and 'password' parameter is provided in the request. 
		if(request.getParameter("email")!=null | request.getParameter("password")!=null)
		{
			
			/*
			 * Query the database for a user that has the given email address.
			 * If the email does not exist, the <Account> instance will be null; thus,
			 * only authenticate a user if the email address belongs to an entry in 
			 * the 'Account' database table AND if the 'password' column matches
			 * the 'password' parameter of this request.
			 */
			Account user = AccountManager.getInstance().getUser(request.getParameter("email"));
			if (user != null && user.getPassword().equals(request.getParameter("password"))
					&& user.isActive())		
			{
				/*Save the user in session */
				HttpSession session = request.getSession();
				session.setAttribute("user", user);
				
			
				/* Report that user is logged in at client side. */
		        response.getWriter().write("success");
			}
			// Write response fail: FAILURE. Failed because not same password.
			else
			{
				response.getWriter().write("failure");
			}
		}
		else
			// Write response fail: FAILURE. Failed because of null parameters..
		{
			response.getWriter().write("failure");
		}
	}
}
