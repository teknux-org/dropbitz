package org.teknux.dropbitz.service;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.mapdb.Atomic;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.exception.ServiceException;


/**
 * Service managing persistent storage from a file/database
 */
public class StorageService implements
		IService {

	public static final String DATABASE_NAME = "dropbitz.db";
	public static final String BACKUP_NAME = "dropbitz.db.bak";

	public enum Storage {
		USERS, CODES, FILES;
	}

	private final Logger logger = LoggerFactory.getLogger(StorageService.class);

	private final String storageFile;

	private DB database;

	public StorageService() {
		this(System.getProperty("java.io.tmpdir") + "/" + DATABASE_NAME);
	}

	public StorageService(String storageFile) {
		this.storageFile = storageFile;
	}

	@Override
	public void start(final ServiceManager serviceManager) throws ServiceException {
		try {
			final File dbFile = new File(storageFile);
			backupStorage(dbFile);
			logger.trace("Opening storage file");
			database = DBMaker.newFileDB(dbFile).cacheDisable().closeOnJvmShutdown().transactionDisable().make();
			logger.trace("Optimizing storage file");
			database.compact();

		} catch (IOError | IOException e) {
			throw new ServiceException("Error opening storage file", e);
		}
	}

	private void backupStorage(final File dbFile) throws IOException {
		final Path dbFilePath = FileSystems.getDefault().getPath(storageFile);
		if (dbFile.exists()) {
			final Path dbDirPath = dbFilePath.getParent();
			Objects.requireNonNull(dbDirPath, "Storage file has not parent directory");
			final Path backupFile = dbDirPath.resolve(BACKUP_NAME);
			logger.trace("Creating storage backup");
			Files.copy(dbFilePath, backupFile, StandardCopyOption.REPLACE_EXISTING);
		}
	}

	@Override
	public void stop() {
		if (database != null) {
			database.commit();
			database.close();
		}
	}

	public <K, V> Map<K, V> getStorageMap(Storage storage) {
		if (database == null) {
			throw new IllegalArgumentException("Storage service has not started yet");
		}
		return database.createHashMap(storage.name()).makeOrGet();
	}

	public Set<Long> getStorageSet(Storage storage) {
		if (database == null) {
			throw new IllegalArgumentException("Storage service has not started yet");
		}
		return database.createTreeSet(storage.name()).makeLongSet();
	}

	public Atomic.Long getAtomicLong(Storage storage) {
		if (database == null) {
			throw new IllegalArgumentException("Storage service has not started yet");
		}
		return database.createAtomicLong(storage + "-id", 0);
	}
}
