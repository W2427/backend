package com.ose.materialspm.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ose.controller.BaseController;
import com.ose.dto.MailSenderInfo;
import com.ose.materialspm.api.DemoAPI;
import com.ose.materialspm.domain.model.service.DemoInterface;
import com.ose.materialspm.dto.DemoCriteriaDTO;
import com.ose.response.JsonResponseBody;
import com.ose.util.FileUtils;
import com.ose.util.SimpleMailSenderUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "Demo")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/demo")
public class DemoController extends BaseController implements DemoAPI {

    /**
     * 工序服务
     */
    private final DemoInterface demoService;

    /**
     * 构造方法
     *
     * @param demoService Demo服务
     */
    @Autowired
    public DemoController(DemoInterface demoService) {
        this.demoService = demoService;
    }

    /**
     * 获取工序列表
     *
     * @return 工序列表
     */
    @Override
    @Operation(
        summary = "查询Demo",
        description = "获取Demo list。"
    )
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    public JsonResponseBody getList(HttpServletRequest request,
                                    HttpServletResponse response,
                                    DemoCriteriaDTO criteriaDTO,
                                    @PathVariable @Parameter(description = "项目id") Long projectId,
                                    @PathVariable @Parameter(description = "orgId") Long orgId) {

        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost("smtp.yeah.net");
        mailInfo.setMailServerPort("465");
        mailInfo.setValidate(true);
        mailInfo.setUserName("chinaportservice@yeah.net");
        mailInfo.setPassword("Ose2018");// 您的邮箱密码
        mailInfo.setFromAddress("chinaportservice@yeah.net");
        mailInfo.setToAddress("13840877563@163.com,93091620@qq.com");
        mailInfo.setCcAddress("13840877563@163.com,93091620@qq.com");
        mailInfo.setSubject("确认问题邮件检查确认");
        mailInfo.setContent("问题邮件检查");
        mailInfo.setAttachFileNames(new String[]{"/var/www/ose/private/files/a.txt", "/var/www/ose/private/files/b.txt"});
        String tempEml = "/var/www/ose/private/files/ssss.eml";
        mailInfo.setTempEmlPath(tempEml);
        SimpleMailSenderUtils.sendTextMail(mailInfo);
        FileUtils.remove(tempEml);
        System.out.println("send OK");

        return new JsonResponseBody();

    }

}
