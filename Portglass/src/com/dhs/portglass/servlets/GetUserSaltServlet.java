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
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		// Set content type of the response so that jQuery knows what it can expect.
		response.setContentType("text/plain");   
		response.setCharacterEncoding("UTF-8");
		
		if(request.getParameter("email")!=null | request.getParameter("email").equals("null"))
		{
			// Negate the result as to return true if username exists, false otherwise.
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
