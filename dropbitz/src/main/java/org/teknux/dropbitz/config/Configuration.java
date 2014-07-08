package org.teknux.dropbitz.config;

import java.io.File;

import org.skife.config.Config;
import org.skife.config.Default;
import org.skife.config.DefaultNull;

public interface Configuration {

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
	@Default("")
	String getTitle();
	
	@Config("email.enable")
	@Default("false")
	boolean isEmailEnable();
	
	@Config("email.host")
	@DefaultNull
	String getEmailHost();
	
	@Config("email.port")
	@Default("25")
	int getEmailPort();
	
	@Config("email.ssl")
	@Default("false")
	boolean getEmailSsl();
	
	@Config("email.username")
	@DefaultNull
	String getEmailUsername();
	
	@Config("email.password")
	@DefaultNull
	String getEmailPassword();
	
	@Config("email.from")
	@DefaultNull
	String getEmailFrom();
	
	@Config("email.to")
	@DefaultNull
	String[] getEmailTo();
}
