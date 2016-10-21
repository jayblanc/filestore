package org.filestore.ejb.file.metrics;

import org.filestore.ejb.file.FileServiceException;

public interface FileServiceMetrics {
	
	public int getTotalUploads() throws FileServiceException;
	
	public int getTotalDownloads() throws FileServiceException;
	
	public int getUptime() throws FileServiceException;

}
