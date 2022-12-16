package com.sendmessage.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Mail implements Serializable {
    protected String id;// 邮件id
    protected String from;// 邮件发送人
    protected String[] to;// 邮件接收人
    protected String subject;// 邮件主题
    protected String text;// 邮件内容
    protected Date sentDate;// 发送时间
    protected String[] cc;// 抄送
    protected String[] bcc;// 密送
    protected String status;// 状态
    protected String error;// 报错信息
    protected MultipartFile[] multipartFiles;// 邮件附件

    Map<String, Object> map;

    String template;

    String expireKey;

    long expire;
}
