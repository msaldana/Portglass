package com.dhs.portglass.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dhs.portglass.dto.SensorMessage;
import com.dhs.portglass.dto.Sensor;
import com.dhs.portglass.server.DBManager;

public class SensorManager {


	//Singleton instance of the SensorManager
	private static final SensorManager INSTANCE = new SensorManager();

	/*****************************************************
	 * Queries to retrieve sensor data from database
	 ****************************************************/

	/*
	 * Retrieves the count for sensor entries matching the provided 
	 * serial identifier. 
	 */
	private static final String GET_AVAILABILITY = "SELECT count(serial) FROM Sensor " +
			" WHERE serial =?";
	
	/*
	 * Retrieves the count for email entries matching the provided 
	 * email address on the 'sensor_follower' database table. 
	 */
	private static final String GET_FOLLOWER_STATUS = "SELECT count(user) FROM sensor_follower " +
			" WHERE sensor = ? AND user = ?";

	/* 
	 * Retrieves a sensor entry from the 'Sensor' database table 
	 * through use of the 'location' column.
	 */
	private static final String GET_SENSOR_BY_LOCATION = "SELECT * FROM Sensor WHERE location = ? ";
	
	/* 
	 * Retrieves a sensor entry from the 'Sensor' database table 
	 * through use of the 'name' column.
	 */
	private static final String GET_SENSOR_BY_NAME = "SELECT * FROM Sensor WHERE name = ? ";

	/* 
	 * Retrieves a sensor entry from the 'Sensor' database table 
	 * through use of the unique identifier 'serial' column.
	 */
	private static final String GET_SENSOR_BY_SERIAL = "SELECT * FROM Sensor WHERE serial = ? ";

	/* 
	 * Retrieves a sensor entry from the 'Sensor' database table 
	 * through use of the 'status' column.
	 */
	private static final String GET_SENSOR_BY_STATUS = "SELECT * FROM Sensor WHERE status = ? ";

	/*
	 * Retrieve all entries from the 'Sensor_Entry' database table whose 
	 * 'sensor' column matches the provided value. These results will
	 * be sorted by descending order of the <Date> 'timestamp' column.
	 */
	private static final String GET_SENSOR_ENTRIES = "SELECT * FROM sensor_entry WHERE sensor_id = ? " +
			" ORDER BY timestamp DESC ";
	
	/*
	 * Retrieves all 'user' column entries from the 'Sensor_Follower' 
	 * database table whose 'sensor' column matches the given value.
	 */
	private static final String GET_SENSOR_FOLLOWERS = "SELECT user FROM sensor_follower WHERE " +
			" sensor = ?";

	/*****************************************************
	 * Queries to create sensor data in the database
	 ****************************************************/

	/*
	 * Creates a new entry in the sensor table initialized at every 
	 * column with the provided values.
	 */
	private static final String ADD_SENSOR = "INSERT INTO sensor(name, location, " +
			"status, date_created, description, serial, isactive) VALUES (?, ?, ?, ?, ?, ?, ?)";


	/*
	 * Creates a new entry in the 'sensor_entry' table initialized at every 
	 * column with the provided values. This table indicates that a new event
	 * has occurred on a sensor. 
	 */
	private static final String ADD_SENSOR_EVENT = "INSERT INTO sensor_entry(sensor_id, " +
			"details, timestamp, key, date, time) VALUES (?, ?, ?, ?, ?, ? )";
	
	/*
	 * Creates a new entry in the 'sensor_follower' table initialized at 
	 * every column with the provided values. This table indicates which
	 * users will receive notification of changes in the particular sensor. 
	 */
	private static final String ADD_SENSOR_FOLLOWER = "INSERT INTO sensor_follower (sensor, " +
			" user, timestamp ) VALUES ( ?, ?, ?)";
	
	/*****************************************************
	 * Queries to update sensor data in the database
	 ****************************************************/

	/*
	 * Updates the 'status' column of the Sensor table entry whose 'serial'
	 * value matches the provided value.
	 */
	private static final String UPDATE_SENSOR_STATUS = "UPDATE sensor SET status = ? " +
			" WHERE serial = ?";




	/*****************************************************
	 * Queries to delete sensor data in the database
	 ****************************************************/

	/*
	 * Deletes entries in the Recover table whose key matches
	 * the provided value. 
	 */
	private static final String DELETE_SENSOR_EVENT = "DELETE FROM sensor_entry where key " +
			"= ?";
	
	
	private static final String DELETE_SENSOR_FOLLOWER = "DELETE FROM sensor_follower where " +
			"sensor = ? AND user = ?";
	
	/*****************************************************
	 * Queries to do multiple operations on sensor data 
	 * in the database
	 ****************************************************/




	/*****************************************************
	 * Other Instance Variables
	 ****************************************************/





	/**
	 * Basic Constructor of the SensorManager object.
	 */
	private SensorManager()
	{
		super();
	}

	/**
	 * This method ensures that no other active instance of the SensorManager
	 * is present. It returns the singleton instance of the manager object.
	 * @return
	 */
	public static synchronized final SensorManager getInstance(){
		return INSTANCE;

	}


	/* *************************************************************************
	 *
	 * INSERT METHODS
	 *
	 **************************************************************************/
	
	/**
	 * Creates a new entry in the 'Sensor_Follower' database table. 
	 * Fills each column of this table with the given parameters.
	 * If the query is executed correctly, this method returns true; false
	 * otherwise. 
	 * @param sensor Refers to the 'serial' column of the 'Sensor' database
	 * table. Used as a foreign key.
	 * @param user Refers to the 'email' column of the 'Account' database
	 * table. Used as a foreign key.
	 * @param timestamp When the user became follower of the sensor.
	 * @return A boolean indicating if the query was processed by the 
	 * database.
	 */
	public boolean addSensorFollower(String sensor, String user, String timestamp) {		

		ArrayList<Object> statements = new ArrayList<Object>();
		statements.add(sensor);
		statements.add(user);
		statements.add(timestamp);

		return DBManager.update(ADD_SENSOR_FOLLOWER, statements.toArray());
	}
	
	
	/**
	 * Creates a new entry in the 'Sensor' database table. 
	 * Fills each column of this table with the contents of the <Sensor> DTO.
	 * If the query is executed correctly, this method returns true; false
	 * otherwise. 
	 * @param account The <Sensor> DTO that will be added to the 'Sensor' 
	 * database table.
	 * @return A boolean indicating if the query was processed by the 
	 * database.
	 */
	public boolean addSensor(Sensor sensor) {		
		
		ArrayList<Object> statements = new ArrayList<Object>();
		statements.add(sensor.getName());
		statements.add(sensor.getLocation());
		statements.add(sensor.getStatus());
		statements.add(sensor.getDateCreated());
		statements.add(sensor.getDescription());
		statements.add(sensor.getSerial());
		statements.add(true);
		return DBManager.update(ADD_SENSOR, statements.toArray());
	}
	
	
	

	/* *************************************************************************
	 *
	 * UPDATE METHODS
	 *
	 **************************************************************************/

	
	/** Queries the 'Sensor' database table for all entries whose 'serial'
	 * column matches the provided serial value. Then, it updates the 'status'
	 * column with the 'status' parameter.
	 * @param status A value to update the 'status' column of the entries that
	 * match the 'serial' address provided
	 * @param sensor Refers to the 'Sensor' table's primary key: 'serial' column
	 * @return False if the query fails, true otherwise.
	 */
	public boolean updateSensorStatus(String status,String sensor){
		ArrayList<Object> expressions = new ArrayList<Object>();
		expressions.add(status);
		expressions.add(sensor);
		return (DBManager.update(UPDATE_SENSOR_STATUS, expressions.toArray()));
	}
	
	

	/* *************************************************************************
	 *
	 * DELETE METHODS
	 *
	 **************************************************************************/


	/**
	 * Invoked by the Indexer instance when the DirectoryWatcher reports
	 * and event of the type DELETE. The hash code provided as a parameter
	 * represents the hashed location of the sensor data file that was
	 * deleted from the monitored directory. Since no one document holds the
	 * same address, the hash is unique and refers to the 'key' column of 
	 * the "sensor_event" database table. 
	 * @param hashCode Alludes to an identifier to be compared to the 'key'
	 * column of the 'sensor_event" database table.
	 * @return A boolean stating whether the SQL query was executed.
	 */
	public boolean indexDeleteEvent(int hashCode)
	{
		ArrayList<Object> expressions = new ArrayList<Object>();
		expressions.add(hashCode+"");
		return DBManager.update(DELETE_SENSOR_EVENT, expressions.toArray());
	}
	
	
	public boolean deleteSensorFollower(String sensor, String email)
	{
		ArrayList<Object> expressions = new ArrayList<Object>();
		expressions.add(sensor);
		expressions.add(email);
		return DBManager.update(DELETE_SENSOR_FOLLOWER, expressions.toArray());
	}


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
	 * column of the 'Sensor_Follower' database' table.
	 * @return A boolean stating whether or not there is a matching entry in
	 *  the 'Sensor_Follower' table.
	 */
	public boolean isFollowing(String sensor, String email)
	{
		boolean isAvailable = false;
		ArrayList<Object> expressions = new ArrayList<Object>();
		expressions.add(sensor);
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
	 * Queries the 'Sensor' table of the database for the rows whose 'location'
	 * column matches the value of the parameter.
	 * @param query String representation of an attribute to be compared with
	 * the 'location' column of the database.
	 * @return An array list of <Sensor> filtered by 'location'. Empty if no entries
	 * are found or an error occurred.
	 */
	public ArrayList<Object> getSensorsByLocation(String query)
	{
		ArrayList<Object> list = new ArrayList<Object>();

		list.add(query);
		ResultSet rs = DBManager.execute(GET_SENSOR_BY_LOCATION, list.toArray());
		list.clear();
		try {
			while (rs.next())
			{							
				list.add(createSensorFromRS(rs));
			}
		} catch (Exception e ) {
			e.printStackTrace();
		}

		return list;		
	}
	
	/**
	 * Queries the 'Sensor' table of the database for the rows whose 'location'
	 * column matches the value of the parameter.
	 * @param query String representation of an attribute to be compared with
	 * the 'location' column of the database.
	 * @return An array list of <Sensor> filtered by 'name'. Empty if no entries
	 * are found or an error occurred.
	 */
	public ArrayList<Object> getSensorsByName(String query)
	{
		ArrayList<Object> list = new ArrayList<Object>();

		list.add(query);
		ResultSet rs = DBManager.execute(GET_SENSOR_BY_NAME, list.toArray());
		list.clear();
		try {
			while (rs.next())
			{							
				list.add(createSensorFromRS(rs));
			}
		} catch (Exception e ) {
			e.printStackTrace();
		}

		return list;		
	}
	
	/**
	 * Queries the 'Sensor' table of the database for the rows whose 'serial'
	 * column matches the value of the parameter.
	 * @param query String representation of an attribute to be compared with
	 * the 'serial' column of the database.
	 * @return An array list of <Sensor> filtered by 'serial'. Empty if no entries
	 * are found or an error occurred.
	 */
	public ArrayList<Object> getSensorsBySerial(String query)
	{
		ArrayList<Object> list = new ArrayList<Object>();

		list.add(query);
		ResultSet rs = DBManager.execute(GET_SENSOR_BY_SERIAL, list.toArray());
		list.clear();
		try {
			while (rs.next())
			{							
				list.add(createSensorFromRS(rs));
			}
		} catch (Exception e ) {
			e.printStackTrace();
		}

		return list;		
	}
	
	/**
	 * Queries the 'Sensor' table of the database for the rows whose 'status'
	 * column matches the value of the parameter.
	 * @param query String representation of an attribute to be compared with
	 * the 'status' column of the database.
	 * @return An array list of <Sensor> filtered by 'status'. Empty if no entries
	 * are found or an error occurred.
	 */
	public ArrayList<Object> getSensorsByStatus(String query)
	{
		ArrayList<Object> list = new ArrayList<Object>();

		list.add(query);
		ResultSet rs = DBManager.execute(GET_SENSOR_BY_STATUS, list.toArray());
		list.clear();
		try {
			while (rs.next())
			{							
				list.add(createSensorFromRS(rs));
			}
		} catch (Exception e ) {
			e.printStackTrace();
		}

		return list;		
	}
	
	/**
	 * Queries the 'Sensor_Follower' table of the database for the rows whose
	 * 'sensor' column matches the value of the parameter. This query returns
	 * only the 'user' column.
	 * @param query String representation of an attribute to be compared with
	 * the 'sensor' column of the database.
	 * @return An array list of <String> of all user accounts following the 
	 * sensor. 
	 */
	public ArrayList<String> getSensorFollowers(String sensor)
	{
		ArrayList<String> list = new ArrayList<String>();

		list.add(sensor);
		ResultSet rs = DBManager.execute(GET_SENSOR_FOLLOWERS, list.toArray());
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
	 * Queries the 'Sensor_Entry' table of the database for the rows whose 'sensor'
	 * column matches the value of the parameter. Results are ordered in accordance
	 * to the 'timestamp' <Date> column of the 'Sensor_Entry' table.
	 * @param query String representation of an attribute to be compared with
	 * the 'sensor' column of the database.
	 * @return An array list of <SensorMessages> masked by <Object>
	 * filtered by 'sensor'. Empty if no entries are found or an error occurred.
	 */
	public ArrayList<Object> getSensorMessages(String query)
	{
		ArrayList<Object> list = new ArrayList<Object>();

		list.add(query);
		ResultSet rs = DBManager.execute(GET_SENSOR_ENTRIES, list.toArray());
		list.clear();
		try {
			while (rs.next())
			{							
				list.add(createSensorMessageFromRS(rs));
			}
		} catch (Exception e ) {
			e.printStackTrace();
		}

		return list;		
	}

	/* *************************************************************************
	 *
	 * MULTIPLE OPERATION METHODS
	 *
	 **************************************************************************/

	/**
	 * This method indexes a sensor data file when the ADD event is registered 
	 * on the server's monitored directory. Each indexed file contains a hash
	 * of the file directory where it resides, this hash will be used as an 
	 * UNIQUE identifier in the 'sensor_entry' table of the database. The ADD
	 * event is indexed in the following manner:
	 * 		
	 * If the sensor exists within the database, identified by the sensor
	 * serial attribute, then a new entry is made to the database. The 
	 * hash belongs to a single file in the directory, thus there should
	 * be no problem when inserting a new entry on the 'sensor_entry' table.
	 * On the other hand, if the sensor does not exist, prior to creating
	 * the new entry, a new sensor entry is created in the 'Sensor' table
	 * of the database. By default new sensors created in this fashion, will
	 * have the 'isactive' column set to false.
	 * directory.  When a new 'sensor' entry is created, all administrators
	 * are notified through use of the NotificationManager. When a new entry
	 * is made on an existing 'sensor' then all 'Account' entries associated
	 * with this 'Sensor' object, are notified, again through use of the 
	 * NotificationManager.
	 *
	 *  @param eventData An array containing the sensor file's data. By default
	 *  the contents of this array by position is: name, location, status, serial,
	 *  date, time, and details. Status and Serial must not be empty | null.
	 *  @param hashCode A hash of the sensor file's directory, used as an unique
	 *  identifier for the 'Sensor_entry' database table's 'key' column.
	 *  @param timestamp Timestamp of when the event was registered.
	 */
	public void indexAddEvent(String[] eventData, int hashCode, String timestamp){
		/*
		 *  If the event data is null, there was a problem parsing
		 *  the data from a sensor file in the server's monitored
		 *  directory. See the project's web.xml for the location
		 *  of the monitored directory and verify that all required 
		 *  parameters have values. See the PropertyConfig Class in
		 *  the IO package of this system to learn which keys are 
		 *  required. No further action will be taken if eventData
		 *  is null.
		 */ 
		if (eventData==null) return;

		ArrayList<Object> expressions = new ArrayList<Object>();

		Connection connection = null;
		ResultSet rs;
		boolean isSensorCreated = false;
		Sensor sensor = null;

		try 
		{
			connection = DBManager.newConnection();
			//Verify sensor exists
			expressions.add(eventData[3]);
			rs = DBManager.execute(connection, GET_SENSOR_BY_SERIAL, expressions.toArray());
			expressions.clear();

			if(rs.next() && rs.isFirst() && rs.isLast())
			{
				sensor = createSensorFromRS(rs);
				isSensorCreated = true;

			}
			//Create sensor otherwise
			else
			{
				
				sensor = new Sensor(eventData[0].trim(), eventData[1].trim(), eventData[2].trim(),
						timestamp, "",eventData[3].trim(), false);
				expressions.add(sensor.getName());
				expressions.add(sensor.getLocation());
				expressions.add(sensor.getStatus());
				expressions.add(sensor.getDateCreated());
				expressions.add("");
				expressions.add(sensor.getSerial());
				expressions.add(false);
				isSensorCreated = DBManager.update(connection, ADD_SENSOR,
						expressions.toArray());
				expressions.clear();
				System.out.println("Added Sensor: "+ isSensorCreated);
				// IMPLEMENT NOTIFICATION SENDER HERE.
			}
			//Only register the 'sensor_entry' if Sensor exists.
			if(isSensorCreated)
			{
				expressions.add(eventData[3].trim()); // Sensor serial
				expressions.add(eventData[6].trim()); // Event details
				expressions.add(timestamp.trim()); // When the file was added to server
				expressions.add(hashCode);     // Hash of file location in directory
				expressions.add(eventData[4].trim()); // Date of event
				expressions.add(eventData[5].trim()); // Time of event
				if(DBManager.update(connection, ADD_SENSOR_EVENT, expressions.toArray()))
				{
					//End connection and get ready for sending notifications
					//and emails.
					connection.close();
					System.out.println("Added Sensor event: "+ true);
					ThreadPoolManager.getInstance().getThreadPoolExecutor().execute(
							NotificationManager.getInstance()
							.sendAsyncSensorNotification(sensor.getSerial(), timestamp,
								"Status: "+eventData[2]+ " Details: "+eventData[6]));
				}
				
				//Update sensor status
				SensorManager.getInstance().updateSensorStatus(eventData[2],
						sensor.getSerial());
			}

		} 

		catch (ClassNotFoundException | SQLException e)
		{		
			e.printStackTrace();
		}
		finally{
			//Throws Exception if the connection never started, which doesn't 
			//matter at this point.
			try { connection.close();}
			catch (SQLException e) {}
		}

	}


	

	/* *************************************************************************
	 *
	 * UTILITY METHODS
	 *
	 **************************************************************************/


	/**
	 * Utility method to add the result set columns to a sensor object in the respective
	 * instance fields.
	 * @param rs The result set returned from the data base query
	 * @throws Exception  Occurs when an incorrect index is selected and does not 
	 * match the Sensor object types.
	 * @return An Sensor object with the information of the DB, null if an exception 
	 * occurs.
	 */
	private Sensor createSensorFromRS(ResultSet rs) {
		Sensor result = null;
		try {
			result = new Sensor(rs.getString(1).trim(), rs.getString(2).trim(),
					rs.getString(3).trim(), rs.getString(4), rs.getString(5),
					rs.getString(6).trim(), rs.getBoolean(7));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Utility method to add the result set columns to an <SensorMessage> object
	 * in the respective instance fields.
	 * @param rs The result set returned from the data base query
	 * @throws Exception  Occurs when an incorrect index is selected and does not 
	 * match the <SensorMessage> object types.
	 * @return An <SensorMessage> object with the information of the DB, null if
	 * an exception occurs.
	 */
	private SensorMessage createSensorMessageFromRS(ResultSet rs) {
		SensorMessage result = null;
		try {
			result = new SensorMessage(rs.getString(1), rs.getString(2),
					rs.getString(3), rs.getString(4), rs.getString(5), 
					rs.getString(6));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}


}
