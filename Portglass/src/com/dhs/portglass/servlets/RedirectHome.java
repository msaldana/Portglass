package com.dhs.portglass.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dhs.portglass.dto.Account;

/**
 * Servlet implementation class home
 */
@WebServlet("/home")
public class RedirectHome extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RedirectHome() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try{
			System.out.println("got to this point");
			Account user = (Account) request.getSession().getAttribute("user");
			if (user.getType().equals("admin")){
				System.out.println("here");
				response.sendRedirect("sa/home.jsp");
			}
			if (user.getType().equals("general")){
				response.sendRedirect("s/home.jsp");
			}
		}
		catch (Exception e){
			//No user in session - redirect to login page
			response.sendRedirect("index.jsp");
		}

	}

}
