package com.ose.util;

import com.ose.exception.BusinessError;
import com.ose.exception.ValidationError;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 电子邮件工具。
 */
public class MailUtils {

    // JavaMailSender
    private static JavaMailSender sender = null;

    // 默认发送方信息
    private static InternetAddress from = null;

    // 内容类型
    private static final String ENCODING = "utf-8";
    private static final String CONTENT_TYPE_TEXT = "text/plain; charset=" + ENCODING;
    private static final String CONTENT_TYPE_HTML = "text/html; charset=" + ENCODING;

    /**
     * 初始化。
     *
     * @param sender        JavaMailSender 实例
     * @param senderAccount 默认发送方电子邮件地址
     * @param senderName    默认发送方姓名
     */
    public static void init(
        final JavaMailSender sender,
        final String senderAccount,
        final String senderName
    ) throws UnsupportedEncodingException {
        MailUtils.sender = sender;
        MailUtils.from = new InternetAddress(senderAccount, senderName);
    }

    // 新增静态方法设置优先级
    private static void setHighPriority(MimeMessage message) {
        try {
            message.setHeader("X-Priority", "1");
            message.setHeader("Importance", "high");
            message.setHeader("X-MSMail-Priority", "High");

            message.setHeader("Sensitivity", "Company-Confidential");
            message.setHeader("X-MS-Exchange-Organization-Sensitivity", "3");
            message.setHeader("msip_labels",
                "MSIP_Label_1a41aa1e-47bb-4b62-a008-5bb393ac3085_Enabled=true;" +
                    "MSIP_Label_1a41aa1e-47bb-4b62-a008-5bb393ac3085_Method=Privileged;" +
                    "MSIP_Label_1a41aa1e-47bb-4b62-a008-5bb393ac3085_Name=公司机密"
            );
            message.setHeader("X-MS-Exchange-Organization-RMS-Encrypted", "true");

        } catch (MessagingException e) {
            throw new RuntimeException("设置邮件优先级失败", e);
        }
    }

    /**
     * 发送电子邮件。
     *
     * @param from    接收者电子邮件地址
     * @param to      接收者电子邮件地址列表
     * @param cc      抄送者电子邮件地址列表
     * @param subject 邮件标题
     * @param content 邮件内容 HTML
     */
    public static void send(
        final InternetAddress from,
        final InternetAddress[] to,
        final InternetAddress[] cc,
        final String subject,
        final String content,
        final String contentType
    ) {
        send(from, to, cc, subject, content, contentType, null);
    }

    /**
     * 发送电子邮件。
     *
     * @param from        接收者电子邮件地址
     * @param to          接收者电子邮件地址列表
     * @param cc          抄送者电子邮件地址列表
     * @param subject     邮件标题
     * @param content     邮件内容 HTML
     * @param attachments 附件路径列表
     */
    public static void send(
        final InternetAddress from,
        final InternetAddress[] to,
        final InternetAddress[] cc,
        final String subject,
        final String content,
        final String contentType,
        final List<String> attachments
    ) {
        if (sender == null || from == null) {
            throw new BusinessError("error.mail.not-initialized");
        }

        MimeMessage message = sender.createMimeMessage();
        boolean multipart = attachments != null && attachments.size() > 0;
        boolean html = CONTENT_TYPE_HTML.equals(contentType);

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, multipart, ENCODING);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content + (html ? "<hr>" : "\r\n\r\n"), html);

            if (cc != null && cc.length > 0) {
                helper.setCc(cc);
            }

            if (multipart) {
                FileSystemResource file;
                for (String attachment : attachments) {
                    file = new FileSystemResource(attachment);
                    if (file.getFilename() != null) {
                        helper.addAttachment(file.getFilename(), file);
                    }
                }
            }
        } catch (MessagingException e) {
            throw new ValidationError("error.mail.invalid-message");
        }

        // 新增优先级设置（在所有邮件中生效）
//        setHighPriority(message);

        sender.send(message);
    }

    /**
     * 发送电子邮件。
     *
     * @param from        接收者电子邮件地址
     * @param to          接受者电子邮件地址列表
     * @param subject     邮件标题
     * @param content     邮件内容 HTML
     * @param attachments 附件路径列表
     */
    public static void send(
        final InternetAddress from,
        final InternetAddress[] to,
        final String subject,
        final String content,
        final List<String> attachments
    ) {
        send(from, to, null, subject, content, CONTENT_TYPE_HTML, attachments);
    }

    /**
     * 发送电子邮件。
     *
     * @param from    接收者电子邮件地址
     * @param to      接受者电子邮件地址列表
     * @param subject 邮件标题
     * @param content 邮件内容 HTML
     */
    public static void send(
        final InternetAddress from,
        final InternetAddress[] to,
        final String subject,
        final String content
    ) {
        send(from, to, null, subject, content, CONTENT_TYPE_HTML, null);
    }

    /**
     * 发送电子邮件。
     *
     * @param from        接收者电子邮件地址
     * @param to          接收者电子邮件地址
     * @param subject     邮件标题
     * @param content     邮件内容 HTML
     * @param attachments 附件路径列表
     */
    public static void send(
        final InternetAddress from,
        final InternetAddress to,
        final String subject,
        final String content,
        final List<String> attachments
    ) {
        send(from, new InternetAddress[]{to}, subject, content, attachments);
    }

    /**
     * 发送电子邮件。
     *
     * @param from    接收者电子邮件地址
     * @param to      接收者电子邮件地址
     * @param subject 邮件标题
     * @param content 邮件内容 HTML
     */
    public static void send(
        final InternetAddress from,
        final InternetAddress to,
        final String subject,
        final String content
    ) {
        send(from, new InternetAddress[]{to}, subject, content, null);
    }

    /**
     * 发送电子邮件。
     *
     * @param to          接收者电子邮件地址
     * @param toName      接收者姓名
     * @param subject     邮件标题
     * @param content     邮件内容 HTML
     * @param attachments 附件路径列表
     */
    public static void send(
        final String to,
        final String toName,
        final String subject,
        final String content,
        final List<String> attachments
    ) {
        try {
            send(
                from,
                StringUtils.isEmpty(toName)
                    ? new InternetAddress(to)
                    : new InternetAddress(to, toName),
                subject,
                content,
                attachments
            );
        } catch (UnsupportedEncodingException | AddressException e) {
            throw new ValidationError("error.mail.invalid-address");
        }
    }

    /**
     * 发送电子邮件。
     *
     * @param to      接收者电子邮件地址
     * @param toName  接收者姓名
     * @param subject 邮件标题
     * @param content 邮件内容 HTML
     */
    public static void send(
        final String to,
        final String toName,
        final String subject,
        final String content
    ) {
        send(to, toName, subject, content, null);
    }

    /**
     * 发送电子邮件。
     *
     * @param to      接收者电子邮件地址
     * @param subject 邮件标题
     * @param content 邮件内容 HTML
     */
    public static void send(
        final String to,
        final String subject,
        final String content,
        final List<String> attachments
    ) {
        send(to, null, subject, content, attachments);
    }

    /**
     * 发送电子邮件。
     *
     * @param to      接收者电子邮件地址
     * @param subject 邮件标题
     * @param content 邮件内容 HTML
     */
    public static void send(
        final String to,
        final String subject,
        final String content
    ) {
        send(to, null, subject, content, null);
    }

    /**
     * 发送电子邮件。
     *
     * @param from        接收者电子邮件地址
     * @param to          接收者电子邮件地址
     * @param subject     邮件标题
     * @param content     邮件内容文本
     * @param attachments 附件路径列表
     */
    public static void sendText(
        final InternetAddress from,
        final InternetAddress to,
        final String subject,
        final String content,
        final List<String> attachments
    ) {
        send(from, new InternetAddress[]{to}, null, subject, content, CONTENT_TYPE_TEXT, attachments);
    }

    /**
     * 发送电子邮件。
     *
     * @param from    接收者电子邮件地址
     * @param to      接收者电子邮件地址
     * @param subject 邮件标题
     * @param content 邮件内容文本
     */
    public static void sendText(
        final InternetAddress from,
        final InternetAddress to,
        final String subject,
        final String content
    ) {
        sendText(from, to, subject, content, null);
    }

    /**
     * 发送电子邮件。
     *
     * @param to          接收者电子邮件地址
     * @param toName      接收者姓名
     * @param subject     邮件标题
     * @param content     邮件内容文本
     * @param attachments 附件路径列表
     */
    public static void sendText(
        final String to,
        final String toName,
        final String subject,
        final String content,
        final List<String> attachments
    ) {
        try {
            sendText(from, new InternetAddress(to, toName), subject, content, attachments);
        } catch (UnsupportedEncodingException e) {
            throw new ValidationError("error.mail.invalid-address");
        }
    }

    /**
     * 发送电子邮件。
     *
     * @param to      接收者电子邮件地址
     * @param toName  接收者姓名
     * @param subject 邮件标题
     * @param content 邮件内容文本
     */
    public static void sendText(
        final String to,
        final String toName,
        final String subject,
        final String content
    ) {
        sendText(to, toName, subject, content, null);
    }

}
