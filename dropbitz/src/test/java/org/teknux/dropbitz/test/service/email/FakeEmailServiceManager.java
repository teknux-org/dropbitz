package org.teknux.dropbitz.test.service.email;

import java.io.File;

import javax.servlet.ServletContext;

import org.teknux.dropbitz.config.Configuration;
import org.teknux.dropbitz.exception.ServiceException;
import org.teknux.dropbitz.service.IConfigurationService;
import org.teknux.dropbitz.service.IService;
import org.teknux.dropbitz.service.IServiceManager;

public class FakeEmailServiceManager implements IServiceManager {

    private boolean emailEnable;
    private String emailFrom;
    private String[] emailTo;

    public FakeEmailServiceManager(boolean emailEnable, String emailFrom, String emailTo[]) {
        this.emailEnable = emailEnable;
        this.emailFrom = emailFrom;
        this.emailTo = emailTo;
    }
    
    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IService> T getService(Class<T> serviceClass) {
        if (serviceClass == IConfigurationService.class) {
            return (T) new IConfigurationService() {
                
                @Override
                public void stop() throws ServiceException {                    
                }
                
                @Override
                public void start(IServiceManager serviceManager) throws ServiceException {                    
                }
                
                @Override
                public Configuration getConfiguration() {
                    return getConfig();
                }
            };
        } else {
            return null;
        }
    }
    
    private Configuration getConfig() {
        return new Configuration() {

            @Override
            public boolean isDebug() {
                return false;
            }
            
            @Override
            public String getBasePath() {
                return null;
            }

            @Override
            public String getSecureId() {
                return null;
            }

            @Override
            public File getDirectory() {
                return null;
            }

            @Override
            public boolean isSsl() {
                return false;
            }

            @Override
            public int getPort() {
                return 0;
            }

            @Override
            public String getTitle() {
                return null;
            }

            @Override
            public boolean isEmailEnable() {
                return emailEnable;
            }

            @Override
            public String getEmailHost() {
                return null;
            }

            @Override
            public int getEmailPort() {
                return 0;
            }

            @Override
            public boolean getEmailSsl() {
                return false;
            }

            @Override
            public String getEmailUsername() {
                return null;
            }

            @Override
            public String getEmailPassword() {
                return null;
            }

            @Override
            public String getEmailFrom() {
                return emailFrom;
            }

            @Override
            public String[] getEmailTo() {
                return emailTo;
            }

            @Override
            public String getEmailLang() {
                return null;
            }
        };
    }
};
