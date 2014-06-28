package org.teknux.dropbitz.helper;

import java.util.List;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class BeansWrapperHelper implements TemplateMethodModelEx {
	@Override
	public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
		if (arguments.size() != 0) {
			throw new TemplateModelException("Wrong arguments");
		}

		return BeansWrapper.getDefaultInstance().getStaticModels();
	}
}
