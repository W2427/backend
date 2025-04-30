package com.ose.tasks.dto.bpm;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.ose.dto.BaseDTO;

import com.ose.issues.vo.IssueCategory;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 实体管理 数据传输对象
 */
public class InternalInspectionIssueDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    @Schema(description = "流程ID")
    private Long actInstId;

    @Schema(description = "流程ID集合")
    private List<Long> actInstIds = new ArrayList<>();

    @Schema(description = "附件临时文件名")
    private String attachment;

    @Schema(description = "意见等级")
    @Enumerated(EnumType.STRING)
    private IssueCategory category;

    @Schema(description = "意见描述")
    private String description;


    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public IssueCategory getEntitySubType() {
        return category;
    }

    public void setEntitySubType(IssueCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public List<Long> getActInstIds() {
        return actInstIds;
    }

    public void setActInstIds(List<Long> actInstIds) {
        this.actInstIds = actInstIds;
    }

}
