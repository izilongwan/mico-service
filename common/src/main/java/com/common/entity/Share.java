package com.common.entity;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties("share")
@Data
public class Share implements Serializable {
    String timestamp;
}
