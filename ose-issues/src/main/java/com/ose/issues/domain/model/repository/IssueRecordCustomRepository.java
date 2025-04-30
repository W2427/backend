package com.ose.issues.domain.model.repository;

import com.ose.dto.PageDTO;
import com.ose.issues.dto.IssueRecordCriteriaDTO;
import com.ose.issues.entity.IssueRecord;
import org.springframework.data.domain.Page;

public interface IssueRecordCustomRepository {
    Page<IssueRecord> search(Long issueId, IssueRecordCriteriaDTO issueRecordCriteriaDTO, PageDTO pageDTO);
}
