package org.teknux.dropbitz.exception;

public class EmailServiceException extends ServiceException {
    private static final long serialVersionUID = 1L;
    
    public EmailServiceException() {
        super();
    }

    public EmailServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailServiceException(String message) {
        super(message);
    }

    public EmailServiceException(Throwable cause) {
        super(cause);
    }
}
