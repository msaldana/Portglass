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
public class DBConnector {
	
	// Initializes the Connection object that will be utilized to access the database.
    private static Connection connect = null;
    
    /*
     * Initializes child logger that appends messages to the log defined in the properties.
     * Properties are defined in ServerListener.java.
     */
   
    private static final Logger logger = Logger.getLogger(DBConnector.class.getName());
    
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
    //Change this for changing databases
    public static DBVersion DB_VERSION = DBVersion.POSTGRESQL;
    
    /**
     * Serves to depict the desired version of the database.
     */
    public static enum DBVersion
    {
    	SQL_SERVER,
    	MYSQL,
    	POSTGRESQL
    }
    
    /**
     * Starts a connection to the configured database.
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection startConnection() throws ClassNotFoundException, SQLException 
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
    public static Connection getConnection()
    {
        return connect;
    }
    
    /**
     * Terminates an established connection.
     */
    public static void endConnection()
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
    
    public static ResultSet execute(String query) throws SQLException
    {
    	
    	PreparedStatement stmt;
    	ResultSet rs = null;
		try {
			connect = DBConnector.newConnection();
			stmt = connect.prepareStatement(query);
			rs = stmt.executeQuery();
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			connect.close();
		}
		return rs;
    }
    
}
    


