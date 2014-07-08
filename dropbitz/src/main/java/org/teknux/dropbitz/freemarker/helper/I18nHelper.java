package org.teknux.dropbitz.freemarker.helper;

import java.util.List;

import org.teknux.dropbitz.model.view.IModel;
import org.teknux.dropbitz.service.ServiceManager;

import freemarker.core.Environment;
import freemarker.ext.beans.BeanModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class I18nHelper implements TemplateMethodModelEx {

    private final static String MODEL_VARIABLE = "model";

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        if (arguments.size() != 1) {
            throw new TemplateModelException("Specify argument key");
        }
        if (arguments.get(0).getClass() != SimpleScalar.class) {
            throw new TemplateModelException("Bad argument type");
        }          
        String key = ((SimpleScalar) arguments.get(0)).getAsString();

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

        ServiceManager serviceManager = ServiceManager.get(iModel.getServletContext());
        if (serviceManager == null) {
            throw new TemplateModelException("Can not get ServiceManager");
        }

        return serviceManager.getI18nService().get(key);
    }
}
