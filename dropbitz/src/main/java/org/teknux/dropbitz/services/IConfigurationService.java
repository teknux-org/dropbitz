package org.teknux.dropbitz.services;

import org.teknux.dropbitz.config.ConfigurationFile;


/**
 * Manages configuration access and updates
 */
public interface IConfigurationService extends
		IService {

	public ConfigurationFile getConfiguration();
}
