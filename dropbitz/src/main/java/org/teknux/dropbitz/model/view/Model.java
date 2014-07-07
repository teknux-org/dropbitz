package org.teknux.dropbitz.model.view;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class Model implements IModel {
    private ServletContext servletContext;
    private HttpServletRequest httpServletRequest;
    
    public Model() {
        
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext context) {
        this.servletContext = context;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setHttpServletRequest(HttpServletRequest request) {
        this.httpServletRequest = request;
    }
}
