package org.teknux.dropbitz.config;

public class ConfigurationValidationException extends ConfigurationException {

	private static final long serialVersionUID = 3855898924513137363L;

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
