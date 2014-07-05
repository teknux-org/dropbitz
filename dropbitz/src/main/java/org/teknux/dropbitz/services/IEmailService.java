package org.teknux.dropbitz.services;

public interface IEmailService {
    void sendEmail(String subject, String viewName);

    void sendEmail(String subject, String viewName, Object model);

    void sendEmail(String subject, String viewName, Object model, String viewNameAlt);
}
