package com.ose.docs.domain.model.service;

import com.ose.docs.dto.FileMetadataDTO;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.OperatorDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 文件存储接口。
 */
public interface FileStorageInterface {

    /**
     * 保存上传的文件。
     *
     * @param operator         上传者信息
     * @param orgId            组织 ID
     * @param fileUploadConfig 文件上传配置
     * @param multipartFile    上传的文件信息
     * @return 文件
     */
    File saveAsTemporaryFile(
        OperatorDTO operator,
        String orgId,
        FileMetadataDTO.FileUploadConfig fileUploadConfig,
        MultipartFile multipartFile
    );

    /**
     * 处理已上传的临时文件。
     *
     * @param operator          操作者信息
     * @param projectId         项目 ID
     * @param temporaryFileName 临时文件名
     * @param filePostDTO       文件附加信息
     * @return 文档数据实体
     */
    FileES resolveTemporaryFile(
        OperatorDTO operator,
        String projectId,
        String temporaryFileName,
        FilePostDTO filePostDTO
    );

}
