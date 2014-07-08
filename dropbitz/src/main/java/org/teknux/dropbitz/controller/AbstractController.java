package org.teknux.dropbitz.controller;

import java.util.Objects;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;

import org.glassfish.jersey.server.mvc.Viewable;
import org.teknux.dropbitz.freemarker.View;
import org.teknux.dropbitz.model.view.IModel;
import org.teknux.dropbitz.model.view.Model;
import org.teknux.dropbitz.service.ServiceManager;

public class AbstractController {
	
    @Inject
    private ServletContext servletContext;
    
    @Context
    private HttpServletRequest httpServletRequest;
    
    public ServletContext getServletContext() {
        return Objects.requireNonNull(servletContext);
    }
    
    public HttpServletRequest getHttpServletRequest() {
        return Objects.requireNonNull(httpServletRequest);
    }
    
    public HttpSession getSession() {
        return Objects.requireNonNull(httpServletRequest).getSession();
    }
    
    public ServiceManager getServiceManager() {
        return ServiceManager.get(getServletContext());
    }
    
    protected Viewable viewable(View view) {
        return viewable(view, new Model());
    }
    
    protected Viewable viewable(View view, IModel model) {
        return new Viewable(Objects.requireNonNull(view).getTemplateName(), initModel(model));
    }
    
    private IModel initModel(IModel model) {
        model.setServletContext(servletContext);
        model.setHttpServletRequest(httpServletRequest);
        
        return model;
    }
}
