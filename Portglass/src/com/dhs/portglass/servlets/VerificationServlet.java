package com.dhs.portglass.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dhs.portglass.dto.Account;
import com.dhs.portglass.services.ImageManager;
import com.dhs.portglass.services.SensorManager;

/**
 * Servlet implementation class VerificationServlet
 */
@WebServlet("/s/verify")
public class VerificationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VerificationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("error.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		int filter = Integer.parseInt(request.getParameter("filter"));
		String email = ((Account) request.getSession().getAttribute("user")).getEmail();
		String value = request.getParameter("value");
		
		boolean isConditionMet = false;
		
		switch(filter){
		
		case 1: isConditionMet = ImageManager.getInstance().isFollowing(value, email);
				break;
		case 2: isConditionMet = SensorManager.getInstance().isFollowing(value, email);
				break;
		default: isConditionMet = false;
				break;
		}
		
		
		// Set content type of the response so that jQuery knows what it can expect.
		response.setContentType("text/plain");   
		response.setCharacterEncoding("UTF-8"); 
		// Write response body.
			response.getWriter().write(isConditionMet+"");
	}

}
