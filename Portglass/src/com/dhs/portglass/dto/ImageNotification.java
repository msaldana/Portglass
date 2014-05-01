package com.dhs.portglass.dto;

/**
 * This POJO is used to map the 'Image_Notification database 
 * table result set into an object that can be utilized to manage
 * all notifications having to do with images. It encapsulates 
 * all image notification data utilized by the system.
 * @author Manuel R Saldana
 *
 */
public class ImageNotification {
	
	// Database table parameters
			private String user;
			private String image;
			private boolean seen;
			private String message;
			private String timestamp;
			private int id;
			
			/**
			 * Basic Constructor
			 */
			public ImageNotification(){
				super();
			}
			
			public ImageNotification(String user, String image, boolean seen,
					String message, String timestamp, int id){
				this.user = user;
				this.image = image;
				this.seen = seen;
				this.message = message;
				this.timestamp=timestamp;
				this.id = id;
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
			 * @return the image
			 */
			public String getImage() {
				return image;
			}

			/**
			 * @return the seen
			 */
			public boolean isSeen() {
				return seen;
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
			public String getTimestamp() {
				return timestamp;
			}
			
			/**
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
			 * @param image the image to set
			 */
			public void setImage(String image) {
				this.image = image;
			}

			/**
			 * @param seen the seen to set
			 */
			public void setSeen(boolean seen) {
				this.seen = seen;
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
			public void setTimestamp(String timestamp) {
				this.timestamp = timestamp;
			}
			
			/**
			 * 
			 * @param id the id to set
			 */
			public void setID(int id){
				this.id=id;
			}

}
