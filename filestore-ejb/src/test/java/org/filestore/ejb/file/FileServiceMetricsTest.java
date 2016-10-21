package org.filestore.ejb.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.naming.InitialContext;

import org.filestore.ejb.file.metrics.FileServiceMetrics;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class FileServiceMetricsTest {

	private static final Logger LOGGER = Logger.getLogger(FileServiceMetricsTest.class.getName());

	@EJB
	private FileService service;
	@EJB
	private FileServiceMetrics metrics;
	@ArquillianResource
	InitialContext initialContext;

	@Deployment
	public static EnterpriseArchive createDeployment() {
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "filestore-ejb.jar");
		jar.addPackage("org.filestore.ejb.config");
		jar.addPackage("org.filestore.ejb.file");
		jar.addPackage("org.filestore.ejb.file.entity");
		jar.addPackage("org.filestore.ejb.file.metrics");
		jar.addPackage("org.filestore.ejb.store");
		jar.addAsManifestResource("test-persistence.xml", "persistence.xml");
		LOGGER.log(Level.INFO, "Created JAR for test : " + jar.toString(true));

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "filestore-ear.ear");
		ear.addAsModule(jar);
		LOGGER.log(Level.INFO, "Created EAR for test : " + ear.toString(true));

		return ear;
	}

	@Test
	public void testUploadFiles() throws FileServiceException, IOException {
		
		int uploadsBefore = metrics.getTotalUploads();
		int downloadsBefore = metrics.getTotalDownloads();
		
		List<String> receivers = new ArrayList<String> ();
		receivers.add("sheldon@tbbt.org");
		String key = service.postFile("jayblanc@gmail.com", receivers, "This is a message for you", "file.txt", "BAZINGA !!".getBytes());
		assertNotNull(key);
		
		int nbdownloads = 3;
		for (int i=0; i<nbdownloads; i++) {
			service.getWholeFileContent(key);
		}
		
		int uploadsAfter = metrics.getTotalUploads();
		int downloadAfter = metrics.getTotalDownloads();
		assertEquals(uploadsBefore + 1, uploadsAfter);
		assertEquals(downloadsBefore + nbdownloads, downloadAfter);

	}

}
