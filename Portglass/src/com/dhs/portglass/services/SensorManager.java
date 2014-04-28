package com.dhs.portglass.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
	 * Retrieves a sensor entry from the 'Sensor' database table 
	 * through use of the unique identifier 'serial' column.
	 */
	private static final String GET_SENSOR = "SELECT * FROM Sensor WHERE serial = ? ";


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
	/*****************************************************
	 * Queries to update sensor data in the database
	 ****************************************************/




	/*****************************************************
	 * Queries to delete sensor data in the database
	 ****************************************************/

	/*
	 * Deletes entries in the Recover table whose key matches
	 * the provided value. 
	 */
	private static final String DELETE_SENSOR_EVENT = "DELETE FROM sensor_entry where key " +
			"= ?";
	
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


	/* *************************************************************************
	 *
	 * SELECT METHODS
	 *
	 **************************************************************************/

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
	 *  @param modifiedTime Timestamp of when the event was registered.
	 */
	public void indexAddEvent(String[] eventData, int hashCode, long modifiedTime){
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
			rs = DBManager.execute(connection, GET_SENSOR, expressions.toArray());
			expressions.clear();

			if(rs.next() && rs.isFirst() && rs.isLast())
			{
				sensor = createSensorFromRS(rs);
				isSensorCreated = true;

			}
			//Create sensor otherwise
			else
			{
				sensor = new Sensor(eventData[0], eventData[1], eventData[2],
						System.currentTimeMillis(), "",eventData[3], false);
				expressions.add(sensor.getName());
				expressions.add(sensor.getLocation());
				expressions.add(sensor.getStatus());
				expressions.add(sensor.getDate_created());
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
				expressions.add(eventData[3]); // Sensor serial
				expressions.add(eventData[6]); // Event details
				expressions.add(modifiedTime); // When the file was added to server
				expressions.add(hashCode);     // Hash of file location in directory
				expressions.add(eventData[4]); // Date of event
				expressions.add(eventData[5]); // Time of event
				if(DBManager.update(connection, ADD_SENSOR_EVENT, expressions.toArray()))
				{
					System.out.println("Added Sensor event: "+ true);
					//IMPLEMENT NOTIFICATION SENDER HERE
				}
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
					rs.getString(3).trim(), rs.getLong(4), rs.getString(5),
					rs.getString(6).trim(), rs.getBoolean(7));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}


}
