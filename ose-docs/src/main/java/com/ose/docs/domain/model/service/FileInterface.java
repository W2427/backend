package com.ose.docs.domain.model.service;

import com.ose.docs.dto.FileCriteriaDTO;
import com.ose.docs.entity.FileBaseES;
import com.ose.docs.entity.FileBasicViewES;
import com.ose.docs.entity.FileViewES;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 文件接口。
 */
public interface FileInterface {

    /**
     * 查询文件。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param criteria  查询条件
     * @param pageable  分页参数
     * @return 文件分页数据
     */
    Page<FileBasicViewES> search(
        String orgId,
        String projectId,
        FileCriteriaDTO criteria,
        Pageable pageable
    );

    /**
     * 取得文件信息。
     *
     * @param orgId  组织 ID
     * @param fileId 文件 ID
     * @return 文件信息
     */
    FileViewES get(String orgId, String fileId);

    /**
     * 取得文件路径信息。
     *
     * @param orgId  组织 ID
     * @param fileId 文件 ID
     * @return 文件信息
     */
    FileBaseES paths(String orgId, String fileId);

    /**
     * 取得文件路径信息。
     *
     * @param orgId  组织 ID
     * @param projectId  项目 ID
     * @param fileId 文件 ID
     * @return 文件信息
     */
    FileBaseES paths(String orgId, String projectId, String fileId);

}
