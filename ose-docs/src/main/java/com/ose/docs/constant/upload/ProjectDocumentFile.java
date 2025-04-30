package com.ose.docs.constant.upload;

import com.ose.docs.dto.FileMetadataDTO;
import com.ose.util.ImageUtils;

import static com.ose.constant.FileSizeValues.KB;
import static com.ose.constant.FileSizeValues.MB;

/**
 * 项目文档上传配置。
 */
public interface ProjectDocumentFile {

    String BIZ_TYPE = "project/ProjectDocumentES";

    long MAX_SIZE = 2048 * MB;

    String MIME_TYPE = "*/*";

    FileMetadataDTO.FileUploadConfig CONFIG = new FileMetadataDTO
        .FileUploadConfig()
        .setBizType(BIZ_TYPE)
        .setPublic(false)
        .setMaxSize(MAX_SIZE)
        .setReserveOriginal(true)
        .setImageCompression(
            new ImageUtils.ImageCompressionConfig()
                .setSize(540, 540)
                .setResizing(ImageUtils.Resizing.KEEP)
                .setExtentSize(256 * KB)
        )
        .setThumbnail(
            new ImageUtils.ImageCompressionConfig()
                .setSize(240, 240)
                .setResizing(ImageUtils.Resizing.CROP)
                .setExtentSize(64 * KB)
        );

}
