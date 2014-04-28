package com.dhs.portglass.dto;


import java.io.InputStream;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties({"content"})
/**
 * This POJO is used to map the File <InputStream> meta
 * data when a client-side request uploads a file stream.
 * It encapsulates data utilized by the system when the
 * <Image> object is going to be created.
 * @author Manuel R Saldana
 *
 */
public class FileMeta {
	
	
	private String fileName;
	private String fileSize;
	private String fileType;
	private InputStream content;
	
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public InputStream getContent(){
		return this.content;
	}
	public void setContent(InputStream content){
		this.content = content;
	}
	
	@Override
	/**
	 * Utility method to present the meta data back to the
	 * user when the file uploads completely.
	 */
	public String toString() {
		return "FileMeta [fileName=" + fileName + ", fileSize=" + fileSize
				+ ", fileType=" + fileType + "]";
	}
	
	
	
}
