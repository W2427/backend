package com.ose.issues.domain.model.repository;

import com.ose.issues.entity.IssueImportTemplate;
import com.ose.issues.vo.IssueType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IssueImportTemplateRepository extends CrudRepository<IssueImportTemplate, Long> {

    /**
     * 取得问题导入文件信息。
     *
     * @param projectId 项目 ID
     * @param issueType 问题类型
     * @return 问题导入文件信息
     */
    Optional<IssueImportTemplate> findByProjectIdAndIssueType(Long projectId, IssueType issueType);

}
