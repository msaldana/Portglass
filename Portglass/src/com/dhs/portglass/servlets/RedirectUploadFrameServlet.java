package com.dhs.portglass.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class upload_frame
 */
@WebServlet("/upload_frame")
public class RedirectUploadFrameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RedirectUploadFrameServlet() {
		super();
		// TODO Auto-generated constructor stub
		
	}	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub




		String forwardURL = "/WEB-INF/frame.jsp";




		// use RequestDispatcher to forward request internally
		try {
			request.getRequestDispatcher(forwardURL).forward(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


}
