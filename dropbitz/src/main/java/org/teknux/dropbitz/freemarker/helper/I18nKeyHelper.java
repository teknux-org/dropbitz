package org.teknux.dropbitz.freemarker.helper;

import java.util.List;

import org.teknux.dropbitz.contant.I18nKey;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class I18nKeyHelper implements TemplateModel {
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
        if (arguments.size() != 0) {
            throw new TemplateModelException("Bad arguments");
        }

        return BeansWrapper.getDefaultInstance().getStaticModels().get(I18nKey.class.getName());
    }
}