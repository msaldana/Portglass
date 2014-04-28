package com.dhs.portglass.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dhs.portglass.dto.Image;
import com.dhs.portglass.server.DBManager;

public class ImageManager {

	//Singleton instance of the ImageManager
	private static final ImageManager INSTANCE = new ImageManager();

	

	/*****************************************************
	 * Queries to retrieve Image data from database
	 ****************************************************/

	/*
	 * Retrieve all entries from the 'Image' database table whose 'creator' 
	 * column matches the provided value. 
	 */
	private static final String GET_IMAGES_BY_CREATOR = "SELECT * FROM Image WHERE creator = " +
			"?";
	
	/*
	 * Retrieve all entries from the 'Image' database table whose 'name' 
	 * column matches the provided value. 
	 */
	private static final String GET_IMAGES_BY_NAME = "SELECT * FROM Image WHERE name = " +
			"?";
	/*
	 * Retrieve all entries from the 'Images' database table whose 'type' 
	 * column matches the provided value. 
	 */
	private static final String GET_IMAGES_BY_TYPE = "SELECT * FROM Image WHERE" +
			" type = ?";

	/*****************************************************
	 * Queries to create Image data in the database
	 ****************************************************/

	/*
	 * Creates a new entry in the account table initialized at every 
	 * column with the provided values.
	 */
	private static final String ADD_IMAGE = "INSERT INTO image(name, type, description, " +
			"size, datecreated, creator, filename) VALUES (?, ?, ?, " +
			"?, ?, ?, ?)";

	/*****************************************************
	 * Queries to update Image data in the database
	 ****************************************************/

	/*****************************************************
	 * Queries to delete Image data in the database
	 ****************************************************/

	/*****************************************************
	 * Other Instance Variables
	 ****************************************************/

	/**
	 * Basic Constructor of the ImageManager object.
	 */
	private ImageManager()
	{
		super();
	}

	/**
	 * This method ensures that no other active instance of the ImageManager
	 * is present. It returns the singleton instance of the manager object.
	 * @return
	 */
	public static synchronized final ImageManager getInstance(){
		return INSTANCE;

	}


	/* *************************************************************************
	 *
	 * INSERT METHODS
	 *
	 **************************************************************************/

	/**
	 * Creates a new entry in the 'Image' database table. 
	 * Fills each column of this table with the contents of the Image DTO.
	 * If the query is executed correctly, this method returns true; false
	 * otherwise. 
	 * @param image The Image DTO that will be added to the 'image' 
	 * database table.
	 * @return A boolean indicating if the query was processed by the 
	 * database.
	 */
	public boolean addImage(Image image) {		

		ArrayList<Object> statements = new ArrayList<Object>();
		statements.add(image.getName());
		statements.add(image.getType());
		statements.add(image.getDescription());
		statements.add(image.getSize());
		statements.add(image.getDateCreated());
		statements.add(image.getCreator());
		statements.add(image.getFileName());


		return DBManager.update(ADD_IMAGE, statements.toArray());
	}




	/* *************************************************************************
	 *
	 * UPDATE METHODS
	 *
	 **************************************************************************/

	/* *************************************************************************
	 *
	 * DELETE METHODS
	 *
	 **************************************************************************/

	/* *************************************************************************
	 *
	 * SELECT METHODS
	 *
	 **************************************************************************/

	/**
	 * Queries the 'Image' table of the database for the rows whose 'creator'
	 * column matches the value of the parameter.
	 * @param query String representation of an attribute to be compared with
	 * the 'creator' column of the database.
	 * @return An array list of Images filtered by 'creator'. Empty if no entries
	 * are found or an error occurred.
	 */
	public ArrayList<Object> getImagesByCreator(String query)
	{
		ArrayList<Object> list = new ArrayList<Object>();

		list.add(query);
		ResultSet rs = DBManager.execute(GET_IMAGES_BY_CREATOR, list.toArray());
		list.clear();
		try {
			while (rs.next())
			{							
				list.add(createImageFromRS(rs));
			}
		} catch (Exception e ) {
			e.printStackTrace();
		}

		return list;		
	}
	
	/**
	 * Queries the 'Image' table of the database for the rows whose 'name'
	 * column matches the value of the parameter.
	 * @param query String representation of an attribute to be compared with
	 * the 'name' column of the database.
	 * @return An array list of Images filtered by 'name'. Empty if no entries
	 * are found or an error occurred.
	 */
	public ArrayList<Object> getImagesByName(String query)
	{
		ArrayList<Object> list = new ArrayList<Object>();

		list.add(query);
		ResultSet rs = DBManager.execute(GET_IMAGES_BY_NAME, list.toArray());
		list.clear();
		try {
			while (rs.next())
			{							
				list.add(createImageFromRS(rs));
			}
		} catch (Exception e ) {
			e.printStackTrace();
		}

		return list;		
	}
	
	/**
	 * Queries the 'Image' table of the database for the rows whose 'type'
	 * column matches the value of the parameter.
	 * @param query String representation of an attribute to be compared with
	 * the 'type' column of the database.
	 * @return An array list of Images filtered by 'type'. Empty if no entries
	 * are found or an error occurred.
	 */
	public ArrayList<Object> getImagesByType(String query)
	{
		ArrayList<Object> list = new ArrayList<Object>();

		list.add(query);
		ResultSet rs = DBManager.execute(GET_IMAGES_BY_TYPE, list.toArray());
		list.clear();
		try {
			while (rs.next())
			{							
				list.add(createImageFromRS(rs));
			}
		} catch (Exception e ) {
			e.printStackTrace();
		}

		return list;		
	}

	/* *************************************************************************
	 *
	 * UTILITY METHODS
	 *
	 **************************************************************************/


	/**
	 * Utility method to add the result set columns to an <Image> object in the respective
	 * instance fields.
	 * @param rs The result set returned from the data base query
	 * @throws Exception  Occurs when an incorrect index is selected and does not 
	 * match the <Image> object types.
	 * @return An <Image> object with the information of the DB, null if an exception 
	 * occurs.
	 */
	private Image createImageFromRS(ResultSet rs) {
		Image result = null;
		try {
			result = new Image(rs.getString(1), rs.getString(2),
					rs.getString(3), rs.getString(4), rs.getString(5),
					rs.getString(6), rs.getString(7));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

}
