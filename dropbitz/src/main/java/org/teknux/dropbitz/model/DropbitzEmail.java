package org.teknux.dropbitz.model;

public class DropbitzEmail {
    private String emailFrom;
    private String emailTo[];
    private String subject;
    private String textMsg;
    private String htmlMsg;
    
    public String getEmailFrom() {
        return emailFrom;
    }
    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }
    public String[] getEmailTo() {
        return emailTo;
    }
    public void setEmailTo(String[] emailTo) {
        this.emailTo = emailTo;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getTextMsg() {
        return textMsg;
    }
    public void setTextMsg(String textMsg) {
        this.textMsg = textMsg;
    }
    public String getHtmlMsg() {
        return htmlMsg;
    }
    public void setHtmlMsg(String htmlMsg) {
        this.htmlMsg = htmlMsg;
    }
}
