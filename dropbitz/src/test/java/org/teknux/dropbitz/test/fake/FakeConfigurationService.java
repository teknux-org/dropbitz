package org.teknux.dropbitz.test.fake;

import org.teknux.dropbitz.config.Configuration;
import org.teknux.dropbitz.exception.ServiceException;
import org.teknux.dropbitz.service.IConfigurationService;
import org.teknux.dropbitz.service.IServiceManager;

public class FakeConfigurationService implements IConfigurationService {

    private Configuration configuration;
    
    public FakeConfigurationService(Configuration configuration) {
        this.configuration = configuration;
    }
    
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void start(IServiceManager serviceManager) throws ServiceException {
    }

    @Override
    public void stop() throws ServiceException {        
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
