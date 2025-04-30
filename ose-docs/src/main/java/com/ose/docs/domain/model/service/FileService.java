package com.ose.docs.domain.model.service;

import com.ose.docs.domain.model.repository.FileESRepository;
import com.ose.docs.dto.FileCriteriaDTO;
import com.ose.docs.entity.FileBaseES;
import com.ose.docs.entity.FileBasicViewES;
import com.ose.docs.entity.FileViewES;
import com.ose.exception.NotFoundError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * 文件存储服务。
 */
@Component
public class FileService implements FileInterface {

    // 文件信息服务
    private final FileESRepository fileESRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public FileService(
        FileESRepository fileESRepository
    ) {
        this.fileESRepository = fileESRepository;
    }

    /**
     * 查询文件。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param criteria  查询条件
     * @param pageable  分页参数
     * @return 文件分页数据
     */
    @Override
    public Page<FileBasicViewES> search(
        String orgId,
        String projectId,
        FileCriteriaDTO criteria,
        Pageable pageable
    ) {
        return fileESRepository.search(orgId.toString(), projectId.toString(), criteria, pageable);
    }

    /**
     * 取得文件信息。
     *
     * @param orgId  组织 ID
     * @param fileId 文件 ID
     * @return 文件信息
     */
    @Override
    public FileViewES get(String orgId, String fileId) {

        FileViewES file = fileESRepository
            .getFileInfo(fileId.toString(), orgId.toString())
            .orElse(null);

        if (file == null) {
            throw new NotFoundError();
        }

        return file;
    }

    /**
     * 取得文件路径信息。
     *
     * @param orgId  组织 ID
     * @param fileId 文件 ID
     * @return 文件信息
     */
    @Override
    public FileBaseES paths(String orgId, String fileId) {

        FileBaseES file = fileESRepository
            .getFilePaths(fileId.toString(), orgId.toString())
            .orElse(null);

        if (file == null) {
            throw new NotFoundError();
        }

        return file;
    }

    /**
     * 取得项目文件路径信息。
     *
     * @param orgId  组织 ID
     * @param projectId 项目 ID
     * @param fileId 文件 ID
     * @return 文件信息
     */
    @Override
    public FileBaseES paths(String orgId, String projectId, String fileId) {

        FileBaseES file = fileESRepository
            .getFilePaths(fileId.toString(), orgId.toString(), projectId.toString())
            .orElse(null);

        if (file == null) {
            throw new NotFoundError();
        }

        return file;
    }

}
