package com.ose.issues.domain.model.service;

import com.ose.dto.PageDTO;
import com.ose.issues.dto.IssueRecordCriteriaDTO;
import com.ose.issues.entity.IssueRecord;
import org.springframework.data.domain.Page;

public interface IssueRecordInterface {

    /**
     * 获取操作记录列表。
     *
     * @param orgId                  组织 ID
     * @param projectId              项目 ID
     * @param issueId                遗留问题ID
     * @param issueRecordCriteriaDTO 查询参数
     * @param pageDTO                分页参数
     * @return 遗留问题操作记录列表
     */
    Page<IssueRecord> search(
        Long orgId,
        Long projectId,
        Long issueId,
        IssueRecordCriteriaDTO issueRecordCriteriaDTO,
        PageDTO pageDTO
    );

}
