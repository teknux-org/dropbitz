package org.teknux.dropbitz.controller;

import java.util.ArrayList;
import java.util.List;
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
import org.teknux.dropbitz.service.ServiceManager;

public class AbstractController {

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
        m.setMessage(message);
        m.setType(type);
        if (closable != null) {
            m.setClosable(closable);
        }
        messages.add(m);
    }
    
    protected String i18n(String key) {
        return getServiceManager().getI18nService().get(key);
    }
}
