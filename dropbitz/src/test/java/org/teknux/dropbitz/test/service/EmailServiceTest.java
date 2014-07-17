/*
 * Copyright (C) 2014 TekNux.org
 *
 * This file is part of the dropbitz Community GPL Source Code.
 *
 * dropbitz Community Source Code is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * dropbitz Community Source Code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with dropbitz Community Source Code.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.teknux.dropbitz.test.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.runners.MockitoJUnitRunner;
import org.teknux.dropbitz.config.Configuration;
import org.teknux.dropbitz.exception.EmailServiceException;
import org.teknux.dropbitz.exception.ServiceException;
import org.teknux.dropbitz.model.DropbitzEmail;
import org.teknux.dropbitz.model.view.IModel;
import org.teknux.dropbitz.model.view.Model;
import org.teknux.dropbitz.service.email.EmailService;
import org.teknux.dropbitz.service.email.EmailTemplateResolver;
import org.teknux.dropbitz.service.email.IEmailSender;
import org.teknux.dropbitz.test.freemarker.FakeModel;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {
    
    private Set<DropbitzEmail> dropbitzEmails;

    private final static String VIEWS_PATH = "/views";
    
    private final Object lockMailSender = new Object();
    private boolean lockMailSenderValue = false;
    
    private void waitIfMailSenderLocked() throws EmailServiceException {
        synchronized (lockMailSender) {
            if (lockMailSenderValue) {
                try {
                    lockMailSender.wait();
                } catch (InterruptedException e) {
                    throw new EmailServiceException("[TEST] Can not wait mail sender lock ", e);
                }               
            }
        }
    }
    
    private void lockMailSender() {
        synchronized (lockMailSender) {
            lockMailSenderValue = true;
        }
    }
    
    private void unlockMailSender() {
        synchronized (lockMailSender) {
            lockMailSender.notify();
            lockMailSenderValue = false;
        }
    }
    
    private EmailService getEmailService(String viewPath, boolean enable, String from, String to[]) throws ServiceException {        
        ServletContext servletContext= mock(ServletContext.class);
       
        EmailTemplateResolver emailTemplateResolver = new EmailTemplateResolver(servletContext);
        emailTemplateResolver.setViewsPath(viewPath);
        IEmailSender emailSender = new IEmailSender() {

            @Override
            public void sendEmail(DropbitzEmail dropbitzEmail) throws EmailServiceException {
                dropbitzEmails.add(dropbitzEmail);
                waitIfMailSenderLocked();
            }
        };
        
        Configuration configuration = mock(Configuration.class);
        when(configuration.isEmailEnable()).thenReturn(enable);
        when(configuration.getEmailFrom()).thenReturn(from);
        when(configuration.getEmailTo()).thenReturn(to);

        EmailService emailService = new EmailService();
        emailService.start(configuration, emailTemplateResolver, emailSender);

        return emailService;
    }

    @Test
    public void test01Disabled() throws ServiceException {
        dropbitzEmails = new HashSet<DropbitzEmail>();
        EmailService emailService = getEmailService(VIEWS_PATH, false, null, null);

        emailService.sendEmail(null, null);
        emailService.stop();

        Assert.assertTrue(dropbitzEmails.isEmpty());
    }
    
    @Test
    public void test02BadSend() throws ServiceException {
        dropbitzEmails = new HashSet<DropbitzEmail>();
        EmailService emailService = getEmailService(VIEWS_PATH, true, null, null);

        try {
            emailService.sendEmail("subject", null, new Model());
            Assert.fail("Should throw NPE");
        } catch (NullPointerException e){
        }
        try {
            emailService.sendEmail("subject", "/simple", (IModel)null);
            Assert.fail("Should throw NPE");
        } catch (NullPointerException e){
        }
        emailService = getEmailService(null, true, null, null);
        try {
            emailService.sendEmail("subject", "/simple", new Model());
            Assert.fail("Should throw NPE");
        } catch (NullPointerException e){
        }
        emailService.stop();

        Assert.assertTrue(dropbitzEmails.isEmpty());
    }

    @Test
    public void test03Simple() throws ServiceException {
        dropbitzEmails = new HashSet<DropbitzEmail>();
        EmailService emailService = getEmailService(VIEWS_PATH, true, "from@localhost.lan", new String[] { "to@localhost.lan" });

        emailService.sendEmail("subject", "/simple", new Model());
        emailService.stop();

        Set<DropbitzEmail> expecteds = new HashSet<DropbitzEmail>();
        DropbitzEmail expected = new DropbitzEmail();
        expected.setSubject("subject");
        expected.setEmailFrom("from@localhost.lan");
        expected.setEmailTo(new String[] { "to@localhost.lan" });
        expected.setHtmlMsg("simple");
        expecteds.add(expected);

        Assert.assertEquals(expecteds, dropbitzEmails);
    }

    @Test
    public void test04Model() throws ServiceException {
        dropbitzEmails = new HashSet<DropbitzEmail>();
        EmailService emailService = getEmailService(VIEWS_PATH, true, "from@localhost.lan", new String[] { "to@localhost.lan" });

        emailService.sendEmail("subject", "/model", new FakeModel("testModel"));
        emailService.stop();

        Set<DropbitzEmail> expecteds = new HashSet<DropbitzEmail>();
        DropbitzEmail expected = new DropbitzEmail();
        expected.setSubject("subject");
        expected.setEmailFrom("from@localhost.lan");
        expected.setEmailTo(new String[] { "to@localhost.lan" });
        expected.setHtmlMsg("testModel");
        expecteds.add(expected);

        Assert.assertEquals(expecteds, dropbitzEmails);
    }

    @Test
    public void test05Alt() throws ServiceException {
        dropbitzEmails = new HashSet<DropbitzEmail>();
        EmailService emailService = getEmailService(VIEWS_PATH, true, "from@localhost.lan", new String[] { "to@localhost.lan" });

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

        Assert.assertEquals(expecteds, dropbitzEmails);
    }
    
    @Test
    public void test06Multiple() throws ServiceException {
        dropbitzEmails = new HashSet<DropbitzEmail>();
        EmailService emailService = getEmailService(VIEWS_PATH, true, "from@localhost.lan", new String[] { "to@localhost.lan" });

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

        Assert.assertEquals(expecteds, dropbitzEmails);
    }
    
    @Test
    public void test07OneAtTime() throws ServiceException, InterruptedException {
        dropbitzEmails = new HashSet<DropbitzEmail>();
        EmailService emailService = getEmailService(VIEWS_PATH, true, "from@localhost.lan", new String[] { "to@localhost.lan" });

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
        Assert.assertEquals(expecteds, dropbitzEmails);
        
        unlockMailSender();

        emailService.stop();
        
        expected = new DropbitzEmail();
        expected.setSubject("subject2");
        expected.setEmailFrom("from@localhost.lan");
        expected.setEmailTo(new String[] { "to@localhost.lan" });
        expected.setHtmlMsg("testModel");
        expecteds.add(expected);

        Assert.assertEquals(expecteds, dropbitzEmails);
    }
    
    @Test
    public void test08EmailTo() throws ServiceException {
        dropbitzEmails = new HashSet<DropbitzEmail>();
        EmailService emailService = getEmailService(VIEWS_PATH, true, "from@localhost.lan", new String[] { "to@localhost.lan" });

        emailService.sendEmail("subject", "/simple", new Model(), new String[]{"customto@localhost.lan"});
        emailService.stop();

        Set<DropbitzEmail> expecteds = new HashSet<DropbitzEmail>();
        DropbitzEmail expected = new DropbitzEmail();
        expected.setSubject("subject");
        expected.setEmailFrom("from@localhost.lan");
        expected.setEmailTo(new String[] { "customto@localhost.lan" });
        expected.setHtmlMsg("simple");
        expecteds.add(expected);

        Assert.assertEquals(expecteds, dropbitzEmails);
    }
}
