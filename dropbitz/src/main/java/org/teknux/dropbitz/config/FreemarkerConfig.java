package org.teknux.dropbitz.config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import jersey.repackaged.com.google.common.collect.Lists;

import org.teknux.dropbitz.contant.I18nKey;
import org.teknux.dropbitz.contant.Route;
import org.teknux.dropbitz.freemarker.helper.I18nHelper;
import org.teknux.dropbitz.freemarker.helper.UrlHelper;
import org.teknux.dropbitz.freemarker.helper.UserHelper;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;

public class FreemarkerConfig extends Configuration {
    public FreemarkerConfig() throws TemplateModelException {
        this(null);
    }
    
	@Inject
	public FreemarkerConfig(final ServletContext servletContext) throws TemplateModelException {
		super();

		//Copy of original configuration (org.glassfish.jersey.server.mvc.freemarker.FreemarkerViewProcessor)
		//=========================
		// Create different loaders
		final List<TemplateLoader> loaders = Lists.newArrayList();
		if (servletContext != null) {
			loaders.add(new WebappTemplateLoader(servletContext));
		}
		loaders.add(new ClassTemplateLoader(FreemarkerConfig.class, "/"));
		try {
			loaders.add(new FileTemplateLoader(new File("/")));
		} catch (IOException e) {
			// NOOP
		}
		
		// Create Factory
		setTemplateLoader(new MultiTemplateLoader(loaders.toArray(new TemplateLoader[loaders.size()])));
		//=========================

		//Add shared variables
		setSharedVariable("statics", BeansWrapper.getDefaultInstance().getStaticModels());
		setSharedVariable("user", new UserHelper());
		setSharedVariable("i18n", new I18nHelper());
        setSharedVariable("i18nKey", BeansWrapper.getDefaultInstance().getStaticModels().get(I18nKey.class.getName()));
		setSharedVariable("url", new UrlHelper());
		setSharedVariable("route", BeansWrapper.getDefaultInstance().getStaticModels().get(Route.class.getName()));
	}
}
