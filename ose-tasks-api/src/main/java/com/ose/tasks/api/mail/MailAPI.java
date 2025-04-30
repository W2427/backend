package com.ose.tasks.api.mail;

import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.mail.MailCreateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping(value = "/email")
public interface MailAPI {
    @Operation(
        summary = "发送邮件",
        description = "发送邮件"
    )
    @RequestMapping(
        method = POST,
        value = "/send"
    )
    @ResponseStatus(OK)
    JsonResponseBody sendEmail(
        @RequestBody @Parameter(description = "email信息") MailCreateDTO dto
    );
}
