package com.dhs.portglass.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dhs.portglass.security.PasswordManager;
import com.dhs.portglass.services.AccountManager;

/**
 * Servlet implementation class passChange
 */
@WebServlet("/passChange")
public class PassChangeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PassChangeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		String forwardURL = "./error.jsp";
		
		if(nullChecker(request.getParameter("key")))
		{
			//Include error message: Empty parameter.
			String [] details = {"We apologize for the inconvenience, but a password" +
					" cannot be changed without a valid key."};
			request.setAttribute("details", details);
			
		}
		
		else if(nullChecker(request.getParameter("password")))
		{
			//Include error message: Empty parameter.
			String [] details = {"We apologize for the inconvenience, but the password" +
					" cannot be blank."};
			request.setAttribute("details", details);
			
		}
		
		else{
			String key = request.getParameter("key");
			String password = PasswordManager.encrypt(request.getParameter("password"));
			//Change Pass of Associated Key
			if(AccountManager.getInstance().updatePasswordThroughLink(key, password)){
				//Delete Key
				AccountManager.getInstance().deletePasswordLink(key);
				forwardURL = "./success.jsp";
			}
			//Failed to change password
			else
			{
				String [] details = {"We apologize for the inconvenience, but the password" +
				" could not be changed at this moment."};
				request.setAttribute("details", details);
		
			}
			
				
		}
		
		
		
        
        // use RequestDispatcher to forward request internally
        try {
            request.getRequestDispatcher(forwardURL).forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
		return (requestParam==null);

	}
}