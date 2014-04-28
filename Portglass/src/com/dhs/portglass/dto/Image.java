package com.dhs.portglass.dto;

/**
 * This POJO is used to map the Image database table result set
 * into an object that can be utilized to manage an user account.
 * It encapsulates all image data utilized by the system.
 * @author Manuel R Saldana
 *
 */
public class Image {

	// Database table parameters
		private String name;
		private String type;
		private String description;
		private String size;
		private String dateCreated;
		private String creator;
		private String fileName;
		
		
		/**
		 * Default Constructor
		 */
		public Image(){}
		
		public Image(String name, String type, String description,
				String size, String dateCreated, String creator,
				String fileName){
			this.name = name;
			this.type = type;
			this.description = description;
			this.size = size;
			this.dateCreated = dateCreated;
			this.creator = creator;
			this.setFileName(fileName);
	
			
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}

		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @return the size
		 */
		public String getSize() {
			return size;
		}

		/**
		 * @return the dateCreated
		 */
		public String getDateCreated() {
			return dateCreated;
		}

		/**
		 * @return the creator
		 */
		public String getCreator() {
			return creator;
		}
		
		/**
		 * @return the fileName
		 */
		public String getFileName() {
			return fileName;
		}

		
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}

		/**
		 * @param description the description to set
		 */
		public void setDescription(String description) {
			this.description = description;
		}

		/**
		 * @param size the size to set
		 */
		public void setSize(String size) {
			this.size = size;
		}

		/**
		 * @param dateCreated the dateCreated to set
		 */
		public void setDateCreated(String dateCreated) {
			this.dateCreated = dateCreated;
		}

		/**
		 * @param creator the creator to set
		 */
		public void setCreator(String creator) {
			this.creator = creator;
		}

		
		/**
		 * @param fileName the fileName to set
		 */
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		
		
		

	
		
}