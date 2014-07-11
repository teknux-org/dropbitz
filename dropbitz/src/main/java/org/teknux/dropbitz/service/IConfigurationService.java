package org.teknux.dropbitz.service;

import org.teknux.dropbitz.config.Configuration;

public interface IConfigurationService extends IService{
    Configuration getConfiguration();
}
