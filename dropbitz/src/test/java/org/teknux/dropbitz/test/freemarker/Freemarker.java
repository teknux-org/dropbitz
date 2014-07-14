package org.teknux.dropbitz.test.freemarker;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.teknux.dropbitz.config.FreemarkerConfig;
import org.teknux.dropbitz.exception.EmailServiceException;
import org.teknux.dropbitz.exception.ServiceException;
import org.teknux.dropbitz.model.view.IModel;
import org.teknux.dropbitz.model.view.Model;
import org.teknux.dropbitz.test.fake.FakeModel;
import org.teknux.dropbitz.test.fake.FakeServletContext;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Freemarker {

    private static final String MODEL_NAME_ATTRIBUTE = "model";
    private final static String VIEWS_PATH = "/views";
    private static final String VIEW_EXTENSION = ".ftl";
    
    private FreemarkerConfig jerseyFreemarkerConfig;
    
    @Before
    public void initJerseyFreemarker() throws ServiceException {
        //Init Freemarker
        try {
            jerseyFreemarkerConfig = new FreemarkerConfig(new FakeServletContext());
        } catch (TemplateModelException e) {
            throw new ServiceException(e);
        }
    }
    
    private String resolve(String viewName, IModel model) throws EmailServiceException {
        if (model == null) {
            model = new Model();
        }
        
        Template template;
        try {
            template = jerseyFreemarkerConfig.getTemplate(Objects.requireNonNull(VIEWS_PATH, "viewsPath can not be null") + Objects.requireNonNull(viewName, "viewName can not be null") + VIEW_EXTENSION);
        } catch (IOException e) {
            throw new EmailServiceException(e);
        }
        Writer writer = new StringWriter();

        model.setServletContext(null);

        Map<String, IModel> map = new HashMap<String, IModel>();
        map.put(MODEL_NAME_ATTRIBUTE, model);

        try {
            template.process(map, writer);
        } catch (TemplateException | IOException e) {
            throw new EmailServiceException();
        }

        return writer.toString();
    }
        
    @Test
    public void test01Simple() throws ServiceException {
        Assert.assertEquals("simple", resolve("/simple", null));
    }
    
    @Test
    public void test02Model() throws ServiceException {
        Assert.assertEquals("testModel", resolve("/model", new FakeModel("testModel")));
    }
}
