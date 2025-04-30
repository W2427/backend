package com.ose.tasks.domain.model.service;

import com.ose.dto.PageDTO;
import com.ose.issues.api.ExperienceFeignAPI;
import com.ose.issues.dto.ExperienceMailDTO;
import com.ose.issues.dto.ExperienceMailListDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.util.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.InternetAddress;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExperienceMailService implements ExperienceMailInterface {

    @Value("${spring.mail.username}")
    private String mailFromAddress;

    private final ExperienceFeignAPI experienceFeignAPI;


    /**
     * 构造方法。
     *
     * @param experienceFeignAPI
     */
    @Autowired
    public ExperienceMailService(ExperienceFeignAPI experienceFeignAPI) {
        this.experienceFeignAPI = experienceFeignAPI;
    }

    /**
     * 发送经验教训邮件。
     *
     * @param operatorId   操作人ID
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param experienceId 经验教训ID
     * @return 问题信息
     */
    @Transactional
    public void mail(
        final Long operatorId,
        final Long orgId,
        final Long projectId,
        final Long experienceId,
        final ExperienceMailDTO experienceMailDTO
    ) {

        try {
            List<String> pathLists = new ArrayList<>();
            String toAddress = experienceMailDTO.getToAddress().replaceAll(";", ",");
            String[] toAddressarr = toAddress.split(",");
            InternetAddress[] tos = new InternetAddress[toAddressarr.length];

            for (int i = 0; i < toAddressarr.length; i++) {
                tos[i] = new InternetAddress(toAddressarr[i]);
            }

            String ccAddress = experienceMailDTO.getCcAddress().replaceAll(";", ",");
            String[] ccAddressarr = ccAddress.split(",");
            InternetAddress[] ccs = new InternetAddress[ccAddressarr.length];

            for (int i = 0; i < ccAddressarr.length; i++) {
                ccs[i] = new InternetAddress(ccAddressarr[i]);
            }

            MailUtils.send(new InternetAddress(mailFromAddress), tos, ccs, experienceMailDTO.getMailSubject(), experienceMailDTO.getComments(), "text/html; charset=utf-8", pathLists);

        } catch (Exception e) {

            e.printStackTrace(System.out);

        }

        experienceFeignAPI.mail(orgId, projectId, experienceId, experienceMailDTO);

    }

    /**
     * 获取经验教训邮件
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return 经验教训邮件列表
     */
    @Override
    public PageImpl getList(
        Long orgId,
        Long projectId,
        PageDTO pageDTO) {
        JsonListResponseBody<ExperienceMailListDTO> list = experienceFeignAPI.getList(orgId, projectId, pageDTO.getPage().getSize(), pageDTO.getPage().getNo());
        return new PageImpl<>(
            list.getData(),
            pageDTO.toPageable(),
            list.getMeta().getCount());
    }

}
