package com.ose.docs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.util.DateUtils;
import com.ose.util.LongUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Transient;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 文件 ES 数据实体。
 */
public final class FileViewES extends FileES implements FileView {

    private static final long serialVersionUID = 2067687410127979553L;

    @Schema(description = "提交者 ID")
    @JsonIgnore
    private String committedBy;

    @Schema(description = "业务类型")
    @Transient
    private String bizType;

    @Schema(description = "匹配的文本")
    @Transient
    private String matchedText;

    @JsonProperty(value = "committedAt", access = READ_ONLY)
    public String getCommittedAtISOString() {
        return DateUtils.toISOString(new Date(super.getCommittedAt()));
    }

    @Override
    public String getCommittedBy() {
        return committedBy;
    }

    @Override
    public void setCommittedBy(String committedBy) {
        this.committedBy = committedBy;
    }

    @JsonSetter
    public void setCommittedBy(ReferenceData committedBy) {
        this.committedBy = committedBy.get$ref().toString();
    }

    @JsonProperty(value = "committedBy")
    public ReferenceData getCommittedByRef() {
        return new ReferenceData(LongUtils.parseLong(getCommittedBy()));
    }

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

}
