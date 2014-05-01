package com.dhs.portglass.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dhs.portglass.dto.Image;
import com.dhs.portglass.dto.ImageMessage;
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
	
	/*
	 * Retrieve all entries from the 'Image_Entry' database table whose 
	 * 'image' column matches the provided value. These results will
	 * be sorted by descending order of the <Date> 'timestamp' column.
	 */
	private static final String GET_IMAGE_ENTRIES = "SELECT * FROM image_entry WHERE image = ? " +
			" ORDER BY timestamp DESC ";
	
	/*
	 * Retrieves all 'user' column entries from the 'Image_Follower' 
	 * database table whose 'image' column matches the given value.
	 */
	private static final String GET_IMAGE_FOLLOWERS = "SELECT user FROM image_follower WHERE " +
			" image = ?";
	
	/*
	 * Retrieves the count for email entries matching the provided 
	 * email address on the 'image_follower' database table. 
	 */
	private static final String GET_FOLLOWER_STATUS = "SELECT count(user) FROM image_follower " +
			" WHERE image = ? AND user = ?";

	/*****************************************************
	 * Queries to create Image data in the database
	 ****************************************************/

	/*
	 * Creates a new entry in the 'Image' table initialized at every 
	 * column with the provided values.
	 */
	private static final String ADD_IMAGE = "INSERT INTO image(name, type, description, " +
			"size, datecreated, creator, filename) VALUES (?, ?, ?, " +
			"?, ?, ?, ?)";
	
	/*
	 * Creates a new entry in the 'Image_Entry' table  initialized at
	 * every column with the provided values.
	 */
	private static final String ADD_ENTRY = "INSERT INTO image_entry (author, image, message, " +
			" timestamp) VALUES (?, ?, ?,?)";
	
	private static final String ADD_FOLLOWER = "INSERT INTO image_follower (image, user, timestamp ) " +
			"VALUES (?, ?, ?)";

	/*****************************************************
	 * Queries to update Image data in the database
	 ****************************************************/

	/*****************************************************
	 * Queries to delete Image data in the database
	 ****************************************************/
	
	private static final String DELETE_IMAGE_FOLLOWER = "DELETE FROM sensor_follower where " +
			"sensor = ? AND user = ?";
	
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
	
	
	/**
	 * Creates a new entry in the 'Image_Entry' database table. 
	 * Fills each column of this table with the contents of the <ImageMessage>
	 * DTO. If the query is executed correctly, this method returns true; 
	 * false otherwise. 
	 * @param image The <ImageMessage> DTO that will be added to the 
	 * 'Image_Entry' database table.
	 * @return A boolean indicating if the query was processed by the 
	 * database.
	 */
	public boolean addImageMessage(ImageMessage message) {		

		ArrayList<Object> statements = new ArrayList<Object>();
		statements.add(message.getAuthor());
		statements.add(message.getImage());
		statements.add(message.getMessage());
		statements.add(message.getTimestamp());
		
		return DBManager.update(ADD_ENTRY, statements.toArray());
	}

	/**
	 * Creates a new entry in the 'Image_Follower' database table.
	 * Fills each column of this table with the email value of the 
	 * <HttpSession>'s current user, the image name, and current
	 * <Date> and time.
	 * @param image Refers to the 'name' column of the 'Image' database
	 * table. Used as a foreign key.
	 * @param user Refers to the 'email'column of the 'Account' database
	 * table. Used as a foreign key.
	 * @param timestamp Current date and time
	 * @return A boolean indicating if the query was processed by the 
	 * database.
	 */
	public boolean addImageFollower(String image, String user, String timestamp){
		ArrayList<Object> statements = new ArrayList<Object>();
		statements.add(image);
		statements.add(user);
		statements.add(timestamp);
		return DBManager.update(ADD_FOLLOWER, statements.toArray());
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
	 * Queries the DB to count the amount of current entries with the 
	 * provided email value. This method is preset to return false 
	 * unless the database responds that no entry is found. If the count
	 * is zero, then this email has not been used and is available to use
	 * as a username (UNIQUE identifier).
	 * @param email Corresponds to a value to be compared to the 'user'
	 * column of the 'Image_Follower' database' table.
	 * @return A boolean stating whether or not there is a matching entry in
	 *  the 'Image_Follower' table.
	 */
	public boolean isFollowing(String image, String email)
	{
		boolean isAvailable = false;
		ArrayList<Object> expressions = new ArrayList<Object>();
		expressions.add(image);
		expressions.add(email);
		ResultSet rs = DBManager.execute(GET_FOLLOWER_STATUS, expressions.toArray());

		try {
			if(rs.next() && rs.isFirst() && rs.isLast())
			{
				if (rs.getInt(1)==0) isAvailable = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isAvailable;
	}

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
	
	/**
	 * Queries the 'Image_Follower' table of the database for the rows whose
	 * 'image' column matches the value of the parameter. This query returns
	 * only the 'user' column.
	 * @param query String representation of an attribute to be compared with
	 * the 'image' column of the database.
	 * @return An array list of <String> of all user accounts following the 
	 * image. 
	 */
	public ArrayList<String> getImageFollowers(String image)
	{
		ArrayList<String> list = new ArrayList<String>();

		list.add(image);
		ResultSet rs = DBManager.execute(GET_IMAGE_FOLLOWERS, list.toArray());
		list.clear();
		try {
			while (rs.next())
			{							
				list.add(rs.getString(1));
			}
		} catch (Exception e ) {
			e.printStackTrace();
		}

		return list;		
	}
	
	/**
	 * Queries the 'Image_Entry' table of the database for the rows whose 'image'
	 * column matches the value of the parameter. Results are ordered in accordance
	 * to the 'timestamp' <Date> column of the 'Image_Entry' table.
	 * @param query String representation of an attribute to be compared with
	 * the 'image' column of the database.
	 * @return An array list of <ImageMessages> masked by <Object>
	 * filtered by 'image'. Empty if no entries are found or an error occurred.
	 */
	public ArrayList<Object> getImageMessages(String query)
	{
		ArrayList<Object> list = new ArrayList<Object>();

		list.add(query);
		ResultSet rs = DBManager.execute(GET_IMAGE_ENTRIES, list.toArray());
		list.clear();
		try {
			while (rs.next())
			{							
				list.add(createImageMessageFromRS(rs));
			}
		} catch (Exception e ) {
			e.printStackTrace();
		}

		return list;		
	}
	
	public boolean deleteImageFollower(String image, String email)
	{
		ArrayList<Object> expressions = new ArrayList<Object>();
		expressions.add(image);
		expressions.add(email);
		return DBManager.update(DELETE_IMAGE_FOLLOWER, expressions.toArray());
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
	

	/**
	 * Utility method to add the result set columns to an <ImageMessage> object
	 * in the respective instance fields.
	 * @param rs The result set returned from the data base query
	 * @throws Exception  Occurs when an incorrect index is selected and does not 
	 * match the <ImageMessage> object types.
	 * @return An <ImageMessage> object with the information of the DB, null if
	 * an exception occurs.
	 */
	private ImageMessage createImageMessageFromRS(ResultSet rs) {
		ImageMessage result = null;
		try {
			result = new ImageMessage(rs.getString(1), rs.getString(2),
					rs.getString(3), rs.getString(4));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

}
