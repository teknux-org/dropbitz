package org.teknux.dropbitz.helper;

import java.util.List;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class BeansWrapperHelper implements TemplateMethodModelEx {
	@Override
	public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
		if (arguments.size() != 1) {
			throw new TemplateModelException("Please specify class");
		}
		if (arguments.get(0).getClass() != SimpleScalar.class) {
			throw new TemplateModelException("Bad argument type");
		}
		
		String className = ((SimpleScalar) arguments.get(0)).getAsString();
		
		return BeansWrapper.getDefaultInstance().getStaticModels().get(className);
	}
}
