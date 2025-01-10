package com.springboot.app.emails.dto.request;

public class RegistationEmailRequest {
    private String subject;
    private String template;

    public RegistationEmailRequest() {
    }

    public RegistationEmailRequest(String subject, String template) {
        this.subject = subject;
        this.template = template;
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
