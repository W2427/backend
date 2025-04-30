package com.ose.notifications.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 通知信息模版数据传输对象。
 */
public class NotificationTemplateBasicDTO extends BaseDTO {

    private static final long serialVersionUID = -1668243016000017391L;

    @Schema(description = "模版 ID")
    private Long id;

    @Schema(description = "模版名称")
    private String name;

    @Schema(description = "标题")
    private String title;

    public NotificationTemplateBasicDTO() {
    }

    public NotificationTemplateBasicDTO(Long id, String name, String title) {
        setId(id);
        setName(name);
        setTitle(title);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

}
