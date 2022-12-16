package com.sendmessage.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.entity.R;
import com.sendmessage.entity.Mail;
import com.sendmessage.service.MailService;

@RestController
@RequestMapping("send-message/mail")
public class MailController {
    @Resource
    MailService mailService;

    @PostMapping("simple")
    public R<Object> sendSimpleMails(@RequestBody Mail[] mails) {
        mailService.sendSimpleMails(mails);

        return R.SUCCESS();
    }

    @PostMapping("")
    public R<Object> sendMails(@RequestBody Mail[] mails) {
        mailService.sendMails(mails);

        return R.SUCCESS();
    }

    @PostMapping("code")
    public R<Object> sendHtmlTestMails(@RequestBody Mail[] mails) {
        mailService.sendHtmlMails(mails);

        return R.SUCCESS();
    }
}
