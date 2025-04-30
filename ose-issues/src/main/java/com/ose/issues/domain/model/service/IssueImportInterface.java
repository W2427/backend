package com.ose.issues.domain.model.service;

import com.ose.dto.PageDTO;
import com.ose.issues.dto.IssueImportDTO;
import com.ose.issues.entity.IssueImportRecord;
import com.ose.issues.entity.IssueImportTemplate;
import com.ose.issues.vo.IssueType;
import com.ose.service.EntityInterface;
import org.springframework.data.domain.Page;

public interface IssueImportInterface extends EntityInterface {

    /**
     * 取得问题导入文件信息。
     *
     * @param projectId 项目 ID
     * @param issueType 问题类型
     * @return 问题导入文件信息
     */
    IssueImportTemplate getImportFile(
        Long projectId,
        IssueType issueType
    );

    /**
     * 取得问题导入文件信息。
     *
     * @param projectId 项目 ID
     * @param pageDTO 分页
     * @return 问题导入文件信息
     */
    Page<IssueImportRecord> getImportHistory(
        Long projectId,
        PageDTO pageDTO
    );

    /**
     * 重新生成问题导入文件。
     *
     * @param operatorId 操作者 ID
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param issueType  问题类型
     * @return 问题导入文件
     */
    IssueImportTemplate generateImportTemplate(
        Long operatorId,
        Long orgId,
        Long projectId,
        IssueType issueType
    );

    /**
     * 批量导入遗留问题。
     *
     * @param operatorId     操作人 ID
     * @param orgId          组织 ID
     * @param projectId      项目 ID
     * @param issueImportDTO 遗留问题导入信息
     */
    void importIssues(
        Long operatorId,
        Long orgId,
        Long projectId,
        IssueImportDTO issueImportDTO
    );

}
