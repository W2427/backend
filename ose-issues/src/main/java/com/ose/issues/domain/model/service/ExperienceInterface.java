package com.ose.issues.domain.model.service;

import com.ose.dto.PageDTO;
import com.ose.issues.dto.*;
import com.ose.issues.entity.Experience;
import com.ose.issues.entity.ExperienceMail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExperienceInterface {

    /**
     * 创建经验教训。
     *
     * @param operatorId          操作人ID
     * @param orgId               组织ID
     * @param projectId           项目ID
     * @param experienceCreateDTO 问题信息
     * @return 经验教训信息
     */
    Experience create(
        Long operatorId,
        Long orgId,
        Long projectId,
        ExperienceCreateDTO experienceCreateDTO
    );

    /**
     * 更新经验教训。
     *
     * @param operatorId          操作人ID
     * @param orgId               组织 ID
     * @param projectId           项目 ID
     * @param experienceId        问题 ID
     * @param experienceUpdateDTO 更新信息
     * @return 经验教训信息
     */
    Experience update(
        Long operatorId,
        Long orgId,
        Long projectId,
        Long experienceId,
        ExperienceUpdateDTO experienceUpdateDTO
    );

    /**
     * 更新经验状态。
     *
     * @param operatorId          操作人ID
     * @param orgId               组织 ID
     * @param projectId           项目 ID
     * @param experienceId        问题 ID
     * @param experienceStatusDTO 更新信息
     * @return 经验教训信息
     */
    Experience operate(
        Long operatorId,
        Long orgId,
        Long projectId,
        Long experienceId,
        ExperienceStatusDTO experienceStatusDTO
    );


    /**
     * 删除问题属性模板。
     *
     * @param operatorId   操作人ID
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param experienceId 属性ID
     */
    void delete(
        Long operatorId,
        Long orgId,
        Long projectId,
        Long experienceId
    );

    /**
     * 问题列表。
     *
     * @param orgId                 组织 ID
     * @param projectId             项目 ID
     * @param experienceCriteriaDTO 查询参数
     * @param pageable              分页参数
     * @return 问题列表
     */
    Page<Experience> search(
        Long orgId,
        Long projectId,
        IssueCriteriaDTO experienceCriteriaDTO,
        Pageable pageable
    );

    /**
     * 获取经验教训详情。
     *
     * @param experienceId 问题ID
     */
    Experience get(Long projectId, Long experienceId);

    /**
     * 发送邮件。
     *
     * @param operatorId        操作人ID
     * @param orgId             组织 ID
     * @param projectId         项目 ID
     * @param experienceId      问题 ID
     * @param experienceMailDTO 更新信息
     * @return 经验教训信息
     */
    ExperienceMail mail(
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
     * @return 经验教训信息
     */
    Page<ExperienceMailListDTO> getList(
        Long orgId,
        Long projectId,
        PageDTO pageDTO
    );

    /**
     * 取得经验教训历史列表
     *
     * @param experienceParentId
     * @return
     */
    List<Experience> getExperienceList(Long orgId, Long projectId, Long experienceParentId);

}
