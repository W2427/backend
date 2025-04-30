package com.ose.docs.constant.upload;


import com.ose.docs.dto.FileMetadataDTO;

import static com.ose.constant.FileSizeValues.MB;

/**
 * 额定工时导入文件上传配置。
 */
public interface RatedTimeImportFile {

    String BIZ_TYPE = "project/RatedTimeES";

    long MAX_SIZE = 32 * MB;

    String MIME_TYPE = "*/*";

    FileMetadataDTO.FileUploadConfig CONFIG = new FileMetadataDTO
        .FileUploadConfig()
        .setBizType(BIZ_TYPE)
        .setPublic(false)
        .setMaxSize(MAX_SIZE)
        .setReserveOriginal(true);

}
