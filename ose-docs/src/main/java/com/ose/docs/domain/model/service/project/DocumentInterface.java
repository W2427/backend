package com.ose.docs.domain.model.service.project;

import com.ose.docs.entity.project.ProjectBaseES;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 项目文档查询接口。
 *
 * @param <T> 文件信息数据实体范型
 */
public interface DocumentInterface<T extends ProjectBaseES> {

    /**
     * 查询文件列表。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param pageable  分页参数
     * @return 文件分页数据
     */
    Page<T> list(String orgId, String projectId, Pageable pageable);

    /**
     * 取得文件详细信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param fileId    文件 ID
     * @return 文件详细信息
     */
    T get(String orgId, String projectId, String fileId);

}
