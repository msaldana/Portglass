package com.dhs.portglass.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;



/**
 * Establishes a connection to the Portglass database. Upon server start, the web.xml is 
 * read to retrieve the updated version of the database name, driver, password, 
 * and URL. The same are configured on this class whenever a call to the database
 * is made.
 *
 */
public class DBManager {
	
	
	// Initializes the Connection object that will be utilized to access the database.
    private static Connection connect = null;
    
    /*
     * Initializes child logger that appends messages to the log defined in the properties.
     * Properties are defined in ServerListener.java.
     */
   
    private static final Logger logger = Logger.getLogger(DBManager.class.getName());
    
    /*
     * The following parameters represent the database credentials required 
     * for a successful connection establishment. 
     */
    private static String driverName = "org.postgresql.Driver";
    		//ServerListener.getDBDriver();
	private static String userName = "portglassAdmin";
			//ServerListener.getDBUserName();
    private static String password = "P0rtgl4ssP4ss";
    		//ServerListener.getDBPassword(); 
    private static String url = "jdbc:postgresql://portglassdb.c3vxmpfh5kyd.us-" +
    		"west-2.rds.amazonaws.com:5432/PortglassDB";
    		//ServerListener.getDBURL(); 
   
    
    /**
     * Starts a connection to the configured database.
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static Connection startConnection() throws ClassNotFoundException, SQLException 
    {	
        if(connect == null) 
        {         	
            Class.forName(driverName);
            connect = DriverManager.getConnection (url, userName,password);
            
        }
        return connect;    
    }
    
    /**
     * Establishes a new connection to the database.
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection newConnection() throws ClassNotFoundException, SQLException 
    {
    	
        Connection newConnection = null;

        Class.forName(driverName);
        newConnection = DriverManager.getConnection (url, userName, password);
        
   
        return newConnection;
        
    }

    /**
     * @return Returns the connection made with the configured database.
     */
    private static Connection getConnection()
    {
        return connect;
    }
    
    /**
     * Terminates an established connection.
     */
    private static void endConnection()
    {
		try {
			if(connect != null)
			{
				if(!connect.isClosed())
				{
				    connect.close();
				    connect = null;
				    logger.log(INFO, "Connection Closed");
				}
			}
		} 
		catch (SQLException e) 
		{
		   
		    logger.log(SEVERE, e.getMessage());
		}	
    }
    
    /**
     * Executes an update query on the database with the given query
     * and query statements. Used for queries that either INSERT,
     * UPDATE, or DELETE entries. Returns a boolean stating if the
     * query was accepted by the database.
     * @param query String version of query to the database.
     * @param expressions Replaces attributes that where dimmed with '?'
     * on the provided query String.
     * @return A boolean stating if the query was processed by the 
     * database.
     * @throws SQLException Thrown by any error during query execution.
     * @throws ClassNotFoundException 
     */
    public static boolean update(String query, Object[] expressions) 
    		
    {
    	boolean processed = false;
    	
		try {
			
			startConnection();
			PreparedStatement statement = getConnection().prepareStatement(query);
	    	for(int i=0; i<expressions.length; i++)
	    	{
	    		statement.setObject(i+1, expressions[i]);
	    	}
	    	statement.executeUpdate();
	    	processed=true;
		} catch (ClassNotFoundException e) {
			logger.log(SEVERE, e.getMessage());
		} catch (SQLException e) {
			logger.log(SEVERE, e.getMessage());
		}
		finally{
			endConnection();
		}
    	return processed;
    	
    }
    
    /**
     * Executes a query on the database with the given expressions.
     * Used for queries that retrieve data in the form of ResultSet
     * objects. If an error occurs the method returns null.
     * @param query Query to be executed
     * @param expressions values for parameters in the provided query.
     * @return ResultSet on successful query execution, null otherwise.
     */
    public static ResultSet execute(String query, Object[] expressions) 
    {
    	ResultSet result = null;
 
		try {
			startConnection();
			PreparedStatement statement = getConnection().prepareStatement(query);
			for(int i=0; i<expressions.length; i++)
	    	{
	    		statement.setObject(i+1, expressions[i]);
	    	}
			
			result = statement.executeQuery();
			
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			endConnection();
		}
    	return result;
    }
    
    

}
    


