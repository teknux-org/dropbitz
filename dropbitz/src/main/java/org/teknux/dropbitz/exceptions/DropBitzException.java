package org.teknux.dropbitz.exceptions;

/**
 * Base for DropBitz all exceptions
 */
public class DropBitzException extends Exception {

	private static final long serialVersionUID = 1L;

	public DropBitzException() {
		super();
	}

	public DropBitzException(String message, Throwable cause) {
		super(message, cause);
	}

	public DropBitzException(String message) {
		super(message);
	}

	public DropBitzException(Throwable cause) {
		super(cause);
	}

}
