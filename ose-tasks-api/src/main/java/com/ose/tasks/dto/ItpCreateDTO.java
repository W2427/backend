package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.qc.ITPType;
import io.swagger.v3.oas.annotations.media.Schema;

public class ItpCreateDTO extends BaseDTO {

    private static final long serialVersionUID = 5422644518971877282L;

    @Schema(description = "实体类型")
    private String wbsEntityType;

    @Schema(description = "工序阶段")
    private Long processStageId;

    @Schema(description = "工序")
    private Long processId;

    @Schema(description = "内检等级")
    private ITPType internalInspection;

    @Schema(description = "业主外检")
    private ITPType ownerInspection;

    @Schema(description = "第三方外检")
    private ITPType thirdPartyInspection;

    @Schema(description = "其他外检")
    private ITPType otherInspection;

    public String getEntityType() {
        return wbsEntityType;
    }

    public void setWbsEntityType(String wbsEntityType) {
        this.wbsEntityType = wbsEntityType;
    }

    public Long getProcessStageId() {
        return processStageId;
    }

    public void setProcessStageId(Long processStageId) {
        this.processStageId = processStageId;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public ITPType getInternalInspection() {
        return internalInspection;
    }

    public void setInternalInspection(ITPType internalInspection) {
        this.internalInspection = internalInspection;
    }

    public ITPType getOwnerInspection() {
        return ownerInspection;
    }

    public void setOwnerInspection(ITPType ownerInspection) {
        this.ownerInspection = ownerInspection;
    }

    public ITPType getThirdPartyInspection() {
        return thirdPartyInspection;
    }

    public void setThirdPartyInspection(ITPType thirdPartyInspection) {
        this.thirdPartyInspection = thirdPartyInspection;
    }

    public ITPType getOtherInspection() {
        return otherInspection;
    }

    public void setOtherInspection(ITPType otherInspection) {
        this.otherInspection = otherInspection;
    }
}
