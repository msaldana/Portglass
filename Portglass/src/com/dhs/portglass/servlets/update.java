package com.dhs.portglass.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dhs.portglass.services.AccountManager;

/**
 * Servlet implementation class update
 */
@WebServlet("/update")
public class update extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public update() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("entro");
				int filter; 
				String value = null, email = null;
				//String type = (String) request.getSession().getAttribute("type");
				String type = "admin";
				boolean isUpdated = false;
				
				if(request.getParameter("filter")==null || request.getParameter("filter").equals("null"))
					filter=0;
				else{
					filter = Integer.parseInt(request.getParameter("filter"));
				}
				if(request.getParameter("value")==null || request.getParameter("value").equals("null"))
				{
					value="";
				}
				if(request.getParameter("email")==null || request.getParameter("email").equals("null"))
				{
					filter=0;
				}
				else{
					email=request.getParameter("email");
					value=request.getParameter("value");
					
				}
				System.out.println("Updating:" + email);
				System.out.println("Selected Value:"+value);
				
				   switch (filter) {
		           case 1:  isUpdated = AccountManager.getInstance().updateFirstName(value, email);
		                    break;
		           case 2:  isUpdated = AccountManager.getInstance().updateLastName(value, email);
		                    break;
		           case 3:  isUpdated = AccountManager.getInstance().updateEmail(value, email);
		                    break;
		           case 4:  isUpdated = AccountManager.getInstance().updatePhone(value, email);
		                    break;
		           case 5:	isUpdated = AccountManager.getInstance().updateType(value, email);
		           			break;
		           case 6:	isUpdated = AccountManager.getInstance().deleteAccount(email);
		           			break;
		           default: isUpdated = false;
		                    break;
				   }
				   
				   System.out.println("IsUpdated:"+isUpdated);
				// Set content type of the response so that jQuery knows what it can expect.
					response.setContentType("text/plain");   
					response.setCharacterEncoding("UTF-8"); 
					// Write response body.
					response.getWriter().write(isUpdated+"");
	}
	

}
