package com.ose.tasks.dto.bpm;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 实体数据传输对象
 */
public class TaskPrivilegeDTO extends BaseDTO {
    /**
     *
     */
    private static final long serialVersionUID = 7615182766136985152L;

    @Schema(description = "权限")
    private String category;

    @Schema(description = "权限类型")
    private String entitySubType;

    @Schema(description = "权限名称")
    private String name;

    @Schema(description = "操作人id")
    private Long assignee;

    @Schema(description = "用户信息")
    public TaskPrivilegeUserDTO user;

    public TaskPrivilegeDTO() {
    }

    public TaskPrivilegeDTO(String category, String entitySubType, String name, Long assignee) {
        super();
        this.category = category;
        this.entitySubType = entitySubType;
        this.assignee = assignee;
        this.name = name;
    }

    public TaskPrivilegeDTO(String entitySubType, String name, Long assignee) {
        super();
        this.entitySubType = entitySubType;
        this.assignee = assignee;
        this.name = name;
    }


    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getAssignee() {
        return assignee;
    }

    public void setAssignee(Long assignee) {
        this.assignee = assignee;
    }

    @JsonProperty(value = "assignee", access = JsonProperty.Access.READ_ONLY)
    public ReferenceData getAssigneeReference() {
        return this.assignee == null ? null : new ReferenceData(this.assignee);
    }

    @Override
    public Set<Long> relatedUserIDs() {
        Set<Long> relatedUserIDs = new HashSet<>();
        relatedUserIDs.add(this.assignee);
        return relatedUserIDs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskPrivilegeUserDTO getUser() {
        return user;
    }

    public void setUser(TaskPrivilegeUserDTO user) {
        this.user = user;
    }
}
