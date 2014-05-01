package com.dhs.portglass.dto;


/**
 * This POJO is used to map the 'sensor_entry' database table result 
 * set into an object that can be utilized to manage an image message.
 * It encapsulates all sensor message data utilized by the system.
 * @author Manuel R Saldana
 *
 */
public class SensorMessage {
	
	// Database table parameters
		private String sensor;
		private String details;
		private String reportedDate;
		private String fileHash;
		private String eventDate;
		private String eventTime;
		
		
		/**
		 * Default Constructor
		 */
		public SensorMessage() {
			super();
		}
		
		public SensorMessage(String sensor, String details, String reportedDate,
				String fileHash, String eventDate, String eventTime)
		{
			this.sensor = sensor;
			this.details = details;
			this.reportedDate = reportedDate;
			this.fileHash = fileHash;
			this.eventDate = eventDate;
			this.eventTime = eventTime;
		}

		/*----------------------------------------------------------
		 * Getter Methods
		 *----------------------------------------------------------
		 */
		
		public String getSensor() {
			return sensor;
		}

		public void setSensor(String sensor) {
			this.sensor = sensor;
		}

		public String getDetails() {
			return details;
		}

		public void setDetails(String details) {
			this.details = details;
		}

		public String getReportedDate() {
			return reportedDate;
		}

		public void setReportedDate(String reportedDate) {
			this.reportedDate = reportedDate;
		}

		public String getFileHash() {
			return fileHash;
		}

		public void setFileHash(String fileHash) {
			this.fileHash = fileHash;
		}

		public String getEventDate() {
			return eventDate;
		}
		
		/*----------------------------------------------------------
		 * Setter Methods
		 *----------------------------------------------------------
		 */

		public void setEventDate(String eventDate) {
			this.eventDate = eventDate;
		}

		public String getEventTime() {
			return eventTime;
		}

		public void setEventTime(String eventTime) {
			this.eventTime = eventTime;
		}
		
		
	

		

		
}
