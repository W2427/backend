package com.ose.tasks.domain.model.service.mail;

import com.ose.tasks.dto.mail.MailCreateDTO;

public interface MailInterface {

    /**
     * 发送邮件
     * @param dto
     */
    void sendEmail(
        MailCreateDTO dto
    );
}
