package org.teknux.dropbitz.test.fake;

import java.io.File;

import org.teknux.dropbitz.config.Configuration;

public class FakeConfiguration implements Configuration {
    private boolean emailEnable;
    private String emailFrom;
    private String[] emailTo;
    
    public void setEmailEnable(boolean emailEnable) {
        this.emailEnable = emailEnable;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public void setEmailTo(String[] emailTo) {
        this.emailTo = emailTo;
    }

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
}
