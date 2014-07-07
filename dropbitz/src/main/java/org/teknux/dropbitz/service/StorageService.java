package org.teknux.dropbitz.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.defragment.Defragment;
import com.db4o.ext.Db4oIOException;


/**
 * Service managing persistent storage from a file/database
 */
public class StorageService implements
		IService {

	public static final String DATABASE_NAME = "dropbitz.db";
	public static final String BACKUP_NAME = "dropbitz.db";

	private final Logger logger = LoggerFactory.getLogger(StorageService.class);

	private final String storageFile;

	private ObjectContainer objectContainer;

	public StorageService() {
		this(System.getProperty("java.io.tmpdir") + "/" + DATABASE_NAME);
	}

	public StorageService(String storageFile) {
		this.storageFile = storageFile;
	}

	@Override
	public void start() {
		logger.debug("Starting storage service...");

		try {
			final File dbFile = new File(storageFile);
			final Path dbFilePath = FileSystems.getDefault().getPath(storageFile);
			// backup and defragment
			if (dbFile.exists()) {
				final Path dbDirPath = dbFilePath.getParent();
				Objects.requireNonNull(dbDirPath, "Storage file has not parent directory");
				logger.trace("Optimizing storage");
				Defragment.defrag(dbFile.getPath(), dbDirPath.resolve(BACKUP_NAME).toString());
			}
			logger.trace("Opening storage file");
			objectContainer = Db4oEmbedded.openFile(storageFile);

			logger.debug("Storage service started.");

		} catch (IOException | Db4oIOException e) {
			logger.error("Error opening storage file", e);
		}
	}

	@Override
	public void stop() {
		if (objectContainer != null) {
			objectContainer.close();
			objectContainer = null;
		}
	}

	/**
	 * Get an {@link ObjectContainer} to deal with the low level storage system.
	 * 
	 * @return the low level storage container.
	 * @throws InvalidParameterException
	 *             if the service was not started prior to calling this method
	 */
	public ObjectContainer getObjectContainer() {
		if (objectContainer == null) {
			throw new InvalidParameterException("Storage service is not started yet.");
		}

		return objectContainer.ext().openSession();
	}
}
