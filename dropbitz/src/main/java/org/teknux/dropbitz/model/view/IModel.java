package org.teknux.dropbitz.model.view;

import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.teknux.dropbitz.model.Message;

public interface IModel {
    ServletContext getServletContext();

    void setServletContext(ServletContext context);
    
    HttpServletRequest getHttpServletRequest();

    void setHttpServletRequest(HttpServletRequest request);
    
    List<Message> getMessages();

    void setMessages(List<Message> messages);
    
    Locale getLocale();
    
    void setLocale(Locale locale);
}
