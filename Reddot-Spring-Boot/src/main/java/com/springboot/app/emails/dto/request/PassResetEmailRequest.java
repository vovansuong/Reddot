package com.springboot.app.emails.dto.request;

public class PassResetEmailRequest {
    private String subject;
    private String template;

    public PassResetEmailRequest() {
        super();
    }

    public PassResetEmailRequest(String subject, String template) {
        super();
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
