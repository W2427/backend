package com.ose.tasks.domain.model.service;

import com.ose.dto.PageDTO;
import com.ose.issues.dto.ExperienceMailDTO;
import org.springframework.data.domain.PageImpl;

/**
 * 经验教训接口。
 */

public interface ExperienceMailInterface {

    /**
     * 发送邮件。
     *
     * @param operatorId        操作人ID
     * @param orgId             组织 ID
     * @param projectId         项目 ID
     * @param experienceId      经验 ID
     * @param experienceMailDTO 邮件信息
     * @return 经验教训信息
     */
    void mail(
        Long operatorId,
        Long orgId,
        Long projectId,
        Long experienceId,
        ExperienceMailDTO experienceMailDTO
    );

    /**
     * 邮件列表。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param pageDTO   分页信息
     * @return 经验教训信息
     */
    PageImpl getList(
        Long orgId,
        Long projectId,
        PageDTO pageDTO);

}
