package org.teknux.dropbitz.exceptions;

public class ConfigurationValidationException extends ConfigurationException {

	private static final long serialVersionUID = 1L;

	public ConfigurationValidationException(String s) {
		super(s);
	}

	public ConfigurationValidationException(Throwable throwable) {
		super(throwable);
	}

	public ConfigurationValidationException(String s, Throwable throwable) {
		super(s, throwable);
	}
}
