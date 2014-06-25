package org.teknux.dropbitz.config;

import java.io.File;

import org.skife.config.Config;

public interface ConfigurationFile {

	@Config("secureId")
	String getSecureId();
	
	@Config("directory")
	File getDirectory();
}
