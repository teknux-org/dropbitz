package org.teknux.dropbitz.config;

import java.io.File;

import org.skife.config.Config;
import org.skife.config.Default;

public interface ConfigurationFile {

	@Config("secureId")
	@Default("")
	String getSecureId();
	
	@Config("directory")
	File getDirectory();
	
	@Config("ssl")
	@Default("false")
	boolean isSsl();
	
	@Config("port")
	@Default("8080")
	int getPort();
	
	@Config("title")
	@Default("DropBitz")
	String getTitle();
}
