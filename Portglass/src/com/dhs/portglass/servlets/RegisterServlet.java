package com.dhs.portglass.servlets;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dhs.portglass.dto.Account;
import com.dhs.portglass.services.AccountManager;
import com.dhs.portglass.services.MailManager;
import com.dhs.portglass.services.NotificationManager;
import com.dhs.portglass.services.ThreadPoolManager;


/**
 * This Servlet is called upon in order to register a new user. Provided
 * that all user data is complete, according to the implementation of 
 * the <Account> DTO, and validated thru the use of the client side web
 * application; a new entry will be made in the 'Account' database table.
 * Upon success, all active administrators will receive notifications of
 * this event. An active administrator is an entry in the 'Account' 
 * database table whose 'type' column is "admin" and whose 'isactive' 
 * column is the boolean value true.
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
	 * This method assumes that it was invoked from the Portglass basic or administrator
	 * registration forms. If all <Account> constructor are met, the entry is made in the
	 * 'Account' database table through the use of the <AccountManager>. By default, the 
	 * account will be set with the 'isActive' boolean at false, meaning that the account
	 * is created but not yet active. If there is an active <Account> on the <HttpServlet>,
	 * however, its type is verified. If the user has administrator privileges, then the 
	 * account is active upon entry to the database. If the <AccountManager> returns true
	 * when the addAccount(Account) method is invoked AND the <Account> is inactive, then 
	 * an email and notification will be sent to all active administrators asynchronously. 
	 * Finally, the <HttpServletResponse> is prepared with an output message stating 
	 * 'successful' or 'failure', according to the outcome of this method.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
			{
		// Set content type of the response so that jQuery knows what to expect.
		response.setContentType("text/plain");   
		response.setCharacterEncoding("UTF-8"); 
		String message = "failure";

		// Default user account active status		
		boolean isActive = false;

		// Verify if a session exists, dont create one if it does not.
		HttpSession session = request.getSession(false);
		if(session != null){
			// See if there is a defined "user" attribute
			if(session.getAttribute("user") != null){
				// If there is a session scope <Account>, verify if 
				// the account has administrator privileges. 
				Account user = (Account) session.getAttribute("user");
				if (user.getType().equals("admin")){
					isActive = true;
				}
			}
		}


		try{
			//Current time
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date(System.currentTimeMillis());
			String timestamp = dateFormat.format(date);
			

			//Create Account Object with given data
			Account account = new Account (request.getParameter("name"), 
					request.getParameter("last_name"), request.getParameter("email"), 
					request.getParameter("password"), 
					request.getParameter("phone"), isActive,request.getParameter("type_select"), 
					request.getParameter("salt"));

			//Try to add Account to DB 
			if(AccountManager.getInstance().addAccount(account))
			{
				message="success";

				//Send Email and Notification to All Administrators Asynchronously if 
				//created account is not yet active
				if(!isActive){
					ThreadPoolManager.getInstance().getThreadPoolExecutor().execute(
							MailManager.getInstance().sendAsyncNewAccountEmail(account));
					ThreadPoolManager.getInstance().getThreadPoolExecutor().execute(
							NotificationManager.getInstance().sendAsyncAccountNotification(
									request.getParameter("email"),timestamp, "registration"));
					
				}
			}

		}
		catch(NullPointerException e){
			/*
			 * Not followed - Occurs if this method is not provided with all the 
			 * parameters of the <Account> DTO. The registration form requires that
			 * appropriate data is provided from the client-side.  
			 */
		}
		// Provide a response upon successful account generation.
		response.getWriter().write(message);
		response.getWriter().close();
	}



}


