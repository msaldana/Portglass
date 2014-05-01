package com.dhs.portglass.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dhs.portglass.dto.Account;
import com.dhs.portglass.dto.AccountNotification;
import com.dhs.portglass.dto.ImageNotification;
import com.dhs.portglass.dto.SensorNotification;
import com.dhs.portglass.server.DBManager;



/**
 * Manages logic for creating, updating, and/or deleting entries 
 * in the notification database tables. For the core version of
 * Portglass, this includes: 'Account_Notification', 'Image_Notification',
 * and 'Sensor_Notification'. It is a synchronized singleton
 * class, accessible through the getInstance() method. Data is
 * transfered to and through this class in the form of <AccountNotification>,
 * <ImageNotification>, or <SensorNotification> POJOs. 
 * 
 * Note: These POJOs by default map to tables that have the same
 * column types, so they could be generalized to a generic <Notification>
 * object. However, they where implemented as separate POJOs in case
 * future upgrades are made on the system; changing the columns of
 * each of these notification types.
 * 
 * @author Manuel R Saldana
 *
 */
public class NotificationManager {

	/*****************************************************
	 * Queries to retrieve Image data from database
	 ****************************************************/
	/*
	 * Retrieve all entries from the 'Account_Notification' database table whose 
	 * 'user' column matches the provided value. These results will
	 * be sorted by descending order of the <Date> 'timestamp' column.
	 */
	private static final String GET_ACCOUNT_NOTIFICATIONS = "SELECT * FROM account_notification WHERE " +
			" (user = ? AND seen = false) ORDER BY timestamp DESC ";
	/*
	 * Retrieve all entries from the 'Image_Notification' database table whose 
	 * 'image' column matches the provided value. These results will
	 * be sorted by descending order of the <Date> 'timestamp' column.
	 */
	private static final String GET_IMAGE_NOTIFICATIONS = "SELECT * FROM image_notification WHERE " +
			" (user = ? AND seen = false) ORDER BY timestamp DESC ";
	
	/*
	 * Retrieve all entries from the 'Sensor_Notification' database table whose 
	 * 'sensor' column matches the provided value. These results will
	 * be sorted by descending order of the <Date> 'timestamp' column.
	 */
	private static final String GET_SENSOR_NOTIFICATIONS = "SELECT * FROM sensor_notification WHERE " +
			" (user = ? AND seen = false) ORDER BY timestamp DESC ";
	

	/*****************************************************
	 * Queries to create Image data in the database
	 ****************************************************/

	/*
	 * Creates a new entry in the 'Account_Notification' table initialized
	 * at every column with the provided values.
	 */
	private static final String ADD_ACCOUNT_NOTIFICATION = "INSERT INTO " +
			" account_notification VALUES (?, ?, ?, ?, ?)";
	
	/*
	 * Creates a new entry in the 'Image_Notification' table initialized
	 * at every column with the provided values.
	 */
	private static final String ADD_IMAGE_NOTIFICATION = "INSERT INTO " +
			" image_notification VALUES (?, ?, ?, ?, ?)";

	/*
	 * Creates a new entry in the 'Sensor_Notification' table initialized
	 * at every column with the provided values.
	 */
	private static final String ADD_SENSOR_NOTIFICATION = "INSERT INTO " +
			" sensor_notification VALUES (?, ?, ?, ?, ?)";






	/*****************************************************
	 * Queries to update Image data in the database
	 ****************************************************/


	private static final String UPDATE_ACCOUNT_NOTIFICATION_ATTEND = 
			"UPDATE account_notification SET seen = true WHERE (solicitor = ? " +
					" AND type = ?)";

	private static final String UPDATE_ACCOUNT_NOTIFICATION_DISMISS = 
			"UPDATE account_notification SET seen = true WHERE id = ? ";
	
	private static final String UPDATE_IMAGE_NOTIFICATION_DISMISS = 
			"UPDATE image_notification SET seen = true WHERE id = ? ";
	
	private static final String UPDATE_SENSOR_NOTIFICATION_DISMISS = 
			"UPDATE sensor_notification SET seen = true WHERE id = ? ";



	/*****************************************************
	 * Queries to delete notification data in the database
	 ****************************************************/

	/*****************************************************
	 * Other Instance Variables
	 ****************************************************/

	// Create a singleton instance for the mail manager class.
	private static final NotificationManager singleton = new NotificationManager();


	/**
	 * Returns the singleton instance of MailManager.
	 * @return
	 */
	public static final NotificationManager getInstance(){
		return singleton;
	}


	/* *************************************************************************
	 *
	 * INSERT METHODS
	 *
	 **************************************************************************/

	/**
	 * Queries the database, through the use of the <AccountManager>, for all
	 * active user accounts of the type 'admin'. To each of these users, a
	 * notification entry is made on the 'Account_Notification' database 
	 * table. As each entry is processed, the <ThreadPoolManager> is invoked
	 * to send a notification to registered devices of the administrator.
	 * @param username Refers to the 'email' column value of the 'Account'
	 * database table of the user that is requesting action.
	 * @param timestamp String representation of date the event took place.
	 * @param type String representation of the type of request.
	 * @return
	 */
	private void addAccountNotification(String username, String timestamp,
			String type){
		
		/* Get all Active Administrators */
		ArrayList<Object> list = AccountManager.getInstance().getAdministratorList();
		ArrayList<Object> expressions = new ArrayList<Object>();

		/* Create a notification for all adminitrators */
		for (int i=0; i<list.size(); i++){				
			expressions.clear();
			expressions.add(((Account)list.get(i)).getEmail()); //Admin username
			expressions.add(username); //Requester's username
			expressions.add(false);//Has been attended
			expressions.add(timestamp);//Date of request
			expressions.add(type);//Type of request
			/* Send Notification to email/devices if added to system */
			if(DBManager.update(ADD_ACCOUNT_NOTIFICATION, 
					expressions.toArray())){
				System.out.println("added sensor notification for:"+((Account)list.get(i)).getEmail());
				//Send device notification.
			}
		}
	}
	
	
	/**
	 * Queries the database, through the use of the <SensorManager>, for all
	 * active user accounts who follow the given sensor. To each of these users, a
	 * notification entry is made on the 'Sensor_Notification' database 
	 * table. As each entry is processed, the <ThreadPoolManager> is invoked
	 * to send a notification to registered devices of the user.
	 * @param sensor Refers to the 'serial' column value of the 'Sensor'
	 * database table of the sensor whose properties have changed.
	 * @param timestamp String representation of date the event took place.
	 * @param type String representation of the message of the notification.
	 */
	private void addImageNotification(String image, String timestamp,
			String message){
		
		/* Get all Image Followers */
		ArrayList<String> list = ImageManager.getInstance().getImageFollowers(image);
		ArrayList<Object> expressions = new ArrayList<Object>();

		/* Create a notification for all image followers */
		for (int i=0; i<list.size(); i++){				
			expressions.clear();
			expressions.add(list.get(i)); //Image follower username
			expressions.add(image); //Requester's username
			expressions.add(false);//Has been attended
			expressions.add(timestamp);//Date of request
			expressions.add(message);//Type of request
			/* Send Notification to email/devices if added to system */
			if(DBManager.update(ADD_IMAGE_NOTIFICATION, 
					expressions.toArray())){
				System.out.println("added image notification for:"+(list.get(i)));
				//Send device notification.
			}
		}
	}

	/**
	 * Queries the database, through the use of the <SensorManager>, for all
	 * active user accounts who follow the given sensor. To each of these users, a
	 * notification entry is made on the 'Sensor_Notification' database 
	 * table. As each entry is processed, the <ThreadPoolManager> is invoked
	 * to send a notification to registered devices of the user.
	 * @param sensor Refers to the 'serial' column value of the 'Sensor'
	 * database table of the sensor whose properties have changed.
	 * @param timestamp String representation of date the event took place.
	 * @param type String representation of the message of the notification.
	 */
	private void addSensorNotification(String sensor, String timestamp,
			String message){
		
		/* Get all Active Administrators */
		ArrayList<String> list = SensorManager.getInstance().getSensorFollowers(sensor);
		ArrayList<Object> expressions = new ArrayList<Object>();

		/* Create a notification for all sensor followers */
		for (int i=0; i<list.size(); i++){				
			expressions.clear();
			expressions.add(list.get(i)); //Sensor follower username
			expressions.add(sensor); //Requester's username
			expressions.add(false);//Has been attended
			expressions.add(timestamp);//Date of request
			expressions.add(message);//Type of request
			/* Send Notification to email/devices if added to system */
			if(DBManager.update(ADD_SENSOR_NOTIFICATION, 
					expressions.toArray())){
				System.out.println("added notification for:"+(list.get(i)));
				//Send device notification.
			}
		}
	}


	/* *************************************************************************
	 *
	 * UPDATE METHODS
	 *
	 **************************************************************************/

	/**
	 * Queries the 'Account_Notification' database table for all entries 
	 * whose 'solicitor' and 'type' columns matches the provided values. 
	 * Then, it updates the query matches' 'seen' column to the yield
	 * the boolean value 'true'. This is used so that when an administrator
	 * attends a case, it is closed and removed from the pending notifications
	 * of all Administrators. 
	 * @param solicitor Refers to the 'Account_Notification' table's 'user' column.
	 * It is a foreign key to the 'Account' table's 'email' column. This is the 
	 * user who issued the notification.
	 * @param type Refers to the 'Account_Notification' table's 'type' column.
	 * It is the type of request being issued by an user. For instance, 
	 * 'register' is a valid type, indicating that the user is solicitating 
	 * access to the Portglass System.
	 * @return False if the query fails, true otherwise.
	 */
	public boolean updateAccountNotificationAttend(String solicitor,
			String type){
		ArrayList<Object> expressions = new ArrayList<Object>();
		expressions.add(solicitor);
		expressions.add(type);
		return (DBManager.update(UPDATE_ACCOUNT_NOTIFICATION_ATTEND, expressions.toArray()));
	}


	/**
	 * Queries the 'Account_Notification' database table for all entries 
	 * whose 'id'' columns matches the provided values. Then, it updates the
	 * query matches' 'seen' column to the yield the boolean value 'true'.
	 * @param user Refers to the 'Account_Notification' table's 'id' column.
	 * @return False if the query fails, true otherwise.
	 */
	public boolean updateAccountNotificationDismiss(int id){
		ArrayList<Object> expressions = new ArrayList<Object>();
		expressions.add(id);
		return (DBManager.update(UPDATE_ACCOUNT_NOTIFICATION_DISMISS, expressions.toArray()));
	}
	
	/**
	 * Queries the 'Image_Notification' database table for all entries 
	 * whose 'id'' columns matches the provided values. Then, it updates the
	 * query matches' 'seen' column to the yield the boolean value 'true'.
	 * @param user Refers to the 'Image_Notification' table's 'id' column.
	 * @return False if the query fails, true otherwise.
	 */
	public boolean updateImageNotificationDismiss(int id){
		ArrayList<Object> expressions = new ArrayList<Object>();
		expressions.add(id);
		return (DBManager.update(UPDATE_IMAGE_NOTIFICATION_DISMISS, expressions.toArray()));
	}
	
	/**
	 * Queries the 'Sensor_Notification' database table for all entries 
	 * whose 'id'' columns matches the provided values. Then, it updates the
	 * query matches' 'seen' column to the yield the boolean value 'true'.
	 * @param user Refers to the 'Sensor_Notification' table's 'id' column.
	 * @return False if the query fails, true otherwise.
	 */
	public boolean updateSensorNotificationDismiss(int id){
		ArrayList<Object> expressions = new ArrayList<Object>();
		expressions.add(id);
		return (DBManager.update(UPDATE_SENSOR_NOTIFICATION_DISMISS, expressions.toArray()));
	}


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
	 * Queries the 'Account_Notification' table of the database for the rows whose 'seen'
	 * is false and whose 'user' column matches the given parameter. Results are ordered 
	 * in accordance to the 'timestamp' <Date> column of the 'Account_Notification' table.
	 * @param query String representation of an attribute to be compared with
	 * the 'user' column of the database. Foreign key to 'email' in the 'Account' table.
	 * @return An array list of <AccountNotifications> masked by <Object>
	 * filtered by 'user' and 'seen'=true. Empty if no entries are found or an error occurred.
	 */
	public ArrayList<Object> getAccountNotifications(String user)
	{
		ArrayList<Object> list = new ArrayList<Object>();

		list.add(user);
		ResultSet rs = DBManager.execute(GET_ACCOUNT_NOTIFICATIONS, list.toArray());
		list.clear();
		try {
			while (rs.next())
			{							
				list.add(createAccountNotificationFromRS(rs));
			}
		} catch (Exception e ) {
			e.printStackTrace();
		}

		return list;		
	}
	
	/**
	 * Queries the 'Image_Notification' table of the database for the rows whose 'seen'
	 * is false and whose 'user' column matches the given parameter. Results are ordered 
	 * in accordance to the 'timestamp' <Date> column of the 'Image_Notification' table.
	 * @param query String representation of an attribute to be compared with
	 * the 'user' column of the database. Foreign key to 'email' in the 'Account' table.
	 * @return An array list of <ImageNotifications> masked by <Object>
	 * filtered by 'user' and 'seen'=true. Empty if no entries are found or an error occurred.
	 */
	public ArrayList<Object> getImageNotifications(String user)
	{
		ArrayList<Object> list = new ArrayList<Object>();

		list.add(user);
		ResultSet rs = DBManager.execute(GET_IMAGE_NOTIFICATIONS, list.toArray());
		list.clear();
		try {
			while (rs.next())
			{							
				list.add(createImageNotificationFromRS(rs));
			}
		} catch (Exception e ) {
			e.printStackTrace();
		}

		return list;		
	}
	
	/**
	 * Queries the 'Sensor_Notification' table of the database for the rows whose 'seen'
	 * is false and whose 'user' column matches the given parameter. Results are ordered 
	 * in accordance to the 'timestamp' <Date> column of the 'Sensor_Notification' table.
	 * @param query String representation of an attribute to be compared with
	 * the 'user' column of the database. Foreign key to 'email' in the 'Account' table.
	 * @return An array list of <SensorNotifications> masked by <Object>
	 * filtered by 'user' and 'seen'=true. Empty if no entries are found or an error occurred.
	 */
	public ArrayList<Object> getSensorNotifications(String user)
	{
		ArrayList<Object> list = new ArrayList<Object>();

		list.add(user);
		ResultSet rs = DBManager.execute(GET_SENSOR_NOTIFICATIONS, list.toArray());
		list.clear();
		try {
			while (rs.next())
			{							
				list.add(createSensorNotificationFromRS(rs));
			}
		} catch (Exception e ) {
			e.printStackTrace();
		}

		return list;		
	}

	/* *************************************************************************
	 *
	 * ASYNCHRONOUS METHODS
	 *
	 **************************************************************************/
	/**
	 * Returns a <Runnable> object to send Account Notifications to all
	 * administrators using the addAccountNotification method.
	 * @param user Account object that belongs to new user
	 * @return Runnable instance to run in new thread.
	 */
	public Runnable sendAsyncAccountNotification(String requester, String timestamp, 
			String type){
		return(new AsyncAccountNotification(requester, timestamp, type));
	}
	
	/**
	 * Returns a <Runnable> object to send Sensor Notifications to all
	 * sensor followers using the addSensorNotification method.
	 * @param user Account object that belongs to new user
	 * @return Runnable instance to run in new thread.
	 */
	public Runnable sendAsyncImageNotification(String image, String timestamp, 
			String message){
		return(new AsyncImageNotification(image, timestamp, message));
	}
	
	/**
	 * Returns a <Runnable> object to send Sensor Notifications to all
	 * sensor followers using the addSensorNotification method.
	 * @param user Account object that belongs to new user
	 * @return Runnable instance to run in new thread.
	 */
	public Runnable sendAsyncSensorNotification(String sensor, String timestamp, 
			String message){
		return(new AsyncSensorNotification(sensor, timestamp, message));
	}


	/* *************************************************************************
	 *
	 * ASYNCHRONOUS SUB CLASSES
	 *
	 **************************************************************************/

	/**
	 * Utility Class to create and send account notifications Asynchronously.
	 * @author Manuel R Saldana Pueyo
	 *
	 */
	private class AsyncAccountNotification implements Runnable{

		private String requester;
		private String timestamp;
		private String type;

		public AsyncAccountNotification(String requester, String timestamp,
				String type){
			this.requester = requester;
			this.timestamp = timestamp;
			this.type=type;
		}

		@Override
		/**
		 * Used to create notifications in a different thread than the that of the 
		 * main application. 
		 */
		public void run() {

			getInstance().addAccountNotification(requester, timestamp,
					type);
		}
	}
	
	
	/**
	 * Utility Class to create and send image notifications Asynchronously.
	 * @author Manuel R Saldana Pueyo
	 *
	 */
	private class AsyncImageNotification implements Runnable{

		private String image;
		private String timestamp;
		private String message;

		public AsyncImageNotification(String image, String timestamp,
				String message){
			this.image = image;
			this.timestamp = timestamp;
			this.message = message ;
		}

		@Override
		/**
		 * Used to create notifications in a different thread than the that of the 
		 * main application. 
		 */
		public void run() {

			getInstance().addImageNotification(image, timestamp,
					message);
		}
	}
	
	/**
	 * Utility Class to create and send sensor notifications Asynchronously.
	 * @author Manuel R Saldana Pueyo
	 *
	 */
	private class AsyncSensorNotification implements Runnable{

		private String sensor;
		private String timestamp;
		private String message;

		public AsyncSensorNotification(String sensor, String timestamp,
				String message){
			this.sensor = sensor;
			this.timestamp = timestamp;
			this.message = message ;
		}

		@Override
		/**
		 * Used to create notifications in a different thread than the that of the 
		 * main application. 
		 */
		public void run() {

			getInstance().addSensorNotification(sensor, timestamp,
					message);
		}
	}
	
	
	

	/* *************************************************************************
	 *
	 * UTILITY METHODS
	 *
	 **************************************************************************/

	/**
	 * Utility method to add the result set columns to an <AccountNotification> object
	 * in the respective instance fields.
	 * @param rs The result set returned from the data base query
	 * @throws Exception  Occurs when an incorrect index is selected and does not 
	 * match the <AccountNotification> object types.
	 * @return An <AccountNotification> object with the information of the DB, null if
	 * an exception occurs.
	 */
	private AccountNotification createAccountNotificationFromRS(ResultSet rs) {
		AccountNotification result = null;
		try {
			result = new AccountNotification(rs.getString(1), rs.getString(2),
					rs.getBoolean(3), rs.getString(4), rs.getString(5), rs.getInt(6));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * Utility method to add the result set columns to an <ImageNotification> object
	 * in the respective instance fields.
	 * @param rs The result set returned from the data base query
	 * @throws Exception  Occurs when an incorrect index is selected and does not 
	 * match the <ImageNotification> object types.
	 * @return An <ImageNotification> object with the information of the DB, null if
	 * an exception occurs.
	 */
	private ImageNotification createImageNotificationFromRS(ResultSet rs) {
		ImageNotification result = null;
		try {
			result = new ImageNotification(rs.getString(1), rs.getString(2),
					rs.getBoolean(3), rs.getString(4), rs.getString(5), rs.getInt(6));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}


	/**
	 * Utility method to add the result set columns to an <SensorNotification> object
	 * in the respective instance fields.
	 * @param rs The result set returned from the data base query
	 * @throws Exception  Occurs when an incorrect index is selected and does not 
	 * match the <SensorNotification> object types.
	 * @return An <SensorNotification> object with the information of the DB, null if
	 * an exception occurs.
	 */
	private SensorNotification createSensorNotificationFromRS(ResultSet rs) {
		SensorNotification result = null;
		try {
			result = new SensorNotification(rs.getString(1), rs.getString(2),
					rs.getBoolean(3), rs.getString(4), rs.getString(5), rs.getInt(6));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

	public static void main(String[] args){
		/*/Current time
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		String timestamp = dateFormat.format(date);
		System.out.println("."+timestamp+".");
		ThreadPoolManager.getInstance().getThreadPoolExecutor().execute(
		getInstance().sendAsyncAccountNotification("manuel.saldana@upr.edu", timestamp, "register"));
		//Dismissing notification/*/
		//System.out.println(getInstance().updateAccountNotificationAttend("manuel.saldana@upr.edu","register"));
	}

}
