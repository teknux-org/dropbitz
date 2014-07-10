package org.teknux.dropbitz.freemarker.helper;

import java.util.List;

import javax.servlet.ServletContext;

import org.teknux.dropbitz.model.view.IModel;

import freemarker.core.Environment;
import freemarker.ext.beans.BeanModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class UrlHelper implements TemplateMethodModelEx {

    private final static String MODEL_VARIABLE = "model";

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        if (arguments.size() > 1) {
            throw new TemplateModelException("Bad arguments");
        }
        String path = "";
        if (arguments.size() == 1) {
            if (arguments.get(0).getClass() != SimpleScalar.class) {
                throw new TemplateModelException("Bad argument 1 type");
            }
            path = ((SimpleScalar) arguments.get(0)).getAsString();
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
        
        if (iModel.getServletContext() == null) {
            throw new TemplateModelException("Can not get ServletContext");
        }
        ServletContext servletContext = iModel.getServletContext();
        
        return servletContext.getContextPath() + path;
    }
}
