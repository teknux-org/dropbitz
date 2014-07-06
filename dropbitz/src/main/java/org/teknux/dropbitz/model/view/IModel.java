package org.teknux.dropbitz.model.view;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public interface IModel {
    ServletContext getServletContext();

    void setServletContext(ServletContext context);
    
    HttpServletRequest getHttpServletRequest();

    void setHttpServletRequest(HttpServletRequest request);
}
