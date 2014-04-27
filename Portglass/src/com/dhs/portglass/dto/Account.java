package com.dhs.portglass.dto;

/**
 * This POJO is used to map the Account database table result set
 * into an object that can be utilized to manage an user account.
 * It encapsulates all account data utilized by the system.
 * @author Manuel R Saldana
 *
 */
public class Account {
	
	// Database table parameters
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String phone;
	private String type;
	private boolean isActive;
	private String salt;
	
	/**
	 * Default Constructor
	 */
	public Account() {
		super();
	}
	
	public Account(String firstName, String lastName, String email, 
			String password, String phone,  boolean isActive, String type,
			String salt)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.isActive = isActive;
		this.type = type;
		this.salt = salt;
	}
	
	
	/*----------------------------------------------------------
	 * Setter Methods
	 *----------------------------------------------------------
	 */

	

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setActiveStatus(boolean isActive) {
		this.isActive = isActive;
	}
	
	public void setSalt(String salt){
		this.salt = salt;
	}

	
	/*----------------------------------------------------------
	 * Getter Methods
	 *----------------------------------------------------------
	 */
	
	

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getPhone() {
		return phone;
	}

	public String getType() {
		return type;
	}

	public boolean isActive() {
		return isActive;
	}

	public String getSalt() {
		return salt;
	}
	
	/*----------------------------------------------------------
	 * Utility Methods
	 *----------------------------------------------------------
	 */
	
	public String toString(){
		return "Name: "+ this.firstName+" Lastname: "+ this.lastName +" email: "+this.email+
			   " Phone: "+this.phone+" Type: "+ this.type+" Active: "+this.isActive; 
	}
	

}
