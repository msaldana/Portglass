package com.dhs.portglass.servlets;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dhs.portglass.dto.Account;
import com.dhs.portglass.dto.Image;
import com.dhs.portglass.dto.ImageMessage;
import com.dhs.portglass.dto.Sensor;
import com.dhs.portglass.dto.SensorMessage;
import com.dhs.portglass.services.AccountManager;
import com.dhs.portglass.services.ImageManager;
import com.dhs.portglass.services.SensorManager;


@WebServlet("/s/search")
/**
 * Invokes by a client-side request to search for information.
 * Receives a filter and a query as parameters to the POST 
 * method. The filter is used to distinguish which database
 * tables must be accessed to get the requested data. The 
 * query parameter is passed to the corresponding table
 * and the response is parsed back to the <HttpResponseServlet>
 * as as a JSON object. Some search capabilities are restricted
 * according to the <HttpSession> current user's type.
 *
 * @author Manuel R Saldana Pueyo
 */
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchServlet() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		ArrayList<Object> list = new ArrayList<Object>();
		int filter; 
		String query;
		String type = ((Account) request.getSession().getAttribute("user")).getType();
		String user = ((Account) request.getSession().getAttribute("user")).getEmail();
		


		if(request.getParameter("filter")==null || request.getParameter("filter").equals("null"))
			filter=0;
		else{
			filter = Integer.parseInt(request.getParameter("filter"));
		}
		if(request.getParameter("query")==null || request.getParameter("query").equals("null"))
		{
			query ="";
		}
		else{
			query=request.getParameter("query");

		}
		System.out.println("Searching for: " + query);

		switch (filter) {
		case 1:  list = AccountManager.getInstance().getAccountsByName(query, type);
		break;
		case 2:  list = AccountManager.getInstance().getAccountsByLastName(query, type);
		break;
		case 3:  list = AccountManager.getInstance().getAccountsByEmail(query, type);
		break;
		case 4:  list = AccountManager.getInstance().getAccountsByType(query, type);
		break;
		case 5:  list = ImageManager.getInstance().getImagesByCreator(query);
		break;
		case 6:  list = ImageManager.getInstance().getImagesByName(query);
		break;
		case 7:  list = ImageManager.getInstance().getImagesByType(query);
		break;
		case 8:  list = ImageManager.getInstance().getImageMessages(query);
		break;
		case 9:  list = ImageManager.getInstance().getImagesByCreator(user);
		break;
		case 10: list = SensorManager.getInstance().getSensorsByLocation(query);
		break;
		case 11: list = SensorManager.getInstance().getSensorsByName(query);
		break;
		case 12: list = SensorManager.getInstance().getSensorsBySerial(query);
		break;
		case 13: list = SensorManager.getInstance().getSensorsByStatus(query);
		break;
		case 14: list = SensorManager.getInstance().getSensorMessages(query);
		default:  break;
		}


		JSONObject json      = new JSONObject();
		JSONArray  items = new JSONArray();
		JSONObject jsonObjectList;
		System.out.println("Trying to parse "+ list.size() + "items");

		try
		{
			if (filter<5)
			{
				for (int i=0 ; i<list.size() ; i++)
				{
					jsonObjectList = new JSONObject();
					jsonObjectList.put("name"     , ((Account) list.get(i)).getFirstName());
					jsonObjectList.put("lastName" , ((Account) list.get(i)).getLastName());
					jsonObjectList.put("email"    , ((Account) list.get(i)).getEmail());
					jsonObjectList.put("type"     , ((Account) list.get(i)).getType());
					jsonObjectList.put("phone"    , ((Account) list.get(i)).getPhone());    
					items.put(jsonObjectList);
				}
				json.put("accounts", items);
			}
			if ((filter>4 && filter<8)|| filter==9){
				for (int i=0 ; i<list.size() ; i++)
				{
					/* Seperate Date Created by Date / Time */
					String[] timestamp = ((Image) list.get(i)).getDateCreated().split(" ");
					
					jsonObjectList = new JSONObject();
					jsonObjectList.put("name"     , ((Image) list.get(i)).getName());
					jsonObjectList.put("type" , ((Image) list.get(i)).getType());
					jsonObjectList.put("description"    , ((Image) list.get(i)).getDescription());
					jsonObjectList.put("size"     , ((Image) list.get(i)).getSize());
					jsonObjectList.put("datecreated"    , timestamp[0]);
					jsonObjectList.put("timeCreated", timestamp[1]);
					jsonObjectList.put("creator"    , ((Image) list.get(i)).getCreator());
					jsonObjectList.put("filename"    , ((Image) list.get(i)).getFileName());
					
					items.put(jsonObjectList);
				}
				json.put("images", items);
			}
			if (filter==8){
				for (int i=0 ; i<list.size() ; i++)
				{
					
					jsonObjectList = new JSONObject();
					jsonObjectList.put("author"     , ((ImageMessage) list.get(i)).getAuthor());
					jsonObjectList.put("message" , ((ImageMessage) list.get(i)).getMessage());
					jsonObjectList.put("date"    , ((ImageMessage) list.get(i)).getTimestamp().toString());
					
					items.put(jsonObjectList);
				}
				json.put("imessages", items);
			}
			
			if (filter>9 && filter<14){
				for (int i=0 ; i<list.size() ; i++)
				{
					/* Seperate Date Created by Date / Time */
					String[] timestamp = ((Sensor) list.get(i)).getDateCreated().split(" ");
					
					jsonObjectList = new JSONObject();
					jsonObjectList.put("name"     , ((Sensor) list.get(i)).getName());
					jsonObjectList.put("location" , ((Sensor) list.get(i)).getLocation());
					jsonObjectList.put("status"    , ((Sensor) list.get(i)).getStatus());
					jsonObjectList.put("description"     , ((Sensor) list.get(i)).getDescription());
					jsonObjectList.put("datecreated"    , timestamp[0]);
					jsonObjectList.put("serial", ((Sensor) list.get(i)).getSerial());
					
					items.put(jsonObjectList);
				}
				json.put("sensors", items);
			}
			
			if (filter==14){
				for (int i=0 ; i<list.size() ; i++)
				{
					
					jsonObjectList = new JSONObject();
					jsonObjectList.put("serial"     , ((SensorMessage) list.get(i)).getSensor());
					jsonObjectList.put("reporteddate" , ((SensorMessage) list.get(i)).getReportedDate());
					jsonObjectList.put("date"     , ((SensorMessage) list.get(i)).getEventDate());
					jsonObjectList.put("time" , ((SensorMessage) list.get(i)).getEventTime());
					jsonObjectList.put("details"     , ((SensorMessage) list.get(i)).getDetails());
					
					items.put(jsonObjectList);
				}
				json.put("smessages", items);
			}
				
						
		}
		catch (Exception ex) {

		} 
		System.out.println("Leaving Servlet");
		response.setContentType("application/json");
		response.getWriter().write(json.toString());
		response.getWriter().close();

	}

}
