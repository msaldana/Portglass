package com.dhs.portglass.server;



import java.io.File;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


import com.dhs.portglass.directory.Indexer;


/**
 * Configuration class that is invoked when the application is deployed.
 * Loads property settings defined in the web.xml configuration file
 * of this application. Additionally, it starts the Indexer so 
 * that the specified directory is monitored throughout the life of the
 * application scope and files entering the directory will be indexed
 * into the database.
 * @author Manuel R Saldana Pueyo
 *
 */

@WebListener
public class ServerListener implements ServletContextListener 
{
	private static String DB_DRIVER;
	private static String DB_USERNAME;
	private static String DB_PASSWORD;
	private static String DB_URL;
	private static String NOREPLY_EMAIL;
	private static String IMG_DIR;
	private static String DATA_DIR;
	private static String INDEX_URL;
	
	
	private static Indexer INDEXER;
	
	/**
	 * On Server startup, initializes database variables according to the 
	 * specified values on the web.xml.
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) 
	{	
		ServletContext servletContext = event.getServletContext();
		
		
		DB_DRIVER = servletContext.getInitParameter("db.driver");
		DB_USERNAME = servletContext.getInitParameter("db.username");
		DB_PASSWORD = servletContext.getInitParameter("db.password");
		DB_URL = servletContext.getInitParameter("db.url");
		NOREPLY_EMAIL= servletContext.getInitParameter("noreply.email");	
		
		IMG_DIR = event.getServletContext().getRealPath("/img/");
		DATA_DIR = event.getServletContext().getRealPath("/WEB-INF/data/");
		INDEX_URL = event.getServletContext().getInitParameter("appliation.index");
		
		
		try {
			INDEXER= new Indexer(new File("/Users/Manuel/git/Portglass/Portglass/WebContent/WEB-INF/data/monitored/").toPath());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Context Initialized");
	}
	
	
	
	
	/**
	 * Makes the driver name available from within project classes the 
	 * moment the server is powered on.
	 * @return
	 */
	public static String getDBDriver()
	{
		return DB_DRIVER;
	}
	
	/**
	 * Makes the database username available from within project classes 
	 * the moment the server is powered on.
	 * @return
	 */
	public static String getDBUserName()
	{
		return DB_USERNAME;
	}
	/**
	 * Makes the database password available from within project classes
	 * the moment the server is powered on.
	 * @return
	 */
	public static String getDBPassword()
	{
		return DB_PASSWORD;
	}
	/**
	 * Makes the database url available from within project classes
	 * the moment the server is powered on.
	 * @return
	 */
	public static String getDBURL()
	{
		return DB_URL;
	}

	/**
	 * Makes the contact email available from within project classes
	 * the moment the server is powered on.
	 */
	public static String getEmail()
	{
		return NOREPLY_EMAIL;
	}

	/**
	 * Makes the Image directory available from within project classes
	 * the moment the server is powered on.
	 */
	public static String getImageDirectory()
	{
		return IMG_DIR;
	}
	
	/**
	 * Makes the Data directory available from within project classes
	 * the moment the server is powered on.
	 */
	public static String getDataDirectory()
	{
		return DATA_DIR;
	}
	
	/**
	 * Makes the application index URL available from within project classes
	 * the moment the server is powered on.
	 */
	public static String getDeploymentURL()
	{
		return INDEX_URL;
	}
	
	
	


	



	
	
	
	/**
	 * Shuts down all instances that may hold on to resources and threads.
	 */ 
	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{
		INDEXER.shutdown();
	}
	

	
	
	
	
		
}
