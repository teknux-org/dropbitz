package org.teknux.dropbitz.freemarker.helper;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.teknux.dropbitz.model.view.IModel;
import org.teknux.dropbitz.provider.AuthenticationHelper;

import freemarker.core.Environment;
import freemarker.ext.beans.BeanModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class UserHelper implements TemplateMethodModelEx {

    private final static String MODEL_VARIABLE = "model";

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        if (arguments.size() != 0) {
            throw new TemplateModelException("Do not specify argument");
        }

        Environment environment = Environment.getCurrentEnvironment();
        if (environment == null) {
            throw new TemplateModelException("Can not get environment");
        }
        TemplateModel templateModel = environment.getVariable(MODEL_VARIABLE);
        if (!(templateModel instanceof BeanModel)) {
            throw new TemplateModelException("TemplateModel is not a instance of BeanModel");
        }
        BeanModel beanModel = (BeanModel) templateModel;
        Object wrappedObject = beanModel.getWrappedObject();
        if (!(wrappedObject instanceof IModel)) {
            throw new TemplateModelException("WrappedObject is not a instance of IModel");
        }
        IModel iModel = (IModel) beanModel.getWrappedObject();

        HttpServletRequest httpServletRequest = iModel.getHttpServletRequest();
        if (httpServletRequest == null) {
            throw new TemplateModelException("Can not get HttpServletRequest");
        }

        return AuthenticationHelper.isSecured(httpServletRequest);
    }
}
