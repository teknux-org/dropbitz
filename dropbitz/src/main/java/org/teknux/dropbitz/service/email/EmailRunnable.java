package org.teknux.dropbitz.service.email;

import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.config.Configuration;
import org.teknux.dropbitz.model.DropbitzEmail;

public class EmailRunnable implements Runnable{
    
    private final Logger logger = LoggerFactory.getLogger(EmailRunnable.class);

    private final Configuration configuration;
    private final LinkedBlockingQueue<DropbitzEmail> emailQueue;
    
    public EmailRunnable(final Configuration configuration, final LinkedBlockingQueue<DropbitzEmail> emailQueue) {
        this.configuration = Objects.requireNonNull(configuration, "EmailCallable require configuration");
        this.emailQueue = Objects.requireNonNull(emailQueue, "EmailCallable require emailQueue");
    }
    
    @Override
    public void run() {
        logger.debug("Start EmailRunnable");
        try {
            while(true) {
                logger.debug("Wait new email...");
                DropbitzEmail dropbitzEmail = emailQueue.take();
                sendEmail(dropbitzEmail);
            }
        } catch (InterruptedException e) {           
            if (emailQueue.size() == 0) {
                logger.debug("Stop EmailRunnable (No email to send, queue is empty)...");
            } else {
                logger.debug("Send emails before stop EmailRunnable...");
                DropbitzEmail dropbitzEmail = null;
                while ((dropbitzEmail = emailQueue.poll()) != null) {
                    sendEmail(dropbitzEmail);
                }
                logger.debug("All emails are sent");
            }
            
            logger.debug("EmailRunnable stopped");
        }
    }
        
    private void sendEmail(DropbitzEmail dropbitzEmail) {
        logger.error("Process email...");
        EmailSender emailSender = new EmailSender(configuration);
        emailSender.sendEmail(dropbitzEmail);
        logger.error("Email processed");
    }
}
