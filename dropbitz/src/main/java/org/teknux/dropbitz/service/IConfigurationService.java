package org.teknux.dropbitz.service;

import org.teknux.dropbitz.config.Configuration;


/**
 * Manages configuration access and updates
 */
public interface IConfigurationService extends
		IService {

	public Configuration getConfiguration();
}
