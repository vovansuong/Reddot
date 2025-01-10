package com.springboot.app.emails.dto.request;

import java.util.Set;

public class DataEmailRequest {
    private Set<String> emails;
    private String subject;
    private String template;

    public DataEmailRequest() {
    }

    public DataEmailRequest(Set<String> emails, String subject, String template) {
        this.emails = emails;
        this.subject = subject;
        this.template = template;
    }

    public Set<String> getEmails() {
        return emails;
    }

    public void setEmails(Set<String> email) {
        this.emails = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

}
