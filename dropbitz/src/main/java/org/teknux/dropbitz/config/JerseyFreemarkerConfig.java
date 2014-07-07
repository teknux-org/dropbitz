package org.teknux.dropbitz.config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import jersey.repackaged.com.google.common.collect.Lists;

import org.jvnet.hk2.annotations.Optional;
import org.teknux.dropbitz.freemarker.helper.UserHelper;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;

public class JerseyFreemarkerConfig extends Configuration {
	@Inject
	public JerseyFreemarkerConfig(@Optional final ServletContext servletContext) {
		super();

		//Copy of original configuration (org.glassfish.jersey.server.mvc.freemarker.FreemarkerViewProcessor)
		//=========================
		// Create different loaders
		final List<TemplateLoader> loaders = Lists.newArrayList();
		if (servletContext != null) {
			loaders.add(new WebappTemplateLoader(servletContext));
		}
		loaders.add(new ClassTemplateLoader(JerseyFreemarkerConfig.class, "/"));
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
	}
}
