package com.dhs.portglass.dto;


/**
 * This POJO is used to map the 'Sensor_Notification database 
 * table result set into an object that can be utilized to manage
 * all notifications having to do with sensor units. It encapsulates 
 * all sensor notification data utilized by the system.
 * @author Manuel R Saldana
 *
 */
public class SensorNotification {
	
	// Database table parameters
			private String user;
			private String sensor;
			private boolean seen;
			private String timestamp;
			private String message;
			private int id;
			
			public SensorNotification(){
				super();
			}
			
			public SensorNotification(String user, String sensor, boolean seen,
					String timestamp, String message, int id){
				this.user=user;
				this.sensor=sensor;
				this.seen=seen;
				this.timestamp=timestamp;
				this.message=message;
				this.id=id;
			}
			
			/*----------------------------------------------------------
			 * Getter Methods
			 *----------------------------------------------------------
			 */

			/**
			 * @return the user
			 */
			public String getUser() {
				return user;
			}

			/**
			 * @return the sensor
			 */
			public String getSensor() {
				return sensor;
			}

			/**
			 * @return the seen
			 */
			public boolean isSeen() {
				return seen;
			}

			/**
			 * @return the timestamp
			 */
			public String getTimestamp() {
				return timestamp;
			}

			/**
			 * @return the message
			 */
			public String getMessage() {
				return message;
			}
			
			/**
			 * 
			 * @return the id
			 */
			public int getID(){
				return id;
			}

			/*----------------------------------------------------------
			 * Setter Methods
			 *----------------------------------------------------------
			 */
			
			/**
			 * @param user the user to set
			 */
			public void setUser(String user) {
				this.user = user;
			}

			/**
			 * @param sensor the sensor to set
			 */
			public void setSensor(String sensor) {
				this.sensor = sensor;
			}

			/**
			 * @param seen the seen to set
			 */
			public void setSeen(boolean seen) {
				this.seen = seen;
			}

			/**
			 * @param timestamp the timestamp to set
			 */
			public void setTimestamp(String timestamp) {
				this.timestamp = timestamp;
			}

			/**
			 * @param message the message to set
			 */
			public void setMessage(String message) {
				this.message = message;
			}
			
			/**
			 * 
			 * @param id the id to set
			 */
			public void setID(int id){
				this.id = id;
			}

}
