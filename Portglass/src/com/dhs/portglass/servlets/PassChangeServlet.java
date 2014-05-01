package com.dhs.portglass.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dhs.portglass.services.AccountManager;

/**
 * Servlet implementation class for changing the password
 * through a secure confirmation link. The link contains a 
 * key attribute that identifies who made the request. This
 * service fails if that key is not found on the database.
 * The <AccountManager> is invoked to search the 'Recover'
 * database table for this purpose.
 * @author Manuel R Saldana
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		String key = request.getParameter("key");
		String password = request.getParameter("password");
		String salt = request.getParameter("salt");
		//Change Pass of Associated Key
		if(AccountManager.getInstance().updatePasswordThroughLink(key, password, salt)){
			//Delete Key
			AccountManager.getInstance().deletePasswordLink(key);
		}
	}
}