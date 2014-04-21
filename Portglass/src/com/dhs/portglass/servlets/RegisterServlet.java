package com.dhs.portglass.servlets;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dhs.portglass.dto.Account;
import com.dhs.portglass.services.AccountManager;
import com.dhs.portglass.services.MailManager;
import com.dhs.portglass.util.ThreadPoolController;


/**
 * Servlet implementation class register
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    
  

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * This method is invoked by a client side request. It will verify that the input parameters
	 * are not empty or null. If one of these conditions is true, it will forward the response to an Error
	 * Page that details that the parameter was empty. On the other hand, if the parameter is buffered with
	 * a non-blank character, an Account object will be created with the given information and the 
	 * AccountManager will be invoked, in order to add the user to the database. The account will be set
	 * with an active boolean set at false, meaning that the account is created but not yet active. If 
	 * the AccountManager successfully adds the user, an email will be sent to all administrators, stating
	 * that a user has requested access to the system. Finally, the client is forwarded to an error page if
	 * any error is encountered, otherwise to a success page. 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		
		if(nullChecker(request.getParameter("name")))
		{
			
		}
				
		else
		{
			//Create Account Object with given data
			Account account = new Account (request.getParameter("name"), 
					request.getParameter("last_name"), request.getParameter("email"), 
					request.getParameter("password"), 
					request.getParameter("phone"), false,request.getParameter("type_select"), 
					request.getParameter("salt"));
			
			//Try to add Account to DB 
			if(AccountManager.getInstance().addAccount(account))
			{
			
				//Send Email to All Administrators Asynchronously
				ThreadPoolController.getInstance().getThreadPoolExecutor().execute(
						MailManager.getInstance().sendAsyncNewAccountEmail(account));
				
			}
			else
			{
				//Forward User to Error Page
			}
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
		return (requestParam.equals(null));
		
	}
}


