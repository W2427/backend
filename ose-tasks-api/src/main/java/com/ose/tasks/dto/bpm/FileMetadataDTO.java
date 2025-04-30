package com.ose.tasks.dto.bpm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;
import com.ose.util.ImageUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * 文件元数据。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileMetadataDTO extends BaseDTO {

    private static final long serialVersionUID = 5926166938250010571L;

    // 原始文件名
    private String filename;

    // 文件媒体类型
    private String mimeType;

    // 上传时间
    private Date uploadedAt;

    // 上传者 ID
    private Long uploadedBy;

    // 所属组织 ID
    private Long orgId;

    // 文件的业务类型
    private FileUploadConfig config;

    @JsonCreator
    public FileMetadataDTO() {
    }

    public FileMetadataDTO(
        Long userId,
        Long orgId,
        FileUploadConfig config,
        MultipartFile multipartFile
    ) {
        setFilename(multipartFile.getOriginalFilename());
        setMimeType(multipartFile.getContentType());
        setUploadedAt(new Date());
        setUploadedBy(userId);
        setOrgId(orgId);
        setConfig(config);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Date getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Long getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(Long uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public FileUploadConfig getConfig() {
        return config;
    }

    public void setConfig(FileUploadConfig config) {
        this.config = config;
    }

    /**
     * 业务类型配置。
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FileUploadConfig {

        // 文件的业务类型
        private String bizType = null;

        // 是否为开放文件
        private boolean isPublic = false;

        // 文件的最大上传大小
        private long maxSize = 0L;

        // 是否保留原始文件
        private boolean reserveOriginal = false;

        // 压缩图配置
        private ImageUtils.ImageCompressionConfig imageCompression = null;

        // 缩略图配置
        private ImageUtils.ImageCompressionConfig thumbnail = null;

        @JsonCreator
        public FileUploadConfig() {
        }

        public String getBizType() {
            return bizType;
        }

        public FileUploadConfig setBizType(String bizType) {
            this.bizType = bizType;
            return this;
        }

        public boolean isPublic() {
            return isPublic;
        }

        public FileUploadConfig setPublic(boolean aPublic) {
            isPublic = aPublic;
            return this;
        }

        public long getMaxSize() {
            return maxSize;
        }

        public FileUploadConfig setMaxSize(long maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public boolean isReserveOriginal() {
            return reserveOriginal;
        }

        public FileUploadConfig setReserveOriginal(boolean reserveOriginal) {
            this.reserveOriginal = reserveOriginal;
            return this;
        }

        public ImageUtils.ImageCompressionConfig getImageCompression() {
            return imageCompression;
        }

        public FileUploadConfig setImageCompression(
            ImageUtils.ImageCompressionConfig imageCompression
        ) {
            this.imageCompression = imageCompression;
            return this;
        }

        public ImageUtils.ImageCompressionConfig getThumbnail() {
            return thumbnail;
        }

        public FileUploadConfig setThumbnail(
            ImageUtils.ImageCompressionConfig thumbnail
        ) {
            this.thumbnail = thumbnail;
            return this;
        }

    }

}
