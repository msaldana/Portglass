package com.dhs.portglass.dto;

import java.sql.Date;


/**
 * This POJO is used to map the 'image_entry' database table result 
 * set into an object that can be utilized to manage an image message.
 * It encapsulates all image message data utilized by the system.
 * @author Manuel R Saldana
 *
 */
public class ImageMessage {
	
	// Database table parameters
		private String author;
		private String image;
		private String message;
		private Date timestamp;
		
		/**
		 * Default Constructor
		 */
		public ImageMessage() {
			super();
		}
		
		public ImageMessage(String author, String image, String message,
				Date timestamp)
		{
			this.author = author;
			this.image = image;
			this.message = message;
			this.timestamp = timestamp;
		}
		
		
		/*----------------------------------------------------------
		 * Getter Methods
		 *----------------------------------------------------------
		 */

		/**
		 * @return the author
		 */
		public String getAuthor() {
			return author;
		}

		/**
		 * @return the image
		 */
		public String getImage() {
			return image;
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * @return the timestamp
		 */
		public Date getTimestamp() {
			return timestamp;
		}

		/*----------------------------------------------------------
		 * Setter Methods
		 *----------------------------------------------------------
		 */
		
		/**
		 * @param author the author to set
		 */
		public void setAuthor(String author) {
			this.author = author;
		}

		/**
		 * @param image the image to set
		 */
		public void setImage(String image) {
			this.image = image;
		}

		/**
		 * @param message the message to set
		 */
		public void setMessage(String message) {
			this.message = message;
		}

		/**
		 * @param timestamp the timestamp to set
		 */
		public void setTimestamp(Date timestamp) {
			this.timestamp = timestamp;
		}

		
}
