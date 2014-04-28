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
import com.dhs.portglass.services.AccountManager;
import com.dhs.portglass.services.ImageManager;


@WebServlet("/search")
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		ArrayList<Object> list = new ArrayList<Object>();
		int filter; 
		String query;
		//String type = (String) request.getSession().getAttribute("type");
		String type = "admin";


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
			if (filter>4 && filter<8){
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
		}
		catch (Exception ex) {

		} 
		System.out.println("Leaving Servlet");
		response.setContentType("application/json");
		response.getWriter().write(json.toString());

	}

}
