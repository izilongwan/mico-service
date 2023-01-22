package com.sendmessage.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.sendmessage.config.MailConfig;
import com.sendmessage.entity.Mail;
import com.sendmessage.service.MailService;
import com.sendmessage.util.RedisUtil;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailServiceImpl implements MailService {
    @Resource
    JavaMailSender javaMailSender;

    @Resource
    SpringTemplateEngine springTemplateEngine;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Resource
    MailConfig mailConfig;

    @Override
    public void sendSimpleMails(Mail[] mails) {
        List<Mail> mailList = Arrays.asList(mails);

        mailList.forEach(mail -> {
            try {
                mail.setFrom(mailConfig.getAddr());
                SimpleMailMessage message = BeanUtil.copyProperties(mail, SimpleMailMessage.class);
                message.setTo(mail.getTo());
                javaMailSender.send(message);

                RedisUtil.setMailValue(redisTemplate, mail);

            } catch (Exception e) {
                log.info("{}", e);
            }
        });
    }

    @Override
    public void sendMails(Mail[] mails) {
        List<Mail> mailList = Arrays.asList(mails);

        mailList.forEach(mail -> {
            try {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                mail.setFrom(mailConfig.getAddr());
                mimeMessage.setFrom(mail.getFrom());
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
                BeanUtil.copyProperties(mail, mimeMessageHelper);
                mimeMessageHelper.setTo(mail.getTo());
                mimeMessageHelper.setSubject(mail.getSubject());
                mimeMessageHelper.setText(mail.getText(), true);

                MultipartFile[] multipartFiles = mail.getMultipartFiles();

                if (multipartFiles != null) {
                    for (MultipartFile multipartFile : multipartFiles) {
                        mimeMessageHelper.addAttachment(
                                multipartFile.getOriginalFilename(),
                                multipartFile);
                    }
                }

                javaMailSender.send(mimeMessage);

                RedisUtil.setMailValue(redisTemplate, mail);
            } catch (Exception e) {
                log.info("{}", e);
            }
        });
    }

    @Override
    public void sendHtmlMails(Mail[] mails) {
        for (Mail mail : mails) {
            Context context = new Context();
            context.setVariable("data", mail.getMap());
            String html = springTemplateEngine.process(mail.getTemplate(), context);

            mail.setText(html);

            if (mail.getExpireKey() == null) {
                mail.setExpireKey(mailConfig.getExpireKey());
            }

            if (mail.getExpire() == 0) {
                mail.setExpire(mailConfig.getExpire());
            }

            if (mail.getTemplate() == null) {
                mail.setTemplate(mail.getTemplate());
            }
        }

        sendMails(mails);
    }

}
