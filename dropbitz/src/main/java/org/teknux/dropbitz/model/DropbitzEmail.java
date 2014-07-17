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

package org.teknux.dropbitz.model;

import java.util.List;

public class DropbitzEmail {
    private String emailFrom;
    private List<String> emailTo;
    private String subject;
    private String textMsg;
    private String htmlMsg;
    
    public String getEmailFrom() {
        return emailFrom;
    }
    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }
    public List<String> getEmailTo() {
        return emailTo;
    }
    public void setEmailTo(List<String> emailTo) {
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
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((emailFrom == null) ? 0 : emailFrom.hashCode());
        result = prime * result + ((emailTo == null) ? 0 : emailTo.hashCode());
        result = prime * result + ((htmlMsg == null) ? 0 : htmlMsg.hashCode());
        result = prime * result + ((subject == null) ? 0 : subject.hashCode());
        result = prime * result + ((textMsg == null) ? 0 : textMsg.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DropbitzEmail other = (DropbitzEmail) obj;
        if (emailFrom == null) {
            if (other.emailFrom != null)
                return false;
        } else if (!emailFrom.equals(other.emailFrom))
            return false;
        if (emailTo == null) {
            if (other.emailTo != null)
                return false;
        } else if (!emailTo.equals(other.emailTo))
            return false;
        if (htmlMsg == null) {
            if (other.htmlMsg != null)
                return false;
        } else if (!htmlMsg.equals(other.htmlMsg))
            return false;
        if (subject == null) {
            if (other.subject != null)
                return false;
        } else if (!subject.equals(other.subject))
            return false;
        if (textMsg == null) {
            if (other.textMsg != null)
                return false;
        } else if (!textMsg.equals(other.textMsg))
            return false;
        return true;
    }
}
