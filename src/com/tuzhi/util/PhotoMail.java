package com.tuzhi.util;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * @program: mail
 * @description: 带图片的邮件
 * @author: 兔子
 * @create: 2021-11-15 15:10
 **/

public class PhotoMail {
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
                return new PasswordAuthentication("发送人@qq.com","授权码");
            }
        });

        //开启session的debug模式;
        session.setDebug(true);

        //2.session==>获取Transport对象
        Transport ts=session.getTransport();
        //3.使用邮箱的授权码,用户名,连接邮件的服务器;
        ts.connect("smtp.qq.com","发送人@qq.com","授权码");

        //4.创建邮件; 需要传递session;
        MimeMessage message=new MimeMessage(session);
        //需要指明邮件的发送人;
        message.setFrom(new InternetAddress("发送人@qq.com"));
        //指明收件人==> 此处为自发;
        message.setRecipient(Message.RecipientType.TO,new InternetAddress("收件人@qq.com"));
        //邮件标题;
        message.setSubject("在吗在吗","utf-8");

        //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%图片的发送实现%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%====>
        //首先准备好图片文件;
        MimeBodyPart image=new MimeBodyPart();
        //数据处理图片;
        DataHandler dataHandler=new DataHandler(new FileDataSource("F:\\idea使用\\B站狂神说java\\Web复习狂神说系列\\9月4日学习邮件发送\\java普通程序实现邮件\\src\\com\\xiaozhi\\img0.jpg"));
        image.setDataHandler(dataHandler);
        //设置图片Id;
        image.setContentID("img0.jpg");

        //准备文本需要的数据;
        MimeBodyPart text=new MimeBodyPart();
        //注意要添加图片的引用;
        text.setContent("请您查看图片哦==><img src='cid:img0.jpg'>","text/html;charset=UTF-8");

        //标注发送内容之间的关系;
        MimeMultipart mmp=new MimeMultipart();
        mmp.addBodyPart(text);
        mmp.addBodyPart(image);
        mmp.setSubType("mixed");

        //5.将内容全部添加到邮件中;
        message.setContent(mmp);
        //保存修改;
        message.saveChanges();
        //6.发送邮件; 需要指明发送的地址;
        ts.sendMessage(message, message.getAllRecipients());
        //7.关闭;
        ts.close();
    }
}
