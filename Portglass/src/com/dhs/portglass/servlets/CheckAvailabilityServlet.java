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
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(nullChecker(request.getParameter("email")))
		{
			//Did not retrieve email from client.
		}
		boolean isAvailable = AccountManager.getInstance().
				isAvailable(request.getParameter("email"));
		// Set content type of the response so that jQuery knows what it can expect.
		response.setContentType("text/plain");   
		response.setCharacterEncoding("UTF-8"); 
		// Write response body.
			response.getWriter().write(isAvailable+"");
			
	}

	/**
	 * Verifies if the ID parameter is not defined. It returns a boolean which will be
	 * picked up on the doPost(request, response) method and forwarded to the error 
	 * page if true.
	 * @param requestParam An URL parameter
	 * @return Contents of the parameter
	 */
	private static boolean nullChecker(Object requestParam)
	{
		return (requestParam.equals(null));

	}

}
