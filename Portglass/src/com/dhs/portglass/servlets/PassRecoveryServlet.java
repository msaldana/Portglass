package com.dhs.portglass.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dhs.portglass.services.AccountManager;

/**
 * Servlet implementation class passRecovery
 */
@WebServlet("/passRecovery")
public class PassRecoveryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PassRecoveryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		String forwardURL = "./error.jsp";
		
		if(nullChecker(request.getParameter("key")))
		{
			//Include error message: Empty parameter.
			String [] details = {"We apologize for the inconvenience, but a password" +
					" cannot be changed without a valid key."};
			request.setAttribute("details", details);
			
		}
		
		else{
			String key = request.getParameter("key");
			//Check if key wasn't generated.
			if (key.equals("-1")){
				String [] details = {"We apologize for the inconvenience, but the link" +
						" is corrupt.", "Go back to the account" +
						" recovery page and request recovery again."};
				request.setAttribute("details", details);
			}
			//Check if the key is invalid.
			else if(!AccountManager.getInstance().isValidRecoveryLink(key))
			{
				String [] details = {"We apologize for the inconvenience, but the provided" +
						" link is either invalid or has expired.", "Go back to the account" +
						" recovery page and request recovery again."};
				request.setAttribute("details", details);
			}
			//Do if key is valid:
			else
			{
				request.setAttribute("key", key);
				forwardURL = "/WEB-INF/passchange.jsp";
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * Not used: if invoked, let the response/request be handled by the doGet method.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
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
