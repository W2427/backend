package com.ose.docs.constant.upload;

import com.ose.docs.dto.FileMetadataDTO;

import static com.ose.constant.FileSizeValues.MB;

public interface WpqrAttachment {

    String BIZ_TYPE = "wpqr/attachment";

    long MAX_SIZE = 32 * MB;

    String MIME_TYPE = "*/*";

    FileMetadataDTO.FileUploadConfig CONFIG = new FileMetadataDTO
        .FileUploadConfig()
        .setBizType(BIZ_TYPE)
        .setPublic(false)
        .setMaxSize(MAX_SIZE)
        .setReserveOriginal(true);
}
