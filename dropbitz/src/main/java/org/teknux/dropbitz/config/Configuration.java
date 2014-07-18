/*
 * Copyright (C) 2014 TekNux.org
 *
 * This file is part of the dropbitz Community GPL Source Code.
 *
 * dropbitz Community Source Code is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * dropbitz Community Source Code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with dropbitz Community Source Code.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.teknux.dropbitz.config;

import java.io.File;
import java.util.List;

import org.skife.config.Config;
import org.skife.config.Default;
import org.skife.config.DefaultNull;

public interface Configuration {

    @Config("debug")
    @Default("false")
    boolean isDebug();
    
    @Config("basePath")
    @Default("/")
    String getBasePath();
    
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
	
    @Config("header.title")
    @DefaultNull
    String getHeaderTitle();
	
	@Config("header.logo")
    @DefaultNull
    String getHeaderLogo();
	
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
	boolean isEmailSsl();
	
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
	List<String> getEmailTo();
	
	@Config("email.lang")
    @DefaultNull
    String getEmailLang();
}
