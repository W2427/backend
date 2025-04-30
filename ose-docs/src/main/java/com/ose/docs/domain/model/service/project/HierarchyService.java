package com.ose.docs.domain.model.service.project;

import com.ose.docs.domain.model.repository.project.HierarchyESRepository;
import com.ose.docs.entity.project.HierarchyES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * 项目层级结构导入文件信息查询服务。
 */
@Component
public class HierarchyService implements HierarchyInterface {

    private final HierarchyESRepository hierarchyESRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public HierarchyService(
        HierarchyESRepository hierarchyESRepository
    ) {
        this.hierarchyESRepository = hierarchyESRepository;
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
    public Page<HierarchyES> list(String orgId, String projectId, Pageable pageable) {
        return hierarchyESRepository
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
    public HierarchyES get(String orgId, String projectId, String fileId) {
//        return hierarchyESRepository.findById(fileId.toString()).orElse(null);
        return null;
    }

}
