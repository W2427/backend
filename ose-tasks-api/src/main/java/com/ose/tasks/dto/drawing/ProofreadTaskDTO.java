package com.ose.tasks.dto.drawing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.dto.BaseDTO;
import com.ose.tasks.dto.bpm.TaskGatewayDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 图纸校审任务信息
 */
public class ProofreadTaskDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    @Schema(description = "任务 Id，123456")
    private String taskId;

    private String taskDefKey;

    private List<TaskGatewayDTO> gateway;

    @Schema(description = "设计人员")
    private Long drawUserId;

    @Schema(description = "校对人员")
    private Long checkUserId;

    @Schema(description = "审核人员")
    private Long approvedUserId;

    @Schema(description = "待提交图纸数量")
    private Integer initCount;

    @Schema(description = "校对中图纸数量")
    private Integer checkCount;

    @Schema(description = "审核中图纸数量")
    private Integer reviewCount;

    @Schema(description = "修改中图纸数量")
    private Integer modifyCount;

    @Schema(description = "已校对图纸数量")
    private Integer checkDoneCount;

    @Schema(description = "已审核图纸数量")
    private Integer reviewDoneCount;

    @Schema(description = "子图纸数量")
    private Integer totalCount;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public List<TaskGatewayDTO> getGateway() {
        return gateway;
    }

    public void setGateway(List<TaskGatewayDTO> gateway) {
        this.gateway = gateway;
    }

    public Long getDrawUserId() {
        return drawUserId;
    }

    public void setDrawUserId(Long drawUserId) {
        this.drawUserId = drawUserId;
    }

    public Long getCheckUserId() {
        return checkUserId;
    }

    public void setCheckUserId(Long checkUserId) {
        this.checkUserId = checkUserId;
    }

    public Long getApprovedUserId() {
        return approvedUserId;
    }

    public void setApprovedUserId(Long approvedUserId) {
        this.approvedUserId = approvedUserId;
    }


    @JsonProperty(value = "drawUserId", access = JsonProperty.Access.READ_ONLY)
    public ReferenceData getDrawUserIdReference() {
        return this.drawUserId == null ? null : new ReferenceData(this.drawUserId);
    }

    @JsonProperty(value = "checkUserId", access = JsonProperty.Access.READ_ONLY)
    public ReferenceData getCheckUserIdReference() {
        return this.checkUserId == null ? null : new ReferenceData(this.checkUserId);
    }

    @JsonProperty(value = "approvedUserId", access = JsonProperty.Access.READ_ONLY)
    public ReferenceData getApprovedUserIdReference() {
        return this.approvedUserId == null ? null : new ReferenceData(this.approvedUserId);
    }

    @Override
    public Set<Long> relatedUserIDs() {
        Set<Long> relatedUserIDs = new HashSet<>();
        relatedUserIDs.add(this.drawUserId);
        relatedUserIDs.add(this.checkUserId);
        relatedUserIDs.add(this.approvedUserId);
        return relatedUserIDs;
    }

    public Integer getInitCount() {
        return initCount;
    }

    public void setInitCount(Integer initCount) {
        this.initCount = initCount;
    }

    public Integer getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(Integer checkCount) {
        this.checkCount = checkCount;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Integer getModifyCount() {
        return modifyCount;
    }

    public void setModifyCount(Integer modifyCount) {
        this.modifyCount = modifyCount;
    }

    public Integer getCheckDoneCount() {
        return checkDoneCount;
    }

    public void setCheckDoneCount(Integer checkDoneCount) {
        this.checkDoneCount = checkDoneCount;
    }

    public Integer getReviewDoneCount() {
        return reviewDoneCount;
    }

    public void setReviewDoneCount(Integer reviewDoneCount) {
        this.reviewDoneCount = reviewDoneCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
