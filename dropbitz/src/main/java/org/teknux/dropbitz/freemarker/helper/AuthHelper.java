package org.teknux.dropbitz.freemarker.helper;

import freemarker.template.TemplateModelException;
import org.teknux.dropbitz.model.Auth;
import org.teknux.dropbitz.model.view.IModel;
import org.teknux.dropbitz.provider.AuthenticationHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class AuthHelper extends AbstractHelper {

    private AuthenticationHelper authenticationHelper;

    public AuthHelper() {
        this(new AuthenticationHelper());
    }

    public AuthHelper(AuthenticationHelper authenticationHelper) {
        this.authenticationHelper = authenticationHelper;
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

        return new Auth(authenticationHelper.isLogged(httpServletRequest), authenticationHelper.isAuthorized(httpServletRequest));
    }
}
