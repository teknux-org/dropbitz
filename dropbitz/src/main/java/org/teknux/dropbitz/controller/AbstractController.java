package org.teknux.dropbitz.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;

import org.glassfish.jersey.server.mvc.Viewable;
import org.teknux.dropbitz.freemarker.View;
import org.teknux.dropbitz.model.Message;
import org.teknux.dropbitz.model.view.IModel;
import org.teknux.dropbitz.model.view.Model;
import org.teknux.dropbitz.service.II18nService;
import org.teknux.dropbitz.service.ServiceManager;

abstract public class AbstractController {

    private List<Message> messages = null;

    @Inject
    private ServletContext servletContext;

    @Context
    private HttpServletRequest httpServletRequest;

    protected ServletContext getServletContext() {
        return Objects.requireNonNull(servletContext);
    }

    protected HttpServletRequest getHttpServletRequest() {
        return Objects.requireNonNull(httpServletRequest);
    }

    protected HttpSession getSession() {
        return Objects.requireNonNull(httpServletRequest).getSession();
    }

    protected ServiceManager getServiceManager() {
        return Objects.requireNonNull(ServiceManager.get(getServletContext()));
    }

    protected Viewable viewable(View view) {
        return viewable(view, new Model());
    }

    protected Viewable viewable(View view, IModel model) {
        return new Viewable(Objects.requireNonNull(view).getTemplateName(), initModel(Objects.requireNonNull(model, "model can not be null")));
    }

    private IModel initModel(IModel model) {
        model.setServletContext(servletContext);
        model.setHttpServletRequest(httpServletRequest);
        model.setMessages(messages);

        return model;
    }

    protected void addMessage(String message, Message.Type type) {
        addMessage(null, message, type, null);
    }

    protected void addMessage(String id, String message, Message.Type type) {
        addMessage(id, message, type, null);

    }

    protected void addMessage(String message, Message.Type type, boolean closable) {
        addMessage(null, message, type, closable);

    }

    protected void addMessage(String id, String message, Message.Type type, Boolean closable) {
        if (messages == null) {
            messages = new ArrayList<Message>();
        }
        
        Message m = new Message();
        if (id != null) {
            m.setId(id);
        } else {
            m.setId(UUID.randomUUID().toString());
        }
        m.setMessage(Objects.requireNonNull(message, "message can not be null"));
        m.setType(Objects.requireNonNull(type, "type can not be null"));
        if (closable != null) {
            m.setClosable(closable);
        }
        messages.add(m);
    }
    
    protected String i18n(String key) {
        return i18n(key, null);
    }
    
    protected String i18n(String key, Locale locale) {
        if (locale == null) {
            locale = getHttpServletRequest().getLocale();
        }
        return getServiceManager().getService(II18nService.class).get(key, locale);
    }
    
    protected String url(String route) {
        return getServletContext().getContextPath() + route;
    }
    
    protected URI uri(String route) throws URISyntaxException {
        return new URI(url(route));
    }
}
