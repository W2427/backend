package com.ose.issues.domain.model.service;

import com.ose.auth.api.OrganizationFeignAPI;
import com.ose.dto.ContextDTO;
import com.ose.issues.dto.*;
import com.ose.issues.entity.Issue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface IssueInterface {

    /**
     * 创建遗留问题。
     *
     * @param operatorId     操作人ID
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param issueCreateDTO 问题信息
     * @return 遗留问题信息
     */
    Issue create(
        Long operatorId,
        Long orgId,
        Long projectId,
        IssueCreateDTO issueCreateDTO
    );

    /**
     * 更新问题信息。
     *
     * @param operatorId     操作人ID
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param issueId        问题ID
     * @param issueUpdateDTO 更新信息
     * @return 遗留问题信息
     */
    Issue update(
        Long operatorId,
        Long orgId,
        Long projectId,
        Long issueId,
        IssueUpdateDTO issueUpdateDTO
    );

    /**
     * 查询问题。
     *
     * @param orgId            组织ID
     * @param projectId        项目ID
     * @param issueCriteriaDTO 查询参数
     * @param pageable         分页参数
     * @return 问题列表
     */
    Page<Issue> search(
        Long orgId,
        Long projectId,
        IssueCriteriaDTO issueCriteriaDTO,
        Pageable pageable
    );

    /**
     * 查询遗留问题。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询参数
     * @return 遗留问题列表
     */
    List<Issue> search(
        Long orgId,
        Long projectId,
        IssueCriteriaDTO criteriaDTO
    );

    /**
     * 获取遗留问题详情。
     *
     * @param issueId 问题ID
     */
    Issue get(Long projectId, Long issueId);

    /**
     * 移交遗留问题。
     *
     * @param operatorId       操作人ID
     * @param orgId            组织ID
     * @param projectId        项目ID
     * @param issueTransferDTO 更新信息
     */
    void transfer(
        Long operatorId,
        Long orgId,
        Long projectId,
        IssueTransferDTO issueTransferDTO
    );

    /**
     * 查询遗留问题内全部部门
     *
     * @param orgId            组织ID
     * @param projectId        项目ID
     * @param issueCriteriaDTO
     * @param orgFeignAPI
     * @return
     */
    List<IssueDepartmentDTO> departments(
        Long orgId,
        Long projectId,
        IssueCriteriaDTO issueCriteriaDTO,
        OrganizationFeignAPI orgFeignAPI
    );


    /**
     * 返回遗留意见、经验教训的xls下载文件
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param issueType
     * @param operatorId
     * @return
     */
    File saveDownloadFile(Long orgId, Long projectId, String issueType, Long operatorId);

    List<Map<String, Object>> getDepartments(Long projectId, IssueCriteriaDTO issueCriteriaDTO);

    List<Map<String, Object>> getSources(Long projectId, IssueCriteriaDTO issueCriteriaDTO);

    List<Map<String, Object>> getModules(Long projectId, IssueCriteriaDTO issueCriteriaDTO);

    Map<String,String> getColumnHeaderMap(Long projectId);

    List<Map<String, Object>> getSystems(Long projectId, IssueCriteriaDTO issueCriteriaDTO);

    List<Map<String, Object>> getDisciplines(Long projectId, IssueCriteriaDTO issueCriteriaDTO);

    void open(
        Long orgId,
        Long projectId,
        Long issueId,
        ContextDTO contextDTO
    );

    void close(
        Long orgId,
        Long projectId,
        Long issueId,
        ContextDTO contextDTO
    );
}
