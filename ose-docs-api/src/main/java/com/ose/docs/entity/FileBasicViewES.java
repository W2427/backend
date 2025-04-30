package com.ose.docs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.util.DateUtils;
import com.ose.util.LongUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Transient;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 文件 ES 数据实体。
 */
public final class FileBasicViewES extends FileBaseES implements FileView {

    private static final long serialVersionUID = -6401465684082413473L;

    @Schema(description = "业务类型")
    @Transient
    private String bizType;

    @Schema(description = "匹配的文本")
    @Transient
    private String matchedText;

    public String getBizType() {
        return bizType;
    }

    @Override
    public FileView setBizType(String bizType) {
        this.bizType = bizType;
        return this;
    }

    public String getMatchedText() {
        return matchedText;
    }

    @Override
    public FileView setMatchedText(String matchedText) {
        this.matchedText = matchedText;
        return this;
    }

    @Override
    @JsonIgnore
    public String getPath() {
        return super.getPath();
    }

    @Override
    @JsonIgnore
    public void setPath(String path) {
        super.setPath(path);
    }

    @Override
    @JsonIgnore
    public String getOriginal() {
        return super.getOriginal();
    }

    @Override
    @JsonIgnore
    public void setOriginal(String original) {
        super.setOriginal(original);
    }

    @Override
    @JsonIgnore
    public String getThumbnail() {
        return super.getThumbnail();
    }

    @Override
    @JsonIgnore
    public void setThumbnail(String thumbnail) {
        super.setThumbnail(thumbnail);
    }

    @JsonProperty(value = "committedAt", access = READ_ONLY)
    public String getCommittedAtISOString() {
        return DateUtils.toISOString(new Date(super.getCommittedAt()));
    }

    @JsonProperty(value = "committedBy", access = READ_ONLY)
    public ReferenceData getCommittedByRef() {
        return new ReferenceData(LongUtils.parseLong(super.getCommittedBy()));
    }

}
