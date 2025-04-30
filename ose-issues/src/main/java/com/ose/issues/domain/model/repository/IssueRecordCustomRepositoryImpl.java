package com.ose.issues.domain.model.repository;

import com.ose.dto.PageDTO;
import com.ose.issues.dto.IssueRecordCriteriaDTO;
import com.ose.issues.entity.IssueRecord;
import com.ose.repository.BaseRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;

public class IssueRecordCustomRepositoryImpl extends BaseRepository implements IssueRecordCustomRepository {

    /**
     * 获取遗留问题操作记录列表
     *
     * @param issueRecordCriteriaDTO 查询参数
     * @param pageDTO                分页参数
     * @return 操作记录列表
     */
    @Override
    public Page<IssueRecord> search(
        Long issueId,
        IssueRecordCriteriaDTO issueRecordCriteriaDTO,
        PageDTO pageDTO
    ) {

        return getSQLQueryBuilder(IssueRecord.class)
            .is("issueId", issueId)
            .is("status", EntityStatus.ACTIVE)
            .between(
                "createdAt",
                issueRecordCriteriaDTO.getStartTime(),
                issueRecordCriteriaDTO.getEndTime()
            )
            .paginate(pageDTO.toPageable())
            .exec()
            .page();
    }
}
