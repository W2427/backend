package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.material.FMaterialStructureNestType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * 结构套料方案创建更新DTO
 */
public class FMaterialStructureNestDTO extends BaseDTO {

    private static final long serialVersionUID = 8817987632555778485L;

    @Schema(description = "结构套料方案名称")
    private String name;

    @Schema(description = "结构套料方案编号")
    private String no;

    @Schema(description = "备注")
    private String memo;

    @Schema(description = "方案类型")
    @Enumerated(EnumType.STRING)
    private FMaterialStructureNestType type;

    @Schema(description = "领料单号")
    private String materialRequisitionsCode;

    @Schema(description = "spm 领料单ID, string")
    private String materialRequisitionsId;

    @Schema(description = "任务id")
    private String taskId;

    @Schema(description = "公司ID")
    private String companyId;

    @Schema(description = "公司业务代码")
    private String companyCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public FMaterialStructureNestType getType() {
        return type;
    }

    public void setType(FMaterialStructureNestType type) {
        this.type = type;
    }

    public String getMaterialRequisitionsCode() {
        return materialRequisitionsCode;
    }

    public void setMaterialRequisitionsCode(String materialRequisitionsCode) {
        this.materialRequisitionsCode = materialRequisitionsCode;
    }

    public String getMaterialRequisitionsId() {
        return materialRequisitionsId;
    }

    public void setMaterialRequisitionsId(String materialRequisitionsId) {
        this.materialRequisitionsId = materialRequisitionsId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
}
