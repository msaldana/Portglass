package com.dhs.portglass.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dhs.portglass.services.AccountManager;

/**
 * Servlet implementation class availability
 * Searches the 'Account' database table to verify if an entry
 * has been made whose 'email' column matches the <HttpServletRequest>
 * "email" parameter provided. The <HttpServletResponse> is provided
 * with a response message recording a boolean 'true' if the email is
 * available; 'false' otherwise. No doGet() method is provided, as 
 * this servlet call is meant to be done asynchronously through 
 * the client-side registration forms of the Portglass System.
 * @author Manuel R Saldana
 */
@WebServlet("/availability")
public class CheckAvailabilityServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckAvailabilityServlet() {
		super();
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * Verifies the availability of the chosen "email" address. Writes the availability
	 * outcome on the <HttpServletResponse> so that the client is allowed or prohibited
	 * to chose the selected "email". Parameter is not verified for <NullPointerException>
	 * as it assumed that this class will always be invoked through the Portglass system.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		
		boolean isAvailable = AccountManager.getInstance().
				isAvailable(request.getParameter("email"));
		// Set content type of the response so that jQuery knows what it can expect.
		response.setContentType("text/plain");   
		response.setCharacterEncoding("UTF-8"); 
		// Write response body.
			response.getWriter().write(isAvailable+"");
			
	}

}
