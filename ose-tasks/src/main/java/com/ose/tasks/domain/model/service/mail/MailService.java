package com.ose.tasks.domain.model.service.mail;

import com.ose.auth.api.UserFeignAPI;
import com.ose.tasks.dto.mail.MailCreateDTO;
import com.ose.util.MailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

@Component
public class MailService implements MailInterface {

    private final static Logger logger = LoggerFactory.getLogger(MailService.class);


    @Value("${spring.mail.username}")
    private String mailFromAddress;

    private final UserFeignAPI userFeignAPI;

    @Autowired
    public MailService(
        UserFeignAPI userFeignAPI
    ) {
        this.userFeignAPI = userFeignAPI;

    }

    /**
     * 开始定时任务（查看是否为节假日）。
     */
//    @Scheduled(cron = "0 0 20 * * ?")
//    @Scheduled(cron = "0 57 11 * * ?")
    public void testMail() {
        logger.info("==========start==========");
        try {
            MailUtils.sendText(new InternetAddress("jtytesting@163.com"),new InternetAddress("jtynsh@163.com"),"test subject","test content");
        } catch (AddressException e) {
            throw new RuntimeException(e);
        }
        logger.info("==========end==========");
    }

    @Override
    public void sendEmail(MailCreateDTO dto) {
        logger.info("==========start send email==========");
        try {
            String signature = "\n\nOceanSTAR IDE｜Intelligent & Digital Engineering\n" +
                "\n" +
                "OceanSTAR Elite Group of Companies\n" +
                "HQ: 2 Venture Drive, #16-15,08,17,19,23,30,31 Vision Exchange,\n" +
                "Singapore 608526\n" +
                "WEB: www.oceanstar.com.sg\n" +
                "This message contains confidential information and is intended only for the individual(s) addressed in the message. If you are not the named addressee, you should not disseminate, distribute, or copy this email. If you are not the intended recipient, you are notified that disclosing, distributing, or copying this email is strictly prohibited.";
            MailUtils.sendText(new InternetAddress(mailFromAddress),new InternetAddress(dto.getEmail()),dto.getTitle(),dto.getContent() + signature);
        } catch (AddressException e) {
            throw new RuntimeException(e);
        }
        logger.info("==========end==========");
    }
}
