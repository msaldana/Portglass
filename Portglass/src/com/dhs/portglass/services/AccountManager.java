package com.dhs.portglass.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dhs.portglass.dto.Account;
import com.dhs.portglass.security.PasswordManager;
import com.dhs.portglass.server.DBManager;


/**
 * Manages logic for creating, updating, and/or deleting entries 
 * in the Account Database Table. It is a synchronized singleton
 * class, accessible through the getInstance() method.
 * @author Manuel R Saldana
 *
 */
public class AccountManager 
{


	//Singleton instance of the AccountManager
	private static final AccountManager INSTANCE = new AccountManager();

	/*****************************************************
	 * Queries to retrieve account data from database
	 ****************************************************/

	/*
	 * Retrieve account entry for the provided email address.
	 */
	private static final String GET_ACCOUNT = "SELECT * FROM Account WHERE email = " +
			"?";

	/* 
	 * Retrieve active administrators. Active administrators are those
	 * whose isActive column is set to 'true'
	 */
	private static final String GET_ACTIVE_ADMIN_ACCOUNTS = "SELECT * FROM Account WHERE type = " +
			"'admin' AND isactive = true";

	/*
	 * Retrieves the count for account entries matching the provided 
	 * email address. 
	 */
	private static final String GET_AVAILABILITY = "SELECT count(email) FROM Account" +
			" WHERE email =?";

	/*
	 * Retrieves the creation date for a given recovery link key.
	 */
	private static final String GET_RECOVERY_LINK_DATE = "SELECT date_added FROM recover" +
			" WHERE key = ?";

	/*
	 * Retrieves the salt used to hash the password of the provided
	 * email address.
	 */
	private static final String GET_USER_SALT = "SELECT salt FROM account WHERE email = ?";


	/*****************************************************
	 * Queries to create account data in the database
	 ****************************************************/

	/*
	 * Creates a new entry in the account table initialized at every 
	 * column with the provided values.
	 */
	private static final String ADD_ACCOUNT = "INSERT INTO account(firstname, lastname, " +
			"email, password, phone, isactive, type, salt) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	/*
	 * Create a new recovery link entry in the Recover table if the 
	 * "account_id" - a foreign key for the "email" column in the 
	 *  Account table - does not have a current entry.
	 */
	private static final String ADD_RECOVERY_LINK_INSERT= "INSERT INTO recover" +
			" (account_id, date_added, key) SELECT ?, ?, ? WHERE NOT EXISTS" +
			" (SELECT 1 FROM recover WHERE account_id=?)";


	/*****************************************************
	 * Queries to update account data in the database
	 ****************************************************/

	/*
	 * Updates the 'password' and 'salt' columns of the Account table entry 
	 * whose 'email' value matches the foreign key 'account_id' and where the
	 * 'key' value in the Recover table matches the provided key. 
	 */
	private static final String UPDATE_PASSWORD_THRU_LINK = "UPDATE account SET password = ?, " +
			" salt = ? FROM recover WHERE email = account_id AND key=?";

	/*
	 * Updates the recovery link entry for the provided account 
	 * email. The "account_id is a foreign key for the Account table 
	 * column "email"
	 */
	private static final String UPDATE_RECOVERY_LINK = "UPDATE recover SET date_added=?, " +
			" key=? WHERE account_id=?";


	/*****************************************************
	 * Queries to delete account data in the database
	 ****************************************************/

	/*
	 * Deletes entries in the Recover table whose key matches
	 * the provided value. 
	 */
	private static final String DELETE_RECOVERY_LINK = "DELETE FROM recover where key " +
			"= ?";

	/*****************************************************
	 * Other Instance Variables
	 ****************************************************/

	/*Recovery Link Validity Threshold - Set at 24 hours*/
	private static final long RECOVERY_LINK_THRESHOLD = 86400000;
	/*Recovery Link Servlet*/
	private static final String PASS_CHANGE_URL = 
			"http://localhost:8080/Portglass/passRecovery";

	//Connection object for DB
	Connection connection = null;



	/**
	 * Basic Constructor of the AccountManager object.
	 */
	private AccountManager()
	{
		super();
	}

	/**
	 * This method ensures that no other active instance of the AccountManager
	 * is present. It returns the singleton instance of the manager object.
	 * @return
	 */
	public static synchronized final AccountManager getInstance(){
		return INSTANCE;

	}
	
	/* *************************************************************************
	 *
	 * INSERT METHODS
	 *
	 **************************************************************************/
	

	/**
	 * Creates a new entry in the 'Account' database table. 
	 * Fills each column of this table with the contents of the Account DTO.
	 * If the query is executed correctly, this method returns true; false
	 * otherwise. 
	 * @param account The Account DTO that will be added to the 'Account' 
	 * database table.
	 * @return A boolean indicating if the query was processed by the 
	 * database.
	 */
	public boolean addAccount(Account account) {		

		ArrayList<Object> statements = new ArrayList<Object>();
		statements.add(account.getFirstName());
		statements.add(account.getLastName());
		statements.add(account.getEmail());
		statements.add(account.getPassword());
		statements.add(account.getPhone());
		statements.add(account.isActive());
		statements.add(account.getType());
		statements.add(account.getSalt());


		return DBManager.update(ADD_ACCOUNT, statements.toArray());
	}

	/**
	 * Generates a recovery link for the provided email. It runs
	 * to separate queries - not ran together to avoid race 
	 * conditions. The first query verifies if the email is already
	 * associated with a key; if so, it proceeds to update the 
	 * entry with a new key. Otherwise, an entry is made to the 
	 * table, associating the email (foreign key) to an account
	 * identification ('account_id') column and the new key. These 
	 * rows are stored in the Recover table of the database, forming
	 * a 1:1 association with the Account table.
	 * @param email Corresponds to an entry in the 'email' column
	 * of the Account database table
	 * @return A String with the new recovery link. If an error 
	 * occurred, the key will be blank.
	 */
	public String generateRecoveryLink(String email) {		
	
		long currentTime = System.currentTimeMillis();
		String key;
		String link=PASS_CHANGE_URL+"?key=";
		boolean isProcessed = false;
		ArrayList<Object> expressions = new ArrayList<Object>();
		try{
			//Generate a key for the password reset, to be sent via email.
			key = PasswordManager.encryptSHA256(email+currentTime, 
					PasswordManager.generateSalt());
			
			/* 
			 * Verify if a key has been already entered for the given 
			 * email and update the entry if so.
			 */
			
			expressions.add(currentTime+"");
			expressions.add(key);
			expressions.add(email);
			isProcessed = (DBManager.update(UPDATE_RECOVERY_LINK, expressions.toArray()));
			expressions.clear();
	
			/*
			 * Insert a new key if the provided email address does
			 * not have any prior entries in the table. Has no effect if
			 * provided email already has key.
			 */
			expressions.add(email);
			expressions.add(currentTime+"");
			expressions.add(key);
			expressions.add(email);
			isProcessed=DBManager.update(ADD_RECOVERY_LINK_INSERT, expressions.toArray());
			
			if (isProcessed) link = link+key;
		}
		catch (Exception e){
			
		}
		return link;
	}
	
	

	/* *************************************************************************
	 *
	 * UPDATE METHODS
	 *
	 **************************************************************************/
	
	/**
	 * Queries the 'Recovery' table for all entries whose 'key' column match the key
	 * value provide. Then, the 'account_id', a column that serves as a foreign key
	 * to the 'Account' table will be used to retrieve the email address of the matches.
	 * Finally, the 'Account table is evaluated in order to retrieve all rows that 
	 * possess the same email value ('email' = 'account_id). The resulting row will
	 * have its 'password' and 'salt' columns changed to the value provided. 
	 * @param key Refers to a value of the 'key' column in the 'Recover' database
	 * table. 
	 * @param password Value that will be updated in the 'password' column of the query 
	 * matches.
	 * @param salt Value that will be updated in the 'salt' column of the query matches.
	 * @return A boolean indicating if the Query was processed by the database
	 */
	public boolean updatePasswordThroughLink(String key, String password, String salt) {		
		
		
		ArrayList<Object> expressions = new ArrayList<Object>();
		expressions.add(password);
		expressions.add(salt);
		expressions.add(key);
		return (DBManager.update(UPDATE_PASSWORD_THRU_LINK, expressions.toArray()));
	}
	
	
	/* *************************************************************************
	 *
	 * DELETE METHODS
	 *
	 **************************************************************************/
		
	
	/**
	 * Deletes the row from the 'recover' table in the DB that matches
	 * the given key. If no row exists, nothing happens.
	 * @param key Corresponds to a validation key to reset password
	 * @return A boolean stating whether the query was executed successfully.
	 */
	public boolean deletePasswordLink(String key){
		ArrayList<Object> expressions = new ArrayList<Object>();
		expressions.add(key);
		return (DBManager.update(DELETE_RECOVERY_LINK, expressions.toArray()));
			
	}
	
	/* *************************************************************************
	 *
	 * SELECT METHODS
	 *
	 **************************************************************************/
	

	/**
	 * Queries the 'Account' table of the database for rows whose 'type' and 
	 * 'isActive' columns match the values "admin" and "true" (respectively).
	 * @return A list Account DTO consisting of all entries that matched the
	 * database query. 
	 */
	public ArrayList<Object> getAdministratorList()
	{
		ArrayList<Object> list = new ArrayList<Object>();
		ResultSet rs = DBManager.execute(GET_ACTIVE_ADMIN_ACCOUNTS, list.toArray());
		try {
			while (rs.next())
			{							
				list.add(createAccountFromRS(rs));
			}
		} catch (Exception e ) {
			e.printStackTrace();
		}
		return list;
		
	}

	/**
	 * Queries the 'Account' table of the database for rows that have the same
	 * value in the 'email' column as the 'email' instance field declared in the
	 * provided Account DTO. Should return only one match, given that the 'email'
	 * column is UNIQUE.
	 * @param account An Account DTO containing a single user's data.
	 * @return An Account DTO containing all database data on the user.
	 */
	public Account getUser(Account account)
	{

		ArrayList<Object> expressions = new ArrayList<Object>();
		expressions.add(account.getEmail());
		ResultSet rs = DBManager.execute(GET_ACCOUNT, expressions.toArray());
		Account result = null;
		try {
			if(rs.next() && rs.isFirst() && rs.isLast()){					
				result = createAccountFromRS(rs);
			}
		} catch (Exception e ) {
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * Queries the 'Account' table of the database for rows that have the same
	 * value in the 'email' column as the provided email string. Should return 
	 * only one match, given that the 'email' column is UNIQUE.
	 * @param email Represents a value to be compared to the 'email' column of 
	 * the 'Account' database table.
	 * @return An Account DTO containing the database entry for the user who
	 * matches the provided email address.
	 */
	public Account getUser(String email)
	{
		ArrayList<Object> expressions = new ArrayList<Object>();
		expressions.add(email);
		ResultSet rs = DBManager.execute(GET_ACCOUNT, expressions.toArray());
		Account result = null;
		try {
			if(rs.next() && rs.isFirst() && rs.isLast()){				
				result = createAccountFromRS(rs);
			}
		} catch (Exception e ) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Queries the 'Account' table of the database for rows that have the
	 * same value of the provided email address in the 'email' column. Given 
	 * that this column is UNIQUE, only one match should result. The query extracts
	 * the 'salt' column value and returns it in the form of a ResultSet. This
	 * method verifies, thus, that only one result is handed back in the ResultSet 
	 * and then returns this value.
	 * @param email Represents a value to be compared to the 'email' column of 
	 * the 'Account' database table.
	 * @return A string representation of the 'salt' column value of the query's 
	 * match. If the query fails or a single match isn't found, the method returns
	 * null.
	 */
	public String getSalt(String email){
		ArrayList<Object> expressions = new ArrayList<Object>();
		expressions.add(email);
		String result = null;
		ResultSet rs = DBManager.execute(GET_USER_SALT, expressions.toArray());
		try {
			if(rs.next() && rs.isFirst() && rs.isLast()){				
				result = rs.getString(1).trim();
			}
		} catch (Exception e ) {
			e.printStackTrace();
		}
		return result;
	}


	/**
	 * Queries the DB to count the amount of current entries with the 
	 * provided email value. This method is preset to return false 
	 * unless the database responds that no entry is found. If the count
	 * is zero, then this email has not been used and is available to use
	 * as a username (UNIQUE identifier).
	 * @param email Corresponds to a value to be compared to the 'email'
	 * column of the 'Account' database' table.
	 * @return A boolean stating whether or not there is a matching entry in
	 *  the 'Account' table.
	 */
	public boolean isAvailable(String email)
	{
		boolean isAvailable = false;
		ArrayList<Object> expressions = new ArrayList<Object>();
		expressions.add(email);
		ResultSet rs = DBManager.execute(GET_AVAILABILITY, expressions.toArray());
			
		try {
			if(rs.next() && rs.isFirst() && rs.isLast())
			{
				if (rs.getInt(1)==0) isAvailable = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return isAvailable;
	}


	/**
	 * Verifies if the provided key is valid in two ways. First
	 * it verifies that the key exists in the 'Recover' table of the
	 * database, then it confirms that the threshold time has not 
	 * been met. If the link was created in a time span that exceeds
	 * the limit, is is considered invalid.
	 * @param key Corresponds to a validation key to reset password
	 * @return A boolean stating whether this key is still valid.
	 */
	public boolean isValidRecoveryLink(String key){
		
		boolean isValid = false;
		ArrayList<Object> expressions = new ArrayList<Object>();
		expressions.add(key);
		ResultSet rs = DBManager.execute(GET_RECOVERY_LINK_DATE, expressions.toArray());
		
			//If this key exists, verify if over the threshold time.
			try {
				if(rs.next() && rs.isFirst() && rs.isLast())
				{
					if(RECOVERY_LINK_THRESHOLD-(System.currentTimeMillis()-rs.getLong(1))>0)
						isValid=true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		
		return isValid;
	}

	/* *************************************************************************
	 *
	 * UTILITY METHODS
	 *
	 **************************************************************************/
	

	/**
	 * Utility method to add the result set columns to an account object in the respective
	 * instance fields.
	 * @param rs The result set returned from the data base query
	 * @throws Exception  Occurs when an incorrect index is selected and does not 
	 * match the Account object types.
	 * @return An Account object with the information of the DB, null if an exception 
	 * occurs.
	 */
	private Account createAccountFromRS(ResultSet rs) {
		Account result = null;
		try {
			result = new Account(rs.getString(1).trim(), rs.getString(2).trim(),
					rs.getString(3).trim(), rs.getString(4).trim(), rs.getString(5),
					rs.getBoolean(6), rs.getString(7).trim(), rs.getString(8).trim());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args){
		Account user = new Account();
		System.out.println(AccountManager.getInstance().addAccount(user));
	}

}
