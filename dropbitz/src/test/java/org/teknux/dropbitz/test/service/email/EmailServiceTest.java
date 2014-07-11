package org.teknux.dropbitz.test.service.email;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.teknux.dropbitz.exception.ServiceException;
import org.teknux.dropbitz.model.DropbitzEmail;
import org.teknux.dropbitz.model.view.Model;
import org.teknux.dropbitz.service.email.EmailService;
import org.teknux.dropbitz.service.email.IEmailSender;

public class EmailServiceTest {

    private Set<DropbitzEmail> dropbitzEmails;

    private final static String VIEWS_PATH = "/emailviews";
    
    private IEmailSender emailSender = new IEmailSender() {

        @Override
        public void sendEmail(DropbitzEmail dropbitzEmail) {
            dropbitzEmails.add(dropbitzEmail);
        }
    };
    
    private EmailService getEmailService(boolean enable, String from, String to[]) throws ServiceException {
        FakeEmailServiceManager serviceManager = new FakeEmailServiceManager(enable, from, to);

        EmailService emailService = new EmailService();
        emailService.setViewsPath(VIEWS_PATH);
        emailService.setEmailSender(emailSender);
        emailService.start(serviceManager);
               
        return emailService;
    }

    @Test
    public void testDisabled() throws ServiceException {
        dropbitzEmails = new HashSet<DropbitzEmail>();
        EmailService emailService = getEmailService(false, null, null);

        emailService.sendEmail(null,null,null);
        emailService.stop();
        
        Assert.assertTrue(dropbitzEmails.isEmpty());
    }
    
    @Test
    public void testSimple() throws ServiceException {
        dropbitzEmails = new HashSet<DropbitzEmail>();
        EmailService emailService = getEmailService(true, "from@localhost.lan", new String[] { "to@localhost.lan" });

        emailService.sendEmail("subject", "/simple", new Model());
        emailService.stop();
        
        Set<DropbitzEmail> expecteds = new HashSet<DropbitzEmail>();
        DropbitzEmail expected = new DropbitzEmail();
        expected.setSubject("subject");
        expected.setEmailFrom("from@localhost.lan");
        expected.setEmailTo(new String[] { "to@localhost.lan" });
        expected.setHtmlMsg("simple");
        expecteds.add(expected);
        
        Assert.assertEquals(dropbitzEmails, expecteds);
    }    
}
