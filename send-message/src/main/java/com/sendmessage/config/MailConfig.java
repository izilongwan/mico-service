package com.sendmessage.config;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("mail-config")
public class MailConfig implements Serializable {
    String addr;
    long expire;
    String expireKey;
    String template;
}
