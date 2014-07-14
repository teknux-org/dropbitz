package org.teknux.dropbitz.service.email;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletContext;

import org.teknux.dropbitz.config.JerseyFreemarkerConfig;
import org.teknux.dropbitz.exception.EmailServiceException;
import org.teknux.dropbitz.model.view.IModel;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

public class EmailTemplateResolver implements IEmailTemplateResolver {

    private static final String DEFAULT_VIEWS_PATH = "/webapp/views/email";
    private static final String MODEL_NAME_ATTRIBUTE = "model";
    private static final String VIEW_EXTENSION = ".ftl";

    private String viewsPath = DEFAULT_VIEWS_PATH;
    
    private ServletContext servletContext;
    private JerseyFreemarkerConfig jerseyFreemarkerConfig;
    
    public EmailTemplateResolver(ServletContext servletContext) throws EmailServiceException {
        this.servletContext = Objects.requireNonNull(servletContext, "ServletContext can not be null");
            
        //Init Freemarker
        try {
            jerseyFreemarkerConfig = new JerseyFreemarkerConfig();
        } catch (TemplateModelException e) {
            throw new EmailServiceException(e);
        }
    }
    
    public void setViewsPath(String viewsPath) {
        this.viewsPath = viewsPath;
    }
    
    /**
     * Resolve template
     * 
     * @param model
     *            Object model for Freemarker template
     * @param viewName
     *            Freemarker Template Name
     * @return String
     * @throws IOException
     *             on load error
     * @throws TemplateException
     *             on template syntax error
     */
    @Override
    public String resolve(IModel model, String viewName) throws EmailServiceException {
        Template template;
        try {
            template = jerseyFreemarkerConfig.getTemplate(Objects.requireNonNull(viewsPath, "viewsPath can not be null") + Objects.requireNonNull(viewName, "viewName can not be null") + VIEW_EXTENSION);
        } catch (IOException e) {
            throw new EmailServiceException(e);
        }
        Writer writer = new StringWriter();

        Objects.requireNonNull(model).setServletContext(servletContext);

        Map<String, IModel> map = new HashMap<String, IModel>();
        map.put(MODEL_NAME_ATTRIBUTE, model);

        try {
            template.process(map, writer);
        } catch (TemplateException | IOException e) {
            throw new EmailServiceException();
        }

        return writer.toString();
    }
}
