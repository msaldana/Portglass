package com.dhs.portglass.IO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import com.mortennobel.imagescaling.ResampleOp;

/**
 * Utility IO class to resize images without losing aspect ratio.
 * Invoked by the <FileUploadServlet>, this will create three files
 * in the disclosed locations with the default extension set at PNG.
 * Its only method, returns a boolean indicating if the conversion
 * was successful, so that the Servlet knows whether or not to create
 * the the database entry.
 * @author Manuel R Saldana
 *
 */
public class ImageTools {

	/*
	 * Location of the three directories used to store processed images. 
	 */
	private static final String STORE_DIR_STANDARD  = "/Users/Manuel/git/Portglass/Portglass/WebContent/WEB-INF/data/images/standard/";
	private static final String STORE_DIR_THUMBNAIL = "/Users/Manuel/git/Portglass/Portglass/WebContent/WEB-INF/data/images/thumbnail/";
	private static final String STORE_DIR_ORIGINAL  = "/Users/Manuel/git/Portglass/Portglass/WebContent/WEB-INF/data/images/original/";

	/*
	 * Definition of image properties in pixels.
	 */
	private static final int STANDARD_THUMBNAIL_SIZE = 100;
	private static final int STANDARD_IMAGE_SIZE = 800;
	private static final String EXTENSION = "png";


	/**
	 * Uses an <InputStream> object containing the image data in order to scale its
	 * dimensions to a standard size for site images; as well as a standard for 
	 * thumbnails. Once the new dimensions are determined, the image is scaled
	 * and stored in the app-defined default directories 
	 * Usage: provide newFileName without extension (.png, .jpeg, etc), the method
	 * will append a pre-defined extension for images. See the web.xml configuration
	 * for the declaration of configurable variables.
	 * @param newFileName Filename for scaled images (without extension) 
	 * @param imageInputStream A FileInputStream containing the image data
	 * @return A boolean stating if the entire image processing was executed, and 
	 * images stored - according to the defined locations.
	 */
	public static boolean saveStandardAndThumbImage(String newFileName, InputStream imageInputStream) 
	{
		boolean isSuccessful = false;
		try{
			BufferedImage srcImage = ImageIO.read(imageInputStream);
			// Save the Original Image in the pre-determined directory.
			ImageIO.write(srcImage, EXTENSION, new File(STORE_DIR_ORIGINAL, newFileName+"."+EXTENSION));

			int actualHeight = srcImage.getHeight();
			int actualWidth = srcImage.getWidth();
			double imageRatio = (double) actualWidth / (double) actualHeight;

			/* 
			 * Adjust size, if necessary, for the standard image file
			 * and store it in the pre-determined directory.
			 */
			int height, width;
			if (actualHeight > STANDARD_IMAGE_SIZE || actualWidth > STANDARD_IMAGE_SIZE) {
				height = width = STANDARD_IMAGE_SIZE;
				if (imageRatio > 1) // 1 is standard ratio
					height = (int) (STANDARD_IMAGE_SIZE / imageRatio);
				else
					width = (int) (STANDARD_IMAGE_SIZE * imageRatio);
			} else {
				height = actualHeight;
				width = actualWidth;
				width = actualWidth;
			}

			ResampleOp resampleOp = new ResampleOp(width, height);
			BufferedImage scaledImage = resampleOp.filter(srcImage, null);
			// Save the Image into the pre-defined 'Standard' Directory
			ImageIO.write(scaledImage, EXTENSION, new File(STORE_DIR_STANDARD, newFileName+"."+EXTENSION));


			/* 
			 * Adjust size, if necessary, for the thumbnail image file
			 * and store it in the pre-determined directory.
			 */
			resampleOp = null;
			scaledImage = null;
			if (actualHeight > STANDARD_THUMBNAIL_SIZE || actualWidth > STANDARD_THUMBNAIL_SIZE) { 
				height = width = STANDARD_THUMBNAIL_SIZE;
				if (imageRatio > 1)
					height = (int) (STANDARD_THUMBNAIL_SIZE / imageRatio);
				else
					width = (int) (STANDARD_THUMBNAIL_SIZE * imageRatio);
			} else {
				height = actualHeight;
				width = actualWidth;
			}
			resampleOp = new ResampleOp(width, height);
			scaledImage = resampleOp.filter(srcImage, null);

			ImageIO.write(scaledImage, EXTENSION, new File(STORE_DIR_THUMBNAIL, newFileName+"."+EXTENSION));
			/* 
			 * All processing was executed, whether the images, at this point, 
			 * are in the expected directory depends on the configuration 
			 */
			isSuccessful=true;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return isSuccessful;

	}
}
