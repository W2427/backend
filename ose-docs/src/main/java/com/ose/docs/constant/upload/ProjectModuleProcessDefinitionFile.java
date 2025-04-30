package com.ose.docs.constant.upload;

import com.ose.docs.dto.FileMetadataDTO;

import static com.ose.constant.FileSizeValues.KB;

/**
 * 项目模块流程定义文件上传配置。
 */
public interface ProjectModuleProcessDefinitionFile {

    String BIZ_TYPE = "project/ModuleProcessDefinitionES";

    long MAX_SIZE = 256 * KB;

    String MIME_TYPE = "*/*";

    FileMetadataDTO.FileUploadConfig CONFIG = new FileMetadataDTO
        .FileUploadConfig()
        .setBizType(BIZ_TYPE)
        .setPublic(false)
        .setMaxSize(MAX_SIZE)
        .setReserveOriginal(true);

}
