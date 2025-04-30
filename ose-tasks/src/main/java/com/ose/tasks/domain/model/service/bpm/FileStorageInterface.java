package com.ose.tasks.domain.model.service.bpm;

import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.bpm.FileMetadataDTO;
import com.ose.tasks.dto.bpm.ModelDeployDTO;
import com.ose.util.ImageUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static com.ose.constant.FileSizeValues.MB;
import static com.ose.constant.FileSizeValues.KB;

/**
 * 文件存储接口。
 */
public interface FileStorageInterface {


    long DOC_MAX_SIZE = 10 * MB;


    FileMetadataDTO.FileUploadConfig DOC_CONFIG = new FileMetadataDTO
        .FileUploadConfig()
        .setBizType("project/WBSEntitiesES")
        .setPublic(false)
        .setMaxSize(DOC_MAX_SIZE)
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
        Long orgId,
        FileMetadataDTO.FileUploadConfig fileUploadConfig,
        MultipartFile multipartFile
    );

    File resolveTemporaryFile(ModelDeployDTO documentPostDTO);
}
