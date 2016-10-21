package org.filestore.ejb.file;

import java.io.InputStream;
import java.util.List;

import org.filestore.ejb.file.entity.FileItem;

public interface FileService {
	
	public String postFile(String owner, List<String> receivers, String message, String name, InputStream stream) throws FileServiceException;
	
	public String postFile(String owner, List<String> receivers, String message, String name, byte[] data) throws FileServiceException;
	
	public FileItem getFile(String id) throws FileServiceException;
	
	public InputStream getFileContent(String id) throws FileServiceException;
	
	public byte[] getWholeFileContent(String id) throws FileServiceException;
	
	public void deleteFile(String id) throws FileServiceException;

}
