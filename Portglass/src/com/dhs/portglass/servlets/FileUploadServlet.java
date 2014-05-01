package com.dhs.portglass.servlets;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dhs.portglass.IO.ImageTools;
import com.dhs.portglass.IO.MultipartRequestHandler;
import com.dhs.portglass.dto.Account;
import com.dhs.portglass.dto.FileMeta;
import com.dhs.portglass.dto.Image;
import com.dhs.portglass.services.ImageManager;
import com.fasterxml.jackson.databind.ObjectMapper;

//this to be used with Java Servlet 3.0 API
@MultipartConfig 
@WebServlet("/s/upload")
/**
 * Builds on JQuery File Upload and Apache File Upload APIs.
 * Files that are added in a session through the use of this
 * servlet, get processed on the the <MultiparRequestHandler>
 * class. The files total size is analyzed and gradual reports
 * on download are sent back to the client-side request. The file
 * list is kept as an input stream unless the get method is 
 * called, where the indicated file index is processed and saved
 * in the database. 
 * 
 * For this implementation, multi-uploads are limited to a single
 * file. To change this, go to <MultipartRequestHandler> and 
 * change the upload file size.
 * 
 * This resource is protected through the <AuthorizationManager>.
 * Whenever the <AuthorizationFilter> detects that this resource is 
 * accessed, the <HttpSession>'s user will be verified. Only <Account>
 * users that have been added to the session may access this resource. 
 * @author Manuel Saldana
 *
 */
public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	/*
	 * Stores Meta Data on a file as it resides as an Output Stream
	 * on the server disk. Contains - file name, size, type, and
	 * the context stream.
	 */
	private static List<FileMeta> files = new LinkedList<FileMeta>();

	private static int MAX_FILES = 1; 

	
	/**
	 * Uploads an image <InputStream> located in the <FileMeta> files
	 * list of this class. The <InputStream> is processed by <ImageTools>
	 * so that the server is given the original, standard, and thumb-nail
	 * sizes. The location of these three images, all information on the 
	 * <FileMeta>, and the request parameter information, are joined to 
	 * create an <Image> POJO to be put into the database.
	 * 
	 * This method is handling one file at a time. For multi-file 
	 * processing, add a 'request.getParameter("index")' to indicate 
	 * the file that is needed. Also, modify the POST method of this class
	 * to handle more than 1 file. 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{

		String name        = request.getParameter("image_name");
		String type        = request.getParameter("image_type");
		String description = request.getParameter("image_description");
		String creator     = ((Account) request.getSession().getAttribute("user")).getEmail();
		String result = "false";

		/* Add this for multi-file processing */
		//String value = request.getParameter("fileIndex");

		/* 
		 * If implementing multi-file, change '0' for the integer value 
		 * of 'fileIndex'.
		 */
		if (files.isEmpty()){
			//show error
			result="noimage";
		}
		else
		{
			FileMeta file = files.get(0);

			try {	

				/* Get the files <InputStream>*/
				InputStream input = file.getContent();

				/* Upload to directories */
				System.out.println("Uploading to directories.");
				/* Get Date Created */
				java.util.Date date= new java.util.Date();
				String dateCreated = (new Timestamp(date.getTime())).toString() ;
				/* Hash Name with Time */
				String fileNameHashString = "pg"+(file.getFileName()+dateCreated).hashCode();
				/* Use this Hash to create the three images */
				if(ImageTools.saveStandardAndThumbImage(fileNameHashString, input)){
					/* Executed if the files where added to server disk */

					/* The file name that will be put in all three directories. */
					String fileName = fileNameHashString+".png";
					
					
					
					Image image = new Image(name, type, description, file.getFileSize(), dateCreated,
							creator, fileName);
					
					

					/* If image is added  to database*/
					if (ImageManager.getInstance().addImage(image)){
						// Set return message for success 
						result="true";
					}
				}
				input.close();
				// Set content type of the response so that jQuery knows what it can expect.
				response.setContentType("text/plain");   
				response.setCharacterEncoding("UTF-8"); 
				// Write response body.
					response.getWriter().write(result);
					

			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



	/**
	 * Buffers a client-side file into an <InputStream>. As the file is being 
	 * uploaded, the client receives gradual response notification via a progress
	 * bar update of completion (calculated with the file's total size). Makes 
	 * use of Apache File Upload API to buffer the stream. All File meta data
	 * is buffered in the request-scope by the <FileMeta> list of this class.
	 * 
	 *  To modify how many files are handled by this tool, change the value
	 *  in the while loop.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{



		/* Upload using the utility of Apache FileUpload API */
		files.addAll(MultipartRequestHandler.uploadByApacheFileUpload(request));

		/*
		 *  Remove files to hold the maximum accepted value. For this 
		 *  implementation, default is set at 1.
		 */
		while(files.size() > MAX_FILES)
		{
			files.remove(0);
		}

		/*
		 * JQuery API uses JSON response for progress bar 
		 * updates, therefore, set content type of response.
		 */
		response.setContentType("application/json");

		/* Convert List<FileMeta> into JSON format */
		ObjectMapper mapper = new ObjectMapper();

		/* Send result to client. */
		mapper.writeValue(response.getOutputStream(), files);

	}

}
