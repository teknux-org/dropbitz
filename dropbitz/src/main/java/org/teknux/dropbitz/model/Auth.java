package org.teknux.dropbitz.model;

/**
 * Authentication model
 */
public class Auth {

    private boolean isLogged;
    private boolean isAuthorized;

    public Auth(boolean isLogged, boolean isAuthorized) {
        this.isLogged = isLogged;
        this.isAuthorized = isAuthorized;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean isLogged) {
        this.isLogged = isLogged;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public void setAuthorized(boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
    }
}
