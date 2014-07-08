package org.teknux.dropbitz.model.view;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.teknux.dropbitz.model.Message;

public class Model implements IModel {
    private ServletContext servletContext;
    private HttpServletRequest httpServletRequest;
    private List<Message> messages;
    
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

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
