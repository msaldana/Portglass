package com.dhs.portglass.servlets;




import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//this to be used with Java Servlet 3.0 API
@MultipartConfig 
@WebServlet("/s/image")
/**
 * Invoked from client-side when an image is desired to be
 * shown in the main application. Receives type and file 
 * parameters on the doPost() method in order to depict the
 * file and location containing the desired image data. Type
 * can have 3 values (1, 2, or 3): representing the original,
 * standard, and thumbnail image directories. 
 * The image is converted to a <FileInputStream> and passed
 * to the client-side through the <HttpServletResponse> 
 * <OutputStream> writer. All data is sent in a bitwise array,
 * formated with PNG context type.
 * @author Manuel R Saldana 
 *
 */
public class ImageRetrieveServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	/* Location of Image Directories */
	private static final String IMAGE_ORIGINAL = "/Users/Manuel/git/Portglass/Portglass/WebContent/WEB-INF/data/images/original/";
	private static final String IMAGE_STANDARD = "/Users/Manuel/git/Portglass/Portglass/WebContent/WEB-INF/data/images/standard/";
	private static final String IMAGE_THUMBNAIL = "/Users/Manuel/git/Portglass/Portglass/WebContent/WEB-INF/data/images/thumbnail/";

	
	/**
	 * Unimplemented, redirects to POST.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		doPost(request, response);
	}
	
	
	/**
	 * Buffers a server-side file into an <InputStream>. The file is then written
	 * bit by bit to the response. Default size for <OutputStream> is set to 10
	 * MB and the content type is set to image/png. If the request parameter type
	 * is set to 1, the original photo is fetched, 2 fetches the standard image
	 * size, and 3 fetches the thumbnail.
	 * See this projects web.xml for a declaration of these sizes and directory
	 * locations.
	 * 
	 *  To modify how many files are handled by this tool, change the value
	 *  in the while loop.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		
		String directory;
		
		/* Verify the image type that is needed to retrieve */
		
		switch (Integer.parseInt(request.getParameter("type"))){
			case 1: directory = IMAGE_ORIGINAL;
					break;
			case 2: directory = IMAGE_STANDARD;
					break;
			case 3: directory = IMAGE_THUMBNAIL;
					break;
			default: directory = "";
					break;
		}

		/* Get the file to retrieve */
		String value = request.getParameter("file");
		System.out.println(directory+value);

		/* Buffer a new file */ 
		File file = new File(directory+value);
	
		/* Set ContentResponse Type */
        response.setContentType("image/png");

        /* Set content size */
        response.setContentLength((int)file.length());

        /* Open the file and output streams */
        FileInputStream in = new FileInputStream(file);
        OutputStream out = response.getOutputStream();

        /*
         *  Copy the contents of the file to the output stream
         *  Default Max size is 10MB
         */
        byte[] buf = new byte[1024*10];
        int count = 0;
        while ((count = in.read(buf)) >= 0) {
            out.write(buf, 0, count);
        }
        in.close();
        out.close();
		
	}
}
