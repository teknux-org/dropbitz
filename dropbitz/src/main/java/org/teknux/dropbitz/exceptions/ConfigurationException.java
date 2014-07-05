package org.teknux.dropbitz.exceptions;

public class ConfigurationException extends DropBitzException {

	private static final long serialVersionUID = 1L;

	public ConfigurationException(String s) {
		super(s);
	}

	public ConfigurationException(Throwable throwable) {
		super(throwable);
	}

	public ConfigurationException(String s, Throwable throwable) {
		super(s, throwable);
	}
}
