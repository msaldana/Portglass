package com.dhs.portglass.dto;

/**
 * This POJO is used to map the Sensor database table result set
 * into an object that can be utilized to manage an user account.
 * It encapsulates all sensor data utilized by the system.
 * @author Manuel R Saldana
 *
 */
public class Sensor {

	// Database table parameters
		private String name;
		private String location;
		private String status;
		private long date_created;
		private String description;
		private String serial;
		private boolean isActive;
		
		/**
		 * Default Constructor
		 */
		public Sensor(){}
		
		public Sensor(String name, String location, String status,
				long date_created, String description,
				String serial, boolean isActive){
			this.name = name;
			this.location = location;
			this.status = status;
			this.date_created = date_created;
			this.description = description;
			this.serial = serial;
			this.isActive = isActive;
			
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the location
		 */
		public String getLocation() {
			return location;
		}

		/**
		 * @return the status
		 */
		public String getStatus() {
			return status;
		}

		/**
		 * @return the date_created
		 */
		public long getDate_created() {
			return date_created;
		}

		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @return the serial
		 */
		public String getSerial() {
			return serial;
		}

		/**
		 * @return the isActive
		 */
		public boolean isActive() {
			return isActive;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @param location the location to set
		 */
		public void setLocation(String location) {
			this.location = location;
		}

		/**
		 * @param status the status to set
		 */
		public void setStatus(String status) {
			this.status = status;
		}

		/**
		 * @param date_created the date_created to set
		 */
		public void setDate_created(long date_created) {
			this.date_created = date_created;
		}

		/**
		 * @param description the description to set
		 */
		public void setDescription(String description) {
			this.description = description;
		}

		/**
		 * @param serial the serial to set
		 */
		public void setSerial(String serial) {
			this.serial = serial;
		}

		/**
		 * @param isActive the isActive to set
		 */
		public void setActive(boolean isActive) {
			this.isActive = isActive;
		}
		
		
}
