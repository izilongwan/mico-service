package com.sendmessage.dto;

import java.io.Serializable;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.sendmessage.entity.Mail;

import lombok.Data;

@Data
@Component
public class MailDto implements Serializable {
    Mail[] mails;

    Map<String, Object> map;

    String template;

    String expireKey;

    long expire;
}
