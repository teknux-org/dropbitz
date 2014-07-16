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

package org.teknux.dropbitz.service.email;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teknux.dropbitz.exception.EmailServiceException;
import org.teknux.dropbitz.model.DropbitzEmail;

public class EmailRunnable implements Runnable{
    
    private final Logger logger = LoggerFactory.getLogger(EmailRunnable.class);

    private final BlockingQueue<DropbitzEmail> emailQueue;
    private final IEmailSender emailSender;
    
    public EmailRunnable(final BlockingQueue<DropbitzEmail> emailQueue, IEmailSender emailSender) {
        this.emailQueue = Objects.requireNonNull(emailQueue, "EmailRunnable require emailQueue");
        this.emailSender = Objects.requireNonNull(emailSender, "EmailRunnable require emailSender");
    }
    
    @Override
    public void run() {
        logger.debug("Start EmailRunnable");
        try {
            while(true) {
                logger.debug("Wait new email...");
                DropbitzEmail dropbitzEmail = emailQueue.take();
                try {
                    sendEmail(dropbitzEmail);
                } catch (EmailServiceException e) {
                    logger.error("Email runnable can not send mail", e);
                }
            }
        } catch (InterruptedException e) {           
            if (emailQueue.size() == 0) {
                logger.debug("Stop EmailRunnable (No email to send, queue is empty)...");
            } else {
                List<DropbitzEmail> dropbitzEmails = new ArrayList<DropbitzEmail>();
                emailQueue.drainTo(dropbitzEmails);
                logger.debug("Send {} emails before stop EmailRunnable...", dropbitzEmails.size());
                for (DropbitzEmail dropbitzEmail : dropbitzEmails) {
                    try {
                        sendEmail(dropbitzEmail);
                    } catch (EmailServiceException e2) {
                        logger.error("Email runnable can not send mail", e2);
                    }
                }
                logger.debug("All emails are sent");
            }
            
            logger.debug("EmailRunnable stopped");
            Thread.currentThread().interrupt();
        }
    }
        
    private void sendEmail(DropbitzEmail dropbitzEmail) throws EmailServiceException {
        logger.debug("Process email...");
        emailSender.sendEmail(dropbitzEmail);
        logger.debug("Email processed");
    }
}
