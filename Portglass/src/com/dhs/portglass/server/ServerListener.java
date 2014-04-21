package com.dhs.portglass.server;



import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


/**
 * Configuration class that initializes the database variables.
 * The same are available throughout the service life of the server.
 * @author saldam4
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
		INDEX_URL = event.getServletContext().getInitParameter("appliation.url");
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
	 * Granted that the project does not need to close any objects when the 
	 * server is powered off, this method is not implemented. Since this class implements
	 * the context listener, it is mandatory that the class be mentioned nonetheless.
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{
	}
	

	
	
	
	
		
}
