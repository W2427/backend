package com.ose.issues.domain.model.service;

import com.ose.dto.PageDTO;
import com.ose.issues.domain.model.repository.IssueRecordRepository;
import com.ose.issues.dto.IssueRecordCriteriaDTO;
import com.ose.issues.entity.IssueRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class IssueRecordService implements IssueRecordInterface {

    private final IssueRecordRepository issueRecordRepository;

    @Autowired
    public IssueRecordService(IssueRecordRepository issueRecordRepository) {
        this.issueRecordRepository = issueRecordRepository;
    }

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
    @Override
    public Page<IssueRecord> search(
        final Long orgId,
        final Long projectId,
        final Long issueId,
        final IssueRecordCriteriaDTO issueRecordCriteriaDTO,
        final PageDTO pageDTO
    ) {
        return issueRecordRepository.search(issueId, issueRecordCriteriaDTO, pageDTO);
    }
}
