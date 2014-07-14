package org.teknux.dropbitz.freemarker.helper;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.teknux.dropbitz.model.view.IModel;
import org.teknux.dropbitz.service.II18nService;
import org.teknux.dropbitz.service.IServiceManager;
import org.teknux.dropbitz.service.ServiceManager;

import freemarker.template.TemplateModelException;

public class I18nHelper extends AbstractHelper {

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        if (arguments.size() < 1) {
            throw new TemplateModelException("Bad arguments");
        }
        //Get I18n key
        String key = getString(arguments.get(0));
        
        //Get optionnal format attributes
        List<Object> messageFormatAttributes = new ArrayList<Object>();
        if (arguments.size() > 1) {
            for (Object object : arguments.subList(1, arguments.size())) {
                messageFormatAttributes.add(getString(object));
            }
        }
        
        IModel iModel = getIModel();
        
        if (iModel.getServletContext() == null) {
            throw new TemplateModelException("Can not get ServletContext");
        }

        IServiceManager serviceManager = ServiceManager.get(iModel.getServletContext());
        if (serviceManager == null) {
            throw new TemplateModelException("Can not get ServiceManager");
        }

        Locale locale = null;
        if (iModel.getLocale() != null) {
            locale = iModel.getLocale();
        } else if (iModel.getHttpServletRequest() != null) {
            locale = iModel.getHttpServletRequest().getLocale();
        } else {
            locale = Locale.getDefault();
        }
                
        if (messageFormatAttributes.size() == 0) {
            return serviceManager.getService(II18nService.class).get(key, locale);
        } else {
            return MessageFormat.format(serviceManager.getService(II18nService.class).get(key, locale), messageFormatAttributes.toArray(new Object[messageFormatAttributes.size()]));
        }
    }
}
