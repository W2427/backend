package com.ose.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Properties;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.Address;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeUtility;

import com.ose.dto.MailSenderInfo;

/**
 * 简单邮件（带附件的邮件）发送器
 */
public class SimpleMailSenderUtils {

    /**
     * 以文本格式发送邮件
     *
     * @param mailInfo 待发送的邮件的信息
     */
    public static boolean sendTextMail(MailSenderInfo mailInfo) {
        // 判断是否需要身份认证
        MailAuthenticator authenticator = null;

        Properties props = mailInfo.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", mailInfo.getMailServerPort());
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        if (mailInfo.isValidate()) {
            // 如果需要身份认证，则创建一个密码验证器
            authenticator = new MailAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(props, authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailInfo.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            String toAddress = mailInfo.getToAddress();
            toAddress = toAddress.replaceAll(",", ";");
            String[] toAddresses = toAddress.split(";");

            Address[] toadd = new Address[toAddresses.length];
            for (int i = 0; i < toAddresses.length; i++) {
                toadd[i] = new InternetAddress(toAddresses[i]);
            }
            // Message.RecipientType.TO属性表示接收者的类型为TO
            mailMessage.setRecipients(Message.RecipientType.TO, toadd);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());

            String ccAddress = mailInfo.getCcAddress();
            if (ccAddress != null && !"".equals(ccAddress)) {
                ccAddress = ccAddress.replaceAll(",", ";");
                String[] ccAddresses = ccAddress.split(";");

                Address[] ccAdd = new Address[ccAddresses.length];
                for (int i = 0; i < ccAddresses.length; i++) {
                    ccAdd[i] = new InternetAddress(ccAddresses[i]);
                }
                // Message.RecipientType.TO属性表示接收者的类型为TO
                mailMessage.setRecipients(Message.RecipientType.CC, ccAdd);
            }

            // 放入内容和附件类
            MimeMultipart allMultipart = new MimeMultipart("mixed");
            // 设置内容
            MimeBodyPart HtmlBodypart = new MimeBodyPart();
            // 设置邮件消息的主要内容
            String mailContent = mailInfo.getContent();
            HtmlBodypart.setContent(mailContent, "text/html;charset=utf-8");

            // 设置附件
            MimeBodyPart attachFileBodypart = new MimeBodyPart();
            MimeMultipart attachFileMMP = new MimeMultipart("related");
            String[] strArrgs = mailInfo.getAttachFileNames(); // 所有文件路径
            if (strArrgs != null) {
                for (int i = 0; i < strArrgs.length; i++) {
                    MimeBodyPart attachFileBody = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(new File(strArrgs[i]));// 附件文件
                    attachFileBody.setDataHandler(new DataHandler(fds));// 得到附件本身并至入BodyPart
                    // MimeUtility.encodeText()解决文件名乱码问题
                    attachFileBody.setFileName(MimeUtility.encodeText(fds.getName()));// 得到文件名同样至入BodyPart
                    attachFileMMP.addBodyPart(attachFileBody);// 放入BodyPart
                }
                attachFileBodypart.setContent(attachFileMMP, "text/html;charset=utf-8");
                // 放入内容
                allMultipart.addBodyPart(HtmlBodypart);
                // 放入附件
                allMultipart.addBodyPart(attachFileBodypart);
                mailMessage.setContent(allMultipart);
            } else {
                mailMessage.setContent(mailContent, "text/html;charset=utf-8");
            }
            mailMessage.saveChanges();

            // 文件内容写在本地，测试 用
            FileOutputStream os = new FileOutputStream(mailInfo.getTempEmlPath());
            mailMessage.writeTo(os);
            os.close();
            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
        return false;
    }

    /**
     * 以HTML格式发送邮件
     *
     * @param mailInfo 待发送的邮件信息
     */
    public static boolean sendHtmlMail(MailSenderInfo mailInfo) {
        // 判断是否需要身份认证
        MailAuthenticator authenticator = null;

        Properties props = mailInfo.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", mailInfo.getMailServerPort());
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        // 如果需要身份认证，则创建一个密码验证器
        if (mailInfo.isValidate()) {
            authenticator = new MailAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(props, authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailInfo.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            String toAddress = mailInfo.getToAddress();
            toAddress = toAddress.replaceAll(",", ";");
            String[] toAddresses = toAddress.split(";");

            Address[] toadd = new Address[toAddresses.length];
            for (int i = 0; i < toAddresses.length; i++) {
                toadd[i] = new InternetAddress(toAddresses[i]);
            }
            // Message.RecipientType.TO属性表示接收者的类型为TO
            mailMessage.setRecipients(Message.RecipientType.TO, toadd);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());

            String ccAddress = mailInfo.getCcAddress();
            if (ccAddress != null && !"".equals(ccAddress)) {
                ccAddress = ccAddress.replaceAll(",", ";");
                String[] ccAddresses = toAddress.split(";");

                Address[] ccAdd = new Address[ccAddresses.length];
                for (int i = 0; i < ccAddresses.length; i++) {
                    ccAdd[i] = new InternetAddress(ccAddresses[i]);
                }
                // Message.RecipientType.TO属性表示接收者的类型为TO
                mailMessage.setRecipients(Message.RecipientType.CC, ccAdd);
            }
            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();
            // 创建一个包含HTML内容的MimeBodyPart
            BodyPart html = new MimeBodyPart();
            // 设置HTML内容
            html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
            mainPart.addBodyPart(html);
            // 将MiniMultipart对象设置为邮件内容
            mailMessage.setContent(mainPart);
            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace(System.out);
        }
        return false;
    }

    public static void main(String[] args) {
        // 这个类主要是设置邮件
        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost("smtp.yeah.net");
        mailInfo.setMailServerPort("465");
        mailInfo.setValidate(true);
        mailInfo.setUserName("chinaportservice@yeah.net");
        mailInfo.setPassword("Ose2018");// 您的邮箱密码
        mailInfo.setFromAddress("chinaportservice@yeah.net");
        mailInfo.setToAddress("jinhy@livebridge.com.cn");
        mailInfo.setCcAddress("karlkim@163.com");
        mailInfo.setSubject("确认问题邮件检查确认");
        mailInfo.setContent("问题邮件检查");
        mailInfo.setAttachFileNames(new String[]{"/var/www/ose/private/files/a.txt", "/var/www/ose/private/files/b.txt"});
        String tempEml = "/var/www/ose/private/files/ssss.eml";
        mailInfo.setTempEmlPath(tempEml);
        SimpleMailSenderUtils.sendTextMail(mailInfo);
        FileUtils.remove(tempEml);
        System.out.println("send OK");

    }
}
