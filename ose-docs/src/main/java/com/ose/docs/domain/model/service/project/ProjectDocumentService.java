package com.ose.docs.domain.model.service.project;

import com.ose.docs.domain.model.repository.project.ProjectDocumentESRepository;
import com.ose.docs.entity.project.ProjectDocumentES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * 项目文档文件信息查询服务。
 */
@Component
public class ProjectDocumentService implements ProjectDocumentInterface {

    private final ProjectDocumentESRepository wbsEntitiesESRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public ProjectDocumentService(
        ProjectDocumentESRepository wbsEntitiesESRepository
    ) {
        this.wbsEntitiesESRepository = wbsEntitiesESRepository;
    }

    /**
     * 查询文件列表。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param pageable  分页参数
     * @return 文件分页数据
     */
    @Override
    public Page<ProjectDocumentES> list(String orgId, String projectId, Pageable pageable) {
        return wbsEntitiesESRepository
            .findByOrgIdAndProjectIdOrderByCommittedAtDesc(orgId.toString(), projectId.toString(), pageable);
    }

    /**
     * 取得文件详细信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param fileId    文件 ID
     * @return 文件详细信息
     */
    @Override
    public ProjectDocumentES get(String orgId, String projectId, String fileId) {
//        return wbsEntitiesESRepository.findById(fileId.toString()).orElse(null);
        return null;
    }

}
