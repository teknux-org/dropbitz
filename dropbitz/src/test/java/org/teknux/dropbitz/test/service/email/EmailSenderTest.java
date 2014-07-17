package org.teknux.dropbitz.test.service.email;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.runners.MockitoJUnitRunner;
import org.teknux.dropbitz.config.Configuration;
import org.teknux.dropbitz.exception.EmailServiceException;
import org.teknux.dropbitz.exception.ServiceException;
import org.teknux.dropbitz.model.DropbitzEmail;
import org.teknux.dropbitz.service.email.EmailSender;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class EmailSenderTest {

    private Configuration configuration;
    private HtmlEmail htmlEmail;
    private EmailSender emailSender;
    private DropbitzEmail dropbitzEmail;
    
    private void send() throws EmailServiceException, EmailException {
        emailSender.sendEmail(dropbitzEmail, htmlEmail);
        verify(htmlEmail).setHostName(configuration.getEmailHost());
        verify(htmlEmail).setSmtpPort(configuration.getEmailPort());
        verify(htmlEmail).setSSLOnConnect(configuration.isEmailSsl());
        verify(htmlEmail).setFrom(dropbitzEmail.getEmailFrom());
        verify(htmlEmail).addTo(dropbitzEmail.getEmailTo().toArray(new String[dropbitzEmail.getEmailTo().size()]));
        if ((configuration.getEmailUsername() != null && !configuration.getEmailUsername().isEmpty()) || (configuration.getEmailPassword() != null && !configuration.getEmailPassword().isEmpty())) {
            verify(htmlEmail).setAuthentication(configuration.getEmailUsername(), configuration.getEmailPassword());
        } else {
            verify(htmlEmail, never()).setAuthentication(null,null);
        }
    }
    
    @Before
    public void init() {
        configuration = mock(Configuration.class);       
        emailSender = new EmailSender(configuration);
        
        htmlEmail = mock(HtmlEmail.class);
        
        dropbitzEmail = new DropbitzEmail();
        dropbitzEmail.setEmailFrom("bigup@localhost.lan");
        dropbitzEmail.setEmailTo(Arrays.asList("oeil@localhost.lan"));
        dropbitzEmail.setHtmlMsg("Hello world");
        
        when(configuration.getEmailHost()).thenReturn("smtp.localhost.lan");
        when(configuration.getEmailPort()).thenReturn(10);
        when(configuration.isEmailSsl()).thenReturn(false);
        when(configuration.getEmailUsername()).thenReturn("bigup");
        when(configuration.getEmailPassword()).thenReturn("password");   
    }
    
    @Test(expected=NullPointerException.class)
    public void test01BadHost() throws ServiceException, EmailException {
        when(configuration.getEmailHost()).thenReturn(null);
        
        send();
    }
    
    @Test(expected=NullPointerException.class)
    public void test02BadFrom() throws ServiceException, EmailException {
        dropbitzEmail.setEmailFrom(null);
        
        send();
    }
    
    @Test(expected=EmailServiceException.class)
    public void test03BadTo() throws ServiceException, EmailException {
        dropbitzEmail.setEmailTo(null);
        
        send();
    }
    
    @Test(expected=EmailServiceException.class)
    public void test04BadTo2() throws ServiceException, EmailException {
        dropbitzEmail.setEmailTo(new ArrayList<String>());
        
        send();
    }
    
    @Test(expected=NullPointerException.class)
    public void test05BadToHtmlMsg() throws ServiceException, EmailException {
        dropbitzEmail.setHtmlMsg(null);
        
        send();
    }
    
    @Test
    public void test06Ok() throws ServiceException, EmailException {
        send();
    }
    
    @Test
    public void test07Ssl() throws ServiceException, EmailException {
        when(configuration.isEmailSsl()).thenReturn(true);  
        
        send();
    }
    
    @Test
    public void test08NoAuth() throws ServiceException, EmailException { 
        when(configuration.getEmailUsername()).thenReturn(null);
        when(configuration.getEmailPassword()).thenReturn(null);  
        
        send();
    }
}
