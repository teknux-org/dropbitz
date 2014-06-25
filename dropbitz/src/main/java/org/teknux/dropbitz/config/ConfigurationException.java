package org.teknux.dropbitz.config;

public class ConfigurationException extends Exception {

	private static final long serialVersionUID = 3855898924513137363L;

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
