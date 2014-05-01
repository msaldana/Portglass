package com.dhs.portglass.servlets;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dhs.portglass.services.AccountManager;
import com.dhs.portglass.services.ImageManager;
import com.dhs.portglass.services.SensorManager;
import com.dhs.portglass.dto.Account;

/**
 * Servlet implementation class update
 */
@WebServlet("/s/update")
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
		
				int filter=0; 
				String value = null, email = null;
				String user = ((Account) request.getSession().getAttribute("user")).getEmail();
				System.out.println("Trying to update");
				boolean isUpdated = false;
				
				
				filter = Integer.parseInt(request.getParameter("filter"));
				
				value = request.getParameter("value");
				
				if(request.getParameter("email")==null || request.getParameter("email").equals("null"))
				{
					
				}
				else{
					email=request.getParameter("email");
					value=request.getParameter("value");
					
				}
				
				System.out.println("Value:" +value);
				System.out.println("Filter:" +filter);
				
				//Current time
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date(System.currentTimeMillis());
				String timestamp = dateFormat.format(date);
				
				
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
		           			
		           case 7: isUpdated = ImageManager.getInstance()
		        		   				.addImageFollower(value, user, timestamp);
		           			break;
		           			
		           case 8: isUpdated = ImageManager.getInstance()
		        		   				.deleteImageFollower(value, user);
		           			break;
		           			
		           case 9: isUpdated = SensorManager.getInstance()
   		   								.addSensorFollower(value, user, timestamp);
			      			break;
			      			
			      case 10: isUpdated = SensorManager.getInstance()
			   		   				.deleteSensorFollower(value, user);
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
