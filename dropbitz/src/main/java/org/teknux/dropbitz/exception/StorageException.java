package org.teknux.dropbitz.exception;

/**
 * Exception thrown when a storage problem occured
 */
public class StorageException extends Exception {

	private static final long serialVersionUID = 1L;

	public StorageException() {
		super();
	}

	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}

	public StorageException(String message) {
		super(message);
	}

	public StorageException(Throwable cause) {
		super(cause);
	}

}
