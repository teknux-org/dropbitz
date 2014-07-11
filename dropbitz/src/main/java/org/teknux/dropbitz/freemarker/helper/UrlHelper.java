package org.teknux.dropbitz.freemarker.helper;

import java.util.List;

import javax.servlet.ServletContext;

import org.teknux.dropbitz.model.view.IModel;

import freemarker.template.TemplateModelException;

public class UrlHelper extends AbstractHelper {

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        if (arguments.size() > 1) {
            throw new TemplateModelException("Bad arguments");
        }
        String path = "";
        if (arguments.size() == 1) {
            path = getString(arguments.get(0));
        }        

        IModel iModel = getIModel();
        
        if (iModel.getServletContext() == null) {
            throw new TemplateModelException("Can not get ServletContext");
        }
        ServletContext servletContext = iModel.getServletContext();
        
        return servletContext.getContextPath() + path;
    }
}
