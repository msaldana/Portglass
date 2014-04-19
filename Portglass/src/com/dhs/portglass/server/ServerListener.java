package com.dhs.portglass.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

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
	private static Logger logger = 
			Logger.getLogger(ServerListener.class.getName());
	
	/**
	 * On Server startup, initializes database variables according to the 
	 * specified values on the web.xml.
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) 
	{	
		ServletContext servletContext = event.getServletContext();
		initLogging(servletContext);
		
		DB_DRIVER = servletContext.getInitParameter("db.driver");
		DB_USERNAME = servletContext.getInitParameter("db.username");
		DB_PASSWORD = servletContext.getInitParameter("db.password");
		DB_URL = servletContext.getInitParameter("db.url");
		NOREPLY_EMAIL= servletContext.getInitParameter("noreply.email");	
		
		IMG_DIR = event.getServletContext().getRealPath("/img/");
		DATA_DIR = event.getServletContext().getRealPath("/WEB-INF/data/");
		
	}
	
	/**
	 * Makes the logger available from within project classes the moment
	 * the server is powered on.
	 * @return Logger
	 */
	public static Logger getLogger()
	{
		return logger;
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
	 * Granted that the project does not need to close any objects when the 
	 * server is powered off, this method is not implemented. Since this class implements
	 * the context listener, it is mandatory that the class be mentioned nonetheless.
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{
	}
	
	/**
	 * Configures the LoggingManager based on this application's default
	 * settings and the LogManager.properties parameter of web.xml.
	 */
	private void initLogging(ServletContext servletContext) {
		Properties properties = getDefaultLogManagerProperties();
		properties = parseProperties(servletContext.
				getInitParameter("LogManager.properties"), properties);
		try {
			configureLogManager(properties);
		} catch (IOException e) {
			logger.log(Level.WARNING, "Couldn't configure LogManager!", e);
		}
	}
	
	/**
	 * Returns the default LogManager properties for this web application. See
	 * the LogManager, ConsoleHandler and FileHandler javadoc for details.
	 */
	private static Properties getDefaultLogManagerProperties() {
		Properties properties = new Properties();
		// Handlers for the root logger. All child loggers will send their
		// LogRecords to these handlers unless you disable the use of the
		// parent's Handlers in the child logger's configuration.
		properties.setProperty("handlers", "java.util.logging.ConsoleHandler," +
				"java.util.logging.FileHandler");
		// Level for the root logger. All child loggers will inherit this Level
		// unless they overwrite it with their own.
		properties.setProperty(".level", "CONFIG");
		// Level for ConsoleHandler. Set to ALL for simplicity of configuration.
		// You can set the logger's level to filter LogRecords.
		properties.setProperty("java.util.logging.ConsoleHandler.level", "ALL");
		// Level for FileHandler. See above.
		properties.setProperty("java.util.logging.FileHandler.level", "ALL");
		// Path and filename pattern for the log files. See FileHandler javadoc.
		properties.setProperty("java.util.logging.FileHandler.pattern",
				"log%g.txt");
		// (Approximate) max file size for log files, in bytes.
		properties.setProperty("java.util.logging.FileHandler.limit",
				"20000000" /* 20 MB */);
		// Number of log files to keep.
		properties.setProperty("java.util.logging.FileHandler.count", "5");
		// Enable appending to existing log file.
		properties.setProperty("java.util.logging.FileHandler.append", "true");
		return properties;
	}
	
	/**
	 * Parses the given String for key-value pairs and adds them to the given
	 * Properties object. If properties is null, the key-value pairs will be
	 * added to a new Properties object. Key-value pairs are separated by a
	 * comma (,). Any whitespace preceding or following a key-value pair will be
	 * removed. The key and value are separated by an equals sign (=). Any
	 * substring that doesn't contain an equals sign is ignored.
	 * @param propertiesString A comma-delimited list of key-value pairs
	 * @param properties The Properties to which the key-value pairs will be
	 * 		added. If null, a new Properties object will be used instead.
	 */
	private static Properties parseProperties(String propertiesString,
			Properties properties) {
		if (properties == null) {
			properties = new Properties();
		}
		Scanner scanner = new Scanner(propertiesString);
		scanner.useDelimiter(",");
		while (scanner.hasNext()) {
			String[] arg = scanner.next().split("=", 2);
			if (arg.length == 2) {
				String key = arg[0].trim();
				String value = arg[1].trim();
				properties.setProperty(key, value);
			}
		}
		return properties;
	}
	
	/**
	 * Passes the given Properties to the LogManager by exporting the Properties
	 * to a byte Stream and calling LogManager.readConfiguration(InputStream)
	 * @param properties The properties to pass to the LogManager
	 * @throws IOException Theoretically possible, but incredibly unlikely. I
	 * 		can think of no way that reading from or writing to a (growable)
	 * 		byte array could fail.
	 */
	private static void configureLogManager(Properties properties) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		properties.store(outputStream, null);
		byte[] streamBuffer = outputStream.toByteArray();
		LogManager logManager = LogManager.getLogManager();
		logManager.readConfiguration(new ByteArrayInputStream(streamBuffer));
		// Note: Closing a ByteArray[Input|Output]Stream has no effect.
	}
	
		
}
