package com.dhs.portglass.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;

public class IO {
	
	/**
	 * Serializes an object. The object must be serializable.
	 */
	public static void serialize(Object serializable, Path file) throws IOException {
		FileOutputStream fileOutStream =
				new FileOutputStream(file.toFile());
		ObjectOutputStream serializer = new ObjectOutputStream(fileOutStream);
		serializer.writeObject(serializable);
		serializer.close();
	}
	
	/**
	 * Deserializes an object.
	 */
	public static Object deserialize(Path file) throws IOException,
			ClassNotFoundException {
		FileInputStream fileInStream = new FileInputStream(file.toFile());
		ObjectInputStream deserializer = new ObjectInputStream(fileInStream);
		Object object = deserializer.readObject();
		deserializer.close();
		return object;
	}
	
}
