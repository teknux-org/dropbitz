package org.teknux.dropbitz.config;

import org.skife.config.Config;

public interface ConfigurationFile {

	@Config("secureId")
	String getSecureId();
}
