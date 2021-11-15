package com.tuzhi.util;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * @program: mail
 * @description: 简单文本邮件
 * @author: 兔子
 * @create: 2021-11-15 14:49
 **/

public class SimpleTextMail {
    public static void main(String[] args) throws GeneralSecurityException, MessagingException {

        Properties properties=new Properties();
        //设置qq邮件服务器;
        properties.setProperty("mail.host","smtp.qq.com");
        //邮件发送协议;
        properties.setProperty("mail.transport.protocol","smtp");
        //需要验证用户名与密码;
        properties.setProperty("mail.smtp.auth","true");

        //QQ邮箱的SSL加密;
        MailSSLSocketFactory sf=new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.ssl.socketFactory",sf);

        //使用JavaMail发送邮件;
        //1.首先创建整个应用程序的全局session对象;(启动==>session销毁结束)
        //QQ的独特创建方式;===>
        Session session=Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //userName:用户名 password:授权码
//                return new PasswordAuthentication("发送人@qq.com","授权码");
                return new PasswordAuthentication("542918096@qq.com","iawzcatsxreubfeh");
            }
        });

        //开启session的debug模式;
        session.setDebug(true);

        //2.session==>获取Transport对象
        Transport ts=session.getTransport();
        //3.使用邮箱的授权码,用户名,连接邮件的服务器;
//        ts.connect("smtp.qq.com","发送人@qq.com","iawzcatsxreubfeh");
        ts.connect("smtp.qq.com","542918096@qq.com","iawzcatsxreubfeh");

        //4.创建邮件; 需要传递session;
        MimeMessage message=new MimeMessage(session);
        //需要指明邮件的发送人;
//        message.setFrom(new InternetAddress("发送人@qq.com"));
        message.setFrom(new InternetAddress("542918096@qq.com"));
        //指明收件人==> 此处为自发;
//        message.setRecipient(Message.RecipientType.TO,new InternetAddress("接收人@qq.com"));
        message.setRecipient(Message.RecipientType.TO,new InternetAddress("1791155304@qq.com"));
        //邮件标题;
        message.setSubject("在吗在吗","utf-8");
        //文本内容;
        message.setContent("<h3 style='color: #FF1493'>好耶,收到消息的话请回复.</h3>","text/html;charset=UTF-8");

        //5.发送邮件; 需要指明发送的地址;
        ts.sendMessage(message, message.getAllRecipients());
        //6.关闭;
        ts.close();
    }
}
