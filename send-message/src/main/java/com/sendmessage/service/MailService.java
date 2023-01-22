package com.sendmessage.service;

import com.sendmessage.entity.Mail;

public interface MailService {
    void sendMails(Mail[] mails);

    void sendHtmlMails(Mail[] mails);

    void sendSimpleMails(Mail[] mails);

}
