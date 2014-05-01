package com.dhs.portglass.dto;


/**
 * This POJO is used to map the 'Account_Notification database 
 * table result set into an object that can be utilized to manage
 * all notifications having to do with user accounts. It encapsulates 
 * all account notification data utilized by the system.
 * @author Manuel R Saldana
 *
 */
public class AccountNotification {
	
	// Database table parameters
		private String user;
		private String solicitor;
		private boolean seen;
		private String timestamp;
		private String type;
		private int id;
		
		/**
		 * Default Constructor
		 */
		public AccountNotification(){
			super();
		}
		
		public AccountNotification(String user, String solicitor, boolean seen,
				String timestamp, String type, int id){
			this.user=user;
			this.solicitor=solicitor;
			this.seen=seen;
			this.timestamp=timestamp;
			this.type= type;
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
		 * @return the solicitor
		 */
		public String getSolicitor() {
			return solicitor;
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
		 * @return the type
		 */
		public String getType() {
			return type;
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
		 * @param solicitor the solicitor to set
		 */
		public void setSolicitor(String solicitor) {
			this.solicitor = solicitor;
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
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}
		
		/**
		 * 
		 * @param id the id to set
		 */
		public void setID(int id){
			this.id=id;
		}
		

}
