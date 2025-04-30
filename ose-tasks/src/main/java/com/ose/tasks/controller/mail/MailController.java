package com.ose.tasks.controller.mail;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.mail.MailAPI;
import com.ose.tasks.domain.model.service.mail.MailInterface;
import com.ose.tasks.dto.mail.MailCreateDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "节假日")
@RestController
@RequestMapping(value = "/email")
public class MailController extends BaseController implements MailAPI {

    private final MailInterface mailService;

    @Autowired
    public MailController(MailInterface mailService) {
        this.mailService = mailService;
    }

    @Override
    @Operation(
        summary = "编辑节假日",
        description = "编辑节假日"
    )
    @RequestMapping(
        method = POST,
        value = "/send"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody sendEmail(
        @RequestBody @Parameter(description = "节假日信息") MailCreateDTO dto

    ) {
        mailService.sendEmail(dto);
        return new JsonResponseBody();
    }
}
