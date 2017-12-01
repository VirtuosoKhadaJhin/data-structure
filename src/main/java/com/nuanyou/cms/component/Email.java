package com.nuanyou.cms.component;

public class Email {

    public String from;
    public String[] to;
    public String subject;
    public String text;

    public Email() {
    }

    public Email(String from, String to, String subject, String text) {
        super();
        this.from = from;
        this.to = new String[]{to};
        this.subject = subject;
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public void setTo(String to) {
        this.to = new String[]{to};
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Email [from=" + from + ", to=" + to + ", subject=" + subject + ", text=" + text + "]";
    }

}