/*
 * Copyright (C) 2014 TekNux.org
 *
 * This file is part of the dropbitz Community GPL Source Code.
 *
 * dropbitz Community Source Code is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * dropbitz Community Source Code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with dropbitz Community Source Code.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.teknux.dropbitz.freemarker.helper;

import java.text.MessageFormat;

import org.teknux.dropbitz.model.view.IModel;

import freemarker.core.Environment;
import freemarker.ext.beans.BeanModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

abstract public class AbstractHelper implements TemplateMethodModelEx {
    private final static String MODEL_VARIABLE = "model";
        
    @SuppressWarnings("unchecked")
    private <T> T getModel(Class<T> clazz) throws TemplateModelException {
        Environment environment = Environment.getCurrentEnvironment();
        if (environment == null) {
            throw new TemplateModelException("Can not get environment");
        }
        TemplateModel templateModel = environment.getVariable(MODEL_VARIABLE);
        if (templateModel == null) {
            throw new TemplateModelException("Can not get model");
        }
        if (!(templateModel instanceof BeanModel)) {
            throw new TemplateModelException("TemplateModel is not a instance of BeanModel");
        }
        BeanModel beanModel = (BeanModel) templateModel;
        Object wrappedObject = beanModel.getWrappedObject();
        if (wrappedObject == null) {
            throw new TemplateModelException("Model is null");
        }
        
        if (! clazz.isInstance(wrappedObject)) {
            throw new TemplateModelException(MessageFormat.format("Model is not an instance of {0}", clazz.getSimpleName()));
        }
        return (T) beanModel.getWrappedObject();
    }
    
    protected IModel getIModel() throws TemplateModelException {
        return getModel(IModel.class);
    }
    
    protected String getString(Object object) throws TemplateModelException {
        if (object instanceof TemplateScalarModel) {
            return ((TemplateScalarModel) object).getAsString();
        } else {
            throw new TemplateModelException("Can not parse argument as string");
        }
    }
}
