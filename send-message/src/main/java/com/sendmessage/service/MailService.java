package com.sendmessage.service;

import java.util.Map;

import com.sendmessage.entity.Mail;

public interface MailService {
    void sendMails(Mail[] mails);

    void sendHtmlMails(Mail[] mails);

    void sendSimpleMails(Mail[] mails);

}
