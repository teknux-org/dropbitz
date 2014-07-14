package org.teknux.dropbitz.service.email;

import org.teknux.dropbitz.exception.EmailServiceException;
import org.teknux.dropbitz.model.view.IModel;

public interface IEmailTemplateResolver {
    
    String resolve(IModel model, String viewName) throws EmailServiceException;
}
