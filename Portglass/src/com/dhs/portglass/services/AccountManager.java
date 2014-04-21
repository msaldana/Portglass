package com.dhs.portglass.services;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.dhs.portglass.dto.Account;
import com.dhs.portglass.security.PasswordManager;
import com.dhs.portglass.server.DBConnector;

/**
 * 
 * @author Manuel R Saldana
 *
 */
public class AccountManager 
{
	
	//Queries to retrieve account data from database
	private static final String GET_ADMIN_ACCOUNTS = "SELECT * FROM Account WHERE type = " +
			"'admin'";
	private static final String GET_ACCOUNT = "SELECT * FROM Account WHERE email = " +
			"?";
	private static final String GET_AVAILABILITY = "SELECT count(email) FROM Account" +
			" WHERE email =?";
	private static final String GET_RECOVERY_LINK = "SELECT date_added FROM recover WHERE key = ?";

	//Queries to create data in database
	private static final String ADD_ACCOUNT = "INSERT INTO account(firstname, lastname, " +
			"email, password, phone, isactive, type, salt) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String ADD_RECOVERY_LINK_UPDATE = "UPDATE recover SET date_added=?, " +
			" key=? WHERE account_id=?";
	private static final String ADD_RECOVERY_LINK_INSERT= "INSERT INTO recover (account_id, date_added, key) " +
			"SELECT ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM recover WHERE account_id=?)";
	
	
	
	
	//Queries to update data in database
	private static final String UPDATE_PASSWORD_THRU_LINK = "UPDATE account SET password = ?, " +
			" salt = ? FROM recover WHERE email = account_id AND key=?";
			
	//Queries to delete data in database
	private static final String DELETE_RECOVERY_LINK = "DELETE FROM recover where key " +
			"= ?";
		
	//Singleton instance of the AccountManager
	private static final AccountManager singleton = new AccountManager();
	
	//Recovery Link Validity Threshold
	private static final long RECOVERY_LINK_THRESHOLD = 86400000;
	//Recovery Link Servlet
	private static final String PASS_CHANGE_URL = 
			"http://localhost:8080/Portglass/passRecovery";
	
	//Connection object for DB
	Connection connection = null;

	/**
	 * Basic Constructor of the AccountManager object.
	 */
	private AccountManager(){
		super();
	}

	/**
	 * This method ensures that no other active instance of the AccountManager
	 * is present. It returns the singleton instance of the manager object.
	 * @return
	 */
	public static final AccountManager getInstance(){
		return singleton;
	}
	
	
	/**
	 * Adds account to the DB and creates a new account request notification. Returns 
	 * true if successful, false otherwise
	 * @param account The account to add
	 * @return true if successful, false otherwise
	 */
	public boolean addAccount(Account account) {		
		Connection connection = null;
		int status;
		try{
			connection = DBConnector.newConnection();
			connection.setAutoCommit(false);

			PreparedStatement stmt = connection.prepareStatement(ADD_ACCOUNT);
			stmt.setString(1, account.getFirstName());
			stmt.setString(2, account.getLastName());
			stmt.setString(3, account.getEmail());
			stmt.setString(4, account.getPassword());
			stmt.setString(5, account.getPhone());
			stmt.setBoolean(6, account.isApproved());
			stmt.setString(7, account.getType());
			stmt.setString(8,  account.getSalt());
			
			

			status = stmt.executeUpdate();
			if (status != 1){
				throw new Exception("Error adding account");
			}

			//Need to add the creation of a new account request and checking its status

			if (status != 1){
				connection.rollback();
				connection.setAutoCommit(true);
				throw new Exception("Error creating new acount request");
			}
			else{ 
				try{
					connection.commit();
				}
				catch(Exception e){
					try{
						connection.rollback();
						connection.setAutoCommit(true);
					}
					catch(Exception e2){
					}

					throw new Exception("Unable to commit transaction.", e);
				}
				connection.setAutoCommit(true);
			}
		}
		catch(Exception e){
			System.out.println(e);
			return false;
		}
		finally{
			DBConnector.endConnection();
		}
		return true;
	}
	
	/**
	 * Queries the database for a user that has the same
	 * email as the email contained within the Account object.
	 * @param account
	 * @return
	 */
	public Account getUser(Account account)
	{
		
		Account result = null;
		Connection conn = null;
		try {
			conn = DBConnector.newConnection();
			PreparedStatement stmt = conn.prepareStatement(GET_ACCOUNT);
			stmt.setString(1, account.getEmail());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()){
				result = new Account();
				createAccountFromRS(result, rs);
			}
		}
		catch(Exception e){
			System.out.println("Unable to read data from data source: "+ e);
		}
		finally {
			DBConnector.endConnection();
		}
		return result;
		
	}
	
	/**
	 * Gets User from database by querying for the specified
	 * email.
	 * @param email
	 * @return
	 */
	public Account getUser(String email)
	{
		
		Account result = null;
		Connection conn = null;
		try {
			conn = DBConnector.newConnection();
			PreparedStatement stmt = conn.prepareStatement(GET_ACCOUNT);
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()){
				result = new Account();
				createAccountFromRS(result, rs);
			}
		}
		catch(Exception e){
			System.out.println("Unable to read data from data source: "+ e);
		}
		finally {
			DBConnector.endConnection();
		}
		return result;
		
	}
	
	
	/**
	 * Finds all the Administrator accounts in the DB
	 * @return List of all administrators
	 */
	public ArrayList<Account> getAdministratorList()
	{
		ArrayList<Account> result = new ArrayList<Account>();
		Account indRes;
		Connection conn = null;
		try {
			conn = DBConnector.newConnection();
			PreparedStatement stmt = conn.prepareStatement(GET_ADMIN_ACCOUNTS);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()){
				indRes = new Account();
				createAccountFromRS(indRes, rs);
				result.add(indRes);
			}
		}
		catch(Exception e){
			System.out.println("Unable to read data from data source: "+ e);
		}
		finally {
			DBConnector.endConnection();
		}
		return result;
	}
	
	/**
	 * Queries the DB to count the amount of current entries with the 
	 * provided email value. This method is preset to return false 
	 * unless the database responds that no entry is found. If the count
	 * is zero, then this email has not been used and is available to use
	 * as a username.
	 * @param username
	 * @return
	 */
	public boolean isAvailable(String username)
	{
		Connection conn = null;
		boolean isAvailable = false;
		try {
			conn = DBConnector.newConnection();
			PreparedStatement stmt = conn.prepareStatement(GET_AVAILABILITY);
			
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			if (count==0) isAvailable =true;
			
		}
		catch(Exception e){
			System.out.println("Unable to read data from data source: "+ e);
		}
		finally {
			DBConnector.endConnection();
		}
		return isAvailable;
	}
	
	
	
	public String generateRecoveryLink(String email) throws NoSuchAlgorithmException {		
		
		Connection connection = null;
		long currentTime = System.currentTimeMillis();
		//Generate a key for the password reset, to be sent via email.
		String key= PasswordManager.encryptSHA256(email+currentTime, 
				PasswordManager.generateSalt());
		try{
			connection = DBConnector.newConnection();
			
			// To avoid race conditions, the connection will execute two consecutive
			// queries; instead of one single query containing both.
			PreparedStatement stmt = connection.prepareStatement(ADD_RECOVERY_LINK_UPDATE);
			stmt.setString(1, currentTime+"");
			stmt.setString(2, key);
			stmt.setString(3, email);
			
			PreparedStatement stmt2 = connection.prepareStatement(ADD_RECOVERY_LINK_INSERT);
			stmt2.setString(1, email);
			stmt2.setString(2, currentTime+"");
			stmt2.setString(3, key);
			stmt2.setString(4, email);
		
			stmt.executeUpdate();
			stmt2.executeUpdate();
			

		}
		catch(Exception e){
			System.out.println("Unable to read data from data source: "+ e);
			return PASS_CHANGE_URL+"?key=-1";
		}
		finally {
			DBConnector.endConnection();
		}
		return PASS_CHANGE_URL+"?key="+key;
	}
	
	
	public boolean updatePasswordThroughLink(String key, String password, String salt) {		
		Connection connection = null;
		int status;
		System.out.println("key: "+key);
		System.out.println("pass: "+password);
		System.out.println("salt: "+salt);
		try{
			connection = DBConnector.newConnection();
			connection.setAutoCommit(false);

			PreparedStatement stmt = connection.prepareStatement(UPDATE_PASSWORD_THRU_LINK);
			stmt.setString(1, password);
			stmt.setString(2, salt);
			stmt.setString(3, key);
			
			status = stmt.executeUpdate();
			

			//Need to add the creation of a new account request and checking its status

			if (status != 1){
				connection.rollback();
				connection.setAutoCommit(true);
				throw new Exception("Error updating password");
			}
			else{ 
				try{
					connection.commit();
				}
				catch(Exception e){
					try{
						connection.rollback();
						connection.setAutoCommit(true);
					}
					catch(Exception e2){
					}

					throw new Exception("Unable to commit transaction.", e);
				}
				connection.setAutoCommit(true);
			}
		}
		catch(Exception e){
			System.out.println(e);
			return false;
		}
		finally{
			DBConnector.endConnection();
		}
		return true;
	}
	
	/**
	 * Deletes the row from the 'recover' table in the DB that matches
	 * the given key. If no row exists, nothing happens.
	 * @param key Corresponds to a validation key to reset password
	 * @return A boolean stating whether this key is still valid.
	 */
	public void deletePasswordLink(String key){
		Connection conn = null;		
		try {
			conn = DBConnector.newConnection();
			PreparedStatement stmt = conn.prepareStatement(DELETE_RECOVERY_LINK);			
			stmt.setString(1, key);	
			stmt.executeUpdate();
		}
		catch(Exception e){
			System.out.println("Unable to read data from data source: "+ e);			
		}
		finally {
			DBConnector.endConnection();
		}	
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
		Connection conn = null;
		boolean isValid = false;
		try {
			conn = DBConnector.newConnection();
			PreparedStatement stmt = conn.prepareStatement(GET_RECOVERY_LINK);
			
			stmt.setString(1, key);
			ResultSet rs = stmt.executeQuery();
			//If this key exists, verify if over the threshold time.
			if(rs.next())
			{
				if(RECOVERY_LINK_THRESHOLD-(System.currentTimeMillis()-rs.getLong(1))>0)
					isValid=true;
			}
			
		}
		catch(Exception e){
			System.out.println("Unable to read data from data source: "+ e);
		}
		finally {
			DBConnector.endConnection();
		}
		return isValid;
	}
	
	
	/**
	 * Utility method to add the result set columns to an account object in the respective
	 * instance fields.
	 * @param account The account object where the result set columns will be saved
	 * @param rs The result set returned from the data base query
	 * @throws Exception  Occurs when an incorrect index is selected and does not 
	 * match the Account object paramater types.
	 */
	private void createAccountFromRS(Account account, ResultSet rs) throws Exception{
	
		account.setFirstName(rs.getString(1).trim());
		account.setLastName(rs.getString(2).trim());
		account.setEmail(rs.getString(3).trim());
		account.setPassword(rs.getString(4).trim());
		account.setPhone(rs.getString(5));
		account.setApproved(rs.getBoolean(6));
		account.setType(rs.getString(7).trim());
		account.setSalt(rs.getString(8).trim());
	}
	
	public static void main(String[] args){
		
		
	}
	
}
