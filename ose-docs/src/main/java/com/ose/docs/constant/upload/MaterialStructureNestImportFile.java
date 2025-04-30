package com.ose.docs.constant.upload;

import com.ose.docs.dto.FileMetadataDTO;

import static com.ose.constant.FileSizeValues.MB;

/**
 * 结构套料导入文件上传配置。
 */
public interface MaterialStructureNestImportFile {

    String BIZ_TYPE = "project/MaterialStructureES";

    long MAX_SIZE = 32 * MB;

    String MIME_TYPE = "application/(vnd.ms-excel|vnd.openxmlformats-officedocument.spreadsheetml.sheet)";

    FileMetadataDTO.FileUploadConfig CONFIG = new FileMetadataDTO
        .FileUploadConfig()
        .setBizType(BIZ_TYPE)
        .setPublic(false)
        .setMaxSize(MAX_SIZE)
        .setReserveOriginal(true);
}
