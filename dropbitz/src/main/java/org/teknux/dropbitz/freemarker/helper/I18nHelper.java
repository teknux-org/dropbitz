package org.teknux.dropbitz.freemarker.helper;

import java.util.List;
import java.util.Locale;

import org.teknux.dropbitz.exception.I18nServiceException;
import org.teknux.dropbitz.model.view.IModel;
import org.teknux.dropbitz.service.I18nService;
import org.teknux.dropbitz.service.ServiceManager;

import freemarker.core.Environment;
import freemarker.ext.beans.BeanModel;
import freemarker.ext.beans.StringModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class I18nHelper implements TemplateMethodModelEx {

    private final static String MODEL_VARIABLE = "model";

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        if (arguments.size() < 1 || arguments.size() > 2) {
            throw new TemplateModelException("Bad arguments");
        }
        String key = null;
        if (arguments.get(0).getClass() == SimpleScalar.class) {
            key = ((SimpleScalar) arguments.get(0)).getAsString();
        } else if (arguments.get(0).getClass() == StringModel.class){
            key = ((StringModel) arguments.get(0)).getAsString();
        } else {
            throw new TemplateModelException("Bad argument 1 type");
        }
        Locale locale = null;
        if (arguments.size() >= 2) {
            if (arguments.get(1).getClass() != SimpleScalar.class) {
                throw new TemplateModelException("Bad argument 2 type");
            } else {
                String lang = ((SimpleScalar) arguments.get(1)).getAsString();
                try {
                    locale = I18nService.getLocaleFromString(lang);
                } catch (I18nServiceException e) {
                    throw new TemplateModelException(e);
                }
            }
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

        ServiceManager serviceManager = ServiceManager.get(iModel.getServletContext());
        if (serviceManager == null) {
            throw new TemplateModelException("Can not get ServiceManager");
        }

        if (locale == null) {
            if (iModel.getLocale() != null) {
                locale = iModel.getLocale();
            } else if (iModel.getHttpServletRequest() != null) {
                locale = iModel.getHttpServletRequest().getLocale();
            }
        }
        
        return serviceManager.getI18nService().get(key, locale);
    }
}
