package org.teknux.dropbitz.service.email;

import org.teknux.dropbitz.exception.EmailServiceException;
import org.teknux.dropbitz.model.DropbitzEmail;

public interface IEmailSender {
    void sendEmail(DropbitzEmail dropbitzEmail) throws EmailServiceException;
}
