package com.dhs.portglass.IO;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Method for retrieving the contents of a sensor file. This class is invoked
 * by a thread whenever a file with (.txt) extension is added, edited, or 
 * removed from the Directory. According to the implementation, this may 
 * also include sub-directories of the monitored directory; see 
 * DirectoryWatcher for the implementation details. Currently, this will look
 * for the values corresponding to the following keys:
 * "sensor.name, sensor.id, sensor.location, sensor.status, sensor.date,
 *  sensor.time, sensor.description"
 */
public class PropertyConfig 
{
	private static final Logger logger =
			Logger.getLogger(PropertyConfig.class.getName());
	
	/**
	 * Analyzes the file at the given path, to see if the properties
	 * are defined properly. If the minimum requirements are met, then
	 * a String Array will be retrieved, containing the sensor data.
	 * All fields that are not required, and not present on the file,
	 * will be filled with an empty String. Required keys include:
	 * sensor.serial, sensor.status. 
	 * @param path Path location of the property file.
	 * @return String list of the sensor data, null of the file doesn't
	 * contain the minimum keys.
	 */
	public static String[] loadProperties(File path)
	{
		Properties prop = new Properties();
		String[] sensorDataEntry = null;
		try
		{
			
			
			prop.load(new FileInputStream(path));
			//Do not check existance in file: if not present, fail.
			String serial = prop.getProperty("sensor.serial");
			String status = prop.getProperty("sensor.status");
			if(serial == null || status == null) 
				throw new NullPointerException("Must Provide serial and status key");
			
			//Initialize sensorDataEntry Array;
			sensorDataEntry = new String[7];
			
			sensorDataEntry[0] = "";
			if (prop.containsKey("sensor.name"))
				sensorDataEntry[0]=prop.getProperty("sensor.name");
			
			sensorDataEntry[1] = "";
			if (prop.containsKey("sensor.location")) 
				sensorDataEntry[1]=prop.getProperty("sensor.location");
			
			sensorDataEntry[2] = status;
			sensorDataEntry[3] = serial;
			
			sensorDataEntry[4] = "";
			if (prop.containsKey("sensor.date")) 
				sensorDataEntry[4]=prop.getProperty("sensor.date");
			
			sensorDataEntry[5] = "";
			if (prop.containsKey("sensor.time")) 
				sensorDataEntry[5]=prop.getProperty("sensor.time");
			
			sensorDataEntry[6] = "";
			if (prop.containsKey("sensor.details")) 
				sensorDataEntry[6]=prop.getProperty("sensor.details");
			
			
			
		}
		catch(IOException | NullPointerException e)
		{
			System.out.println("pls");
			logger.log(Level.WARNING, "Verify that the path provided exists " +
					"and is available.", e); // TODO Verify Level
		}
		return sensorDataEntry;
	}
	
	
	public static void main(String[] args){
		String[] result = loadProperties(new File(System.getProperty("user.dir")+"/WebContent/WEB-INF/data/monitored/sensors/sensor1/test.txt"));
		
		for (int i=0; i<result.length; i++){
			System.out.println(result[i]);
		
		}
		
		
	}
}
