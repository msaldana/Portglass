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

import com.dhs.portglass.dto.Account;
import com.dhs.portglass.dto.ImageMessage;
import com.dhs.portglass.services.ImageManager;


@WebServlet("/s/post")
public class MessagePostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MessagePostServlet() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * This method is invoked by a client side request. It will verify that the input
	 * parameters are not empty or null. If one of these conditions is true, it will 
	 * return an error as part of the <OutputStream> of the <HttpServletResponse>.
	 * On the other hand, if the parameters are buffered with non-blank characters, a 
	 * <ImageMessage> object will be created with the given information. The 
	 * <ImageManager> will then be invoked, in order to add the message post to the database.
	 * If the message post is successfully added, an email and notification will be sent
	 * to all users that have commented on the <Image> belonging to this <ImageEntry>.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
			{
		//Default message response
		String result ="false";

		//Current time
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		String timestamp = dateFormat.format(date);

		//Current user
		String user = ((Account) request.getSession().getAttribute("user")).getEmail();
		//Create <ImageEntry> with the given request data.
		ImageMessage message = new ImageMessage (user, request.getParameter("image"),
				request.getParameter("message"), timestamp);

		//Try to enter post in database 
		if(ImageManager.getInstance().addImageMessage(message))
		{

			//Send Email to All Administrators Asynchronously


			//Response Message True
			result = "true";
		}

		// Set content type of the response so that jQuery knows what it can expect.
		response.setContentType("text/plain");   
		response.setCharacterEncoding("UTF-8"); 
		// Write response body.
		response.getWriter().write(result);

	}

}
