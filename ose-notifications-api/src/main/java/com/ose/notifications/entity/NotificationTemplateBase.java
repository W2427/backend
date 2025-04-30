package com.ose.notifications.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.notifications.vo.NotificationContentType;
import com.ose.util.CryptoUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 通知模版数据实体。
 */
@MappedSuperclass
public class NotificationTemplateBase extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -6996381874006550435L;

    @Schema(description = "组织 ID")
    @Column
    private Long orgId;

    @Schema(description = "修订分组 ID")
    @JsonIgnore
    @Column
    private Long revisionId;

    @Schema(description = "模版名称")
    @Column
    private String name;

    @Schema(description = "标题")
    @Lob
    @Column(length = 1020)
    private String title;

    @Schema(description = "消息内容类型")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private NotificationContentType contentType = NotificationContentType.TEXT;

    @Column
    @JsonIgnore
    private String tags;

    @Schema(description = "是否已停用")
    @Column
    private Boolean disabled = false;

    public NotificationTemplateBase() {
        super();
        setRevisionId(CryptoUtils.uniqueDecId());
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(Long revisionId) {
        this.revisionId = revisionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public NotificationContentType getContentType() {
        return contentType;
    }

    public void setContentType(NotificationContentType contentType) {
        this.contentType = contentType;
    }

    @Schema(hidden = true)
    public String getTags() {
        return tags;
    }

    @Schema(description = "标签列表")
    @JsonProperty(value = "tagList", access = JsonProperty.Access.READ_ONLY)
    public Set<String> getTagList() {

        Set<String> tags = this.tags == null
            ? new HashSet<>()
            : new HashSet<>(Arrays.asList(this.tags.split(",")));

        tags.remove("");

        return tags;
    }

    @JsonSetter
    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setTags(Set<String> tags) {
        if (tags == null || tags.size() == 0) {
            this.tags = null;
        } else {
            this.tags = "," + String.join(",", tags) + ",";
        }
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

}
