package com.ose.notifications.dto;

import com.ose.dto.BaseDTO;
import com.ose.notifications.vo.NotificationContentType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * 通知信息模版创建数据传输对象。
 */
public class NotificationTemplatePostDTO extends BaseDTO implements NotificationTemplateDTOInterface {

    private static final long serialVersionUID = -4047732818525155956L;

    @Schema(description = "模版名称")
    @NotBlank
    private String name;

    @Schema(description = "标题")
    @NotBlank
    @Size(max = 1000)
    private String title;

    @Schema(description = "消息内容类型")
    @NotNull
    private NotificationContentType contentType;

    @Schema(description = "内容模版")
    @NotBlank
    @Size(max = 100000)
    private String content;

    @Schema(description = "短信内容模版")
    @Size(max = 1000)
    private String text;

    private String tags;

    @Schema(description = "标签列表")
    private Set<String> tagList;

    @Schema(description = "备注")
    @Size(max = 1000)
    private String remarks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public NotificationContentType getContentType() {
        return contentType;
    }

    public void setContentType(NotificationContentType contentType) {
        this.contentType = contentType;
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Schema(hidden = true)
    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Set<String> getTagList() {
        return tagList;
    }

    public void setTagList(Set<String> tagList) {
        this.tagList = tagList;
        this.tags = (tagList == null || tagList.size() == 0) ? null : ("," + String.join(",", tagList) + ",");
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
