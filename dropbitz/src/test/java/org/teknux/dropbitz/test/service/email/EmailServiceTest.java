package org.teknux.dropbitz.test.service.email;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.exception.EmailServiceException;
import org.teknux.dropbitz.exception.ServiceException;
import org.teknux.dropbitz.model.DropbitzEmail;
import org.teknux.dropbitz.model.view.Model;
import org.teknux.dropbitz.service.email.EmailService;
import org.teknux.dropbitz.service.email.IEmailSender;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmailServiceTest {

    private final Logger logger = LoggerFactory.getLogger(EmailServiceTest.class);
    
    private Set<DropbitzEmail> dropbitzEmails;

    private final static String VIEWS_PATH = "/views";
    
    private final Object lockMailSender = new Object();
    private boolean lockMailSenderValue = false;

    private IEmailSender emailSender = new IEmailSender() {

        @Override
        public void sendEmail(DropbitzEmail dropbitzEmail) throws EmailServiceException {
            dropbitzEmails.add(dropbitzEmail);
            waitIfMailSenderLocked();
        }
    };
    
    private void waitIfMailSenderLocked() throws EmailServiceException {
        synchronized (lockMailSender) {
            if (lockMailSenderValue) {
                logger.info("[TEST] Wait Mail sender lock...");
                try {
                    lockMailSender.wait();
                } catch (InterruptedException e) {
                    throw new EmailServiceException("[TEST] Can not wait mail sender lock ", e);
                }
                logger.info("[TEST] Mail sender lock released");                
            }
        }
    }
    
    private void lockMailSender() {
        logger.info("[TEST] Lock mail sender...");
        synchronized (lockMailSender) {
            lockMailSenderValue = true;
        }
        logger.info("[TEST] Mail sender locked !");
    }
    
    private void unlockMailSender() {
        logger.info("[TEST] Unlock mail sender...");
        synchronized (lockMailSender) {
            lockMailSender.notify();
            lockMailSenderValue = false;
        }
        logger.info("[TEST] Mail sender unlocked !");
    }

    private EmailService getEmailService(boolean enable, String from, String to[]) throws ServiceException {
        FakeEmailServiceManager serviceManager = new FakeEmailServiceManager(enable, from, to);

        EmailService emailService = new EmailService();
        emailService.setViewsPath(VIEWS_PATH);
        emailService.setEmailSender(emailSender);
        emailService.start(serviceManager);

        return emailService;
    }

    @Test
    public void test01Disabled() throws ServiceException {
        dropbitzEmails = new HashSet<DropbitzEmail>();
        EmailService emailService = getEmailService(false, null, null);

        emailService.sendEmail(null, null, null);
        emailService.stop();

        Assert.assertTrue(dropbitzEmails.isEmpty());
    }

    @Test
    public void test02Simple() throws ServiceException {
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

    @Test
    public void test03Model() throws ServiceException {
        dropbitzEmails = new HashSet<DropbitzEmail>();
        EmailService emailService = getEmailService(true, "from@localhost.lan", new String[] { "to@localhost.lan" });

        emailService.sendEmail("subject", "/model", new FakeModel("testModel"));
        emailService.stop();

        Set<DropbitzEmail> expecteds = new HashSet<DropbitzEmail>();
        DropbitzEmail expected = new DropbitzEmail();
        expected.setSubject("subject");
        expected.setEmailFrom("from@localhost.lan");
        expected.setEmailTo(new String[] { "to@localhost.lan" });
        expected.setHtmlMsg("testModel");
        expecteds.add(expected);

        Assert.assertEquals(dropbitzEmails, expecteds);
    }

    @Test
    public void test04Alt() throws ServiceException {
        dropbitzEmails = new HashSet<DropbitzEmail>();
        EmailService emailService = getEmailService(true, "from@localhost.lan", new String[] { "to@localhost.lan" });

        emailService.sendEmail("subject", "/simple", new FakeModel("testModel"), "/model");
        emailService.stop();

        Set<DropbitzEmail> expecteds = new HashSet<DropbitzEmail>();
        DropbitzEmail expected = new DropbitzEmail();
        expected.setSubject("subject");
        expected.setEmailFrom("from@localhost.lan");
        expected.setEmailTo(new String[] { "to@localhost.lan" });
        expected.setHtmlMsg("simple");
        expected.setTextMsg("testModel");
        expecteds.add(expected);

        Assert.assertEquals(dropbitzEmails, expecteds);
    }
    
    @Test
    public void test05Multiple() throws ServiceException {
        dropbitzEmails = new HashSet<DropbitzEmail>();
        EmailService emailService = getEmailService(true, "from@localhost.lan", new String[] { "to@localhost.lan" });

        emailService.sendEmail("subject", "/simple");
        emailService.sendEmail("subject2", "/model", new FakeModel("testModel"));
        emailService.stop();

        Set<DropbitzEmail> expecteds = new HashSet<DropbitzEmail>();
        DropbitzEmail expected = new DropbitzEmail();
        expected.setSubject("subject");
        expected.setEmailFrom("from@localhost.lan");
        expected.setEmailTo(new String[] { "to@localhost.lan" });
        expected.setHtmlMsg("simple");
        expecteds.add(expected);
        expected = new DropbitzEmail();
        expected.setSubject("subject2");
        expected.setEmailFrom("from@localhost.lan");
        expected.setEmailTo(new String[] { "to@localhost.lan" });
        expected.setHtmlMsg("testModel");
        expecteds.add(expected);

        Assert.assertEquals(dropbitzEmails, expecteds);
    }
    
    @Test
    public void test06OneAtTime() throws ServiceException, InterruptedException {
        dropbitzEmails = new HashSet<DropbitzEmail>();
        EmailService emailService = getEmailService(true, "from@localhost.lan", new String[] { "to@localhost.lan" });

        lockMailSender();
        
        emailService.sendEmail("subject", "/simple");
        emailService.sendEmail("subject2", "/model", new FakeModel("testModel"));
        
        Thread.sleep(300);//Wait to be sure that second email will not be processed before release lock
        
        Set<DropbitzEmail> expecteds = new HashSet<DropbitzEmail>();
        DropbitzEmail expected = new DropbitzEmail();
        expected.setSubject("subject");
        expected.setEmailFrom("from@localhost.lan");
        expected.setEmailTo(new String[] { "to@localhost.lan" });
        expected.setHtmlMsg("simple");
        expecteds.add(expected);
        Assert.assertEquals(dropbitzEmails, expecteds);
        
        unlockMailSender();

        emailService.stop();
        
        expected = new DropbitzEmail();
        expected.setSubject("subject2");
        expected.setEmailFrom("from@localhost.lan");
        expected.setEmailTo(new String[] { "to@localhost.lan" });
        expected.setHtmlMsg("testModel");
        expecteds.add(expected);

        Assert.assertEquals(dropbitzEmails, expecteds);
    }
}
