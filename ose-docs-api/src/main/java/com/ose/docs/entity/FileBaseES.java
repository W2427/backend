package com.ose.docs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ose.docs.vo.FileCategory;
import com.ose.util.FileUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 文件 ES 数据实体。
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileBaseES extends BaseES {

    private static final long serialVersionUID = -5337547339628882575L;

    @Schema(description = "文件名")
    @Field(type = FieldType.Text)
    private String name;

    @Schema(description = "MIME 主类型")
    @Field(type = FieldType.Keyword)
    private String type = null;

    @Schema(description = "MIME 子类型")
    @Field(type = FieldType.Keyword)
    private String subType = null;

    @Schema(description = "文件类型")
    @Field(type = FieldType.Keyword)
    @Enumerated(EnumType.STRING)
    private List<FileCategory> categories;

    @Schema(hidden = true)
    @Field(type = FieldType.Text, index = false)
    private String path;

    @Schema(hidden = true)
    @Field(type = FieldType.Text, index = false)
    private String original;

    @Schema(hidden = true)
    @Field(type = FieldType.Text, index = false)
    private String thumbnail;

    @Schema(description = "文件 URI")
    @Field(type = FieldType.Object, index = false)
    private FileURIs uris = new FileURIs();

    @Schema(description = "提交时间 ID")
    @Field(type = FieldType.Date)
    private Long committedAt;

    @Schema(description = "提交者 ID")
    @Field(type = FieldType.Keyword)
    private String committedBy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public List<FileCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<FileCategory> categories) {
        this.categories = categories;
    }

    @JsonIgnore
    public String getMimeType() {

        if (StringUtils.isEmpty(type) || StringUtils.isEmpty(subType)) {
            return "";
        }

        return type + "/" + subType;
    }

    @JsonIgnore
    public void setMimeType(String mimeType) {

        String[] parts = mimeType.split("/");

        if (parts.length != 2) {
            return;
        }

        type = parts[0];
        subType = parts[1];
        categories = FileCategory.resolveMimeType(mimeType, name);

        if (StringUtils.isEmpty(uris.getThumbnail())) {
            uris.setThumbnail(FileCategory.icon(categories));
        }

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        this.uris.setPath(
            String.format(
                "/orgs/%s/files/%s",
                this.getOrgId(),
                this.getId()
            )
        );
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
        this.uris.setOriginal(
            String.format(
                "/orgs/%s/files/%s/original",
                this.getOrgId(),
                this.getId()
            )
        );
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        this.uris.setThumbnail(
            String.format(
                "/orgs/%s/files/%s/thumbnail",
                this.getOrgId(),
                this.getId()
            )
        );
    }

    @JsonIgnore
    public void setPaths(
        String basePath,
        String path,
        String original,
        String thumbnail
    ) {

        if (path != null || original != null) {
            setPath(
                path != null
                    ? FileUtils.relative(basePath, path)
                    : FileUtils.relative(basePath, original)
            );
        }

        if (original != null) {
            setOriginal(FileUtils.relative(basePath, original));
        }

        if (thumbnail != null) {
            setThumbnail(FileUtils.relative(basePath, thumbnail));
        }

    }

    public FileURIs getUris() {
        return uris;
    }

    public void setUris(FileURIs uris) {
        this.uris = uris;
    }

    public Long getCommittedAt() {
        return committedAt;
    }

    public void setCommittedAt(Long committedAt) {
        this.committedAt = committedAt;
    }

    public String getCommittedBy() {
        return committedBy;
    }

    public void setCommittedBy(String committedBy) {
        this.committedBy = committedBy;
    }

    @Override
    public Set<Long> relatedUserIDs() {

        Set<Long> userIDs = new HashSet<>();

        if (!StringUtils.isEmpty(this.getCommittedBy())) {
            userIDs.add(LongUtils.parseLong(this.getCommittedBy()));
        }

        return userIDs;
    }

    /**
     * 文件 URI。
     */
    public static class FileURIs {

        @Schema(description = "文件 URI")
        @Field(type = FieldType.Text, index = false)
        private String path;

        @Schema(description = "原文件 URI")
        @Field(type = FieldType.Text, index = false)
        private String original;

        @Schema(description = "缩略图 URI")
        @Field(type = FieldType.Text, index = false)
        private String thumbnail;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

    }

}
