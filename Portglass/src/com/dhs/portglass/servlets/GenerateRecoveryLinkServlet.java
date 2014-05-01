package com.dhs.portglass.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dhs.portglass.services.AccountManager;
import com.dhs.portglass.services.MailManager;
import com.dhs.portglass.services.ThreadPoolManager;

/**
 * Servlet implementation class recovery
 */
@WebServlet("/recovery")
public class GenerateRecoveryLinkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;



	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GenerateRecoveryLinkServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * 
	 * 
	 * This method assumes that is was invoked from the Portglass password recovery form,
	 * and thus has been validated for correct parameters in the client-side prior to the 
	 * <HttpServletRequest>. The <AccountManager> will be accessed to create a new password
	 * recovery link. This link will sent to the provided email address asynchronously by 
	 * running the task on a <Thread> provided by the <TheadPoolManager>'s <ThreadPoolExecutor>.
	 * Finally, the <HttpServletResponse> is provided with a 'success' message if the link
	 * is created; 'failure' otherwise. 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 
	{
		// Set content type of the response so that jQuery knows what to expect.
		response.setContentType("text/plain");   
		response.setCharacterEncoding("UTF-8"); 
		String message = "failure";
		try {
			//Create a new Account Recovery Link
			String email = request.getParameter("email");
			String url;
			url = AccountManager.getInstance().generateRecoveryLink(email);

			//Send Email 
			ThreadPoolManager.getInstance().getThreadPoolExecutor().execute(
					MailManager.getInstance().sendAsyncPasswordRecoveryEmail(email, url));


			//Alert User that Email has been sent
			message = "success";
		}
		catch(Exception e){

		}
		response.getWriter().write(message);
		response.getWriter().close();
	}



}
