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
import com.dhs.portglass.dto.Sensor;
import com.dhs.portglass.services.SensorManager;

/**
 * Servlet implementation class CreateSensorServlet
 */
@WebServlet("/sa/newsensor")
public class CreateSensorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateSensorServlet() {
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
		// TODO Auto-generated method stub
		// Set content type of the response so that jQuery knows what to expect.
				response.setContentType("text/plain");   
				response.setCharacterEncoding("UTF-8"); 
				String message = "failure";
				
				try{
					//Current time
					DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					Date date = new Date(System.currentTimeMillis());
					String timestamp = dateFormat.format(date);
					

					//Create Sensor Object with given data
					Sensor sensor = new Sensor (request.getParameter("name"), 
							request.getParameter("location"), request.getParameter("status"), 
							timestamp, request.getParameter("description"), request.getParameter("serial"), 
							true);
					

					//Try to add Sensor to DB 
					if(SensorManager.getInstance().addSensor(sensor))
					{
						message="success";

						
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
