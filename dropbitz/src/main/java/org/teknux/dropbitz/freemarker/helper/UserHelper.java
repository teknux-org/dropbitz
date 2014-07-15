package org.teknux.dropbitz.freemarker.helper;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.teknux.dropbitz.model.view.IModel;
import org.teknux.dropbitz.provider.AuthenticationHelper;

import freemarker.template.TemplateModelException;

public class UserHelper extends AbstractHelper {

    private AuthenticationHelper authenticationHelper;

    public UserHelper() {
        authenticationHelper = new AuthenticationHelper();
    }

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        if (arguments.size() != 0) {
            throw new TemplateModelException("Do not specify argument");
        }

        IModel iModel = getIModel();

        HttpServletRequest httpServletRequest = iModel.getHttpServletRequest();
        if (httpServletRequest == null) {
            throw new TemplateModelException("Can not get HttpServletRequest");
        }

        return authenticationHelper.isLogged(httpServletRequest);
    }
}
