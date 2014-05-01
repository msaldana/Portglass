package com.dhs.portglass.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dhs.portglass.services.AccountManager;

/**
 * Servlet implementation class userSalt
 * Makes use of the <AccountManager> to access the 'Account'
 * database table. A parameter is given to filter results based
 * on the 'salt' column. The resulting value is handed back to 
 * the client through use of the <HttpServletResponse> writer.
 * @author Manuel R Saldana
 */
@WebServlet("/userSalt")
public class GetUserSaltServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetUserSaltServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * Uses an "email" parameter retrieved from the <HttpServletRequest> to retrieve
	 * the 'salt' column of the matching entry in the 'Account' database table. This
	 * is done by invoking the getSalt(email) method of the <AccountManager> class.
	 * The <HttpResponse> is attributed a 'failure' message upon failure (will happen
	 * mostly if the "email" parameter is not provided. Otherwise, the response will 
	 * have the 'salt' value written into it. 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		// Set content type of the response so that jQuery knows what it can expect.
		response.setContentType("text/plain");   
		response.setCharacterEncoding("UTF-8");
		
		if(request.getParameter("email")!=null | request.getParameter("email").equals("null"))
		{
		
			String salt = AccountManager.getInstance().getSalt(request.getParameter("email"));
			
			// Write response body: SUCCESS
			if (salt != null) response.getWriter().write(salt);
			else{
				// Write response body: FAILURE because of unregistered email.
				response.getWriter().write("failure");
			}
		}
		else
		{
			// Write response body: FAILURE because of null parameter.
			response.getWriter().write("failure");
		}
	

	}

}
