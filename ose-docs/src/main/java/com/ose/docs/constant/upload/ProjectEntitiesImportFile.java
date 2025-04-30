package com.ose.docs.constant.upload;

import com.ose.docs.dto.FileMetadataDTO;

import static com.ose.constant.FileSizeValues.MB;

/**
 * 项目实体导入文件上传配置。
 */
public interface ProjectEntitiesImportFile {

    String BIZ_TYPE = "project/WBSEntitiesES";

    long MAX_SIZE = 5 * MB;

    String MIME_TYPE = "application/(vnd.ms-excel|vnd.openxmlformats-officedocument.spreadsheetml.sheet)";

    FileMetadataDTO.FileUploadConfig CONFIG = new FileMetadataDTO
        .FileUploadConfig()
        .setBizType(BIZ_TYPE)
        .setPublic(false)
        .setMaxSize(MAX_SIZE)
        .setReserveOriginal(true);

}
