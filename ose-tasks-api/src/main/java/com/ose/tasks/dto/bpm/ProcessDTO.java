package com.ose.tasks.dto.bpm;

import java.util.List;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;

import com.ose.tasks.vo.bpm.ProcessType;
import io.swagger.v3.oas.annotations.media.Schema;
import com.ose.tasks.vo.qc.ITPType;

/**
 * 工序管理 数据传输对象
 */
public class ProcessDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    // 工序名称
    private String nameCn;

    // 工序名称-英文
    private String nameEn;

    // 排序
    private short orderNo = 0;

    // 备注
    private String memo;

    // 工序分类id
    private Long processStageId;

    @Schema(description = "是否生成建造日志")
    private boolean constructLog;

    @Schema(description = "工序类型")
    private ProcessType processType;

    @Schema(description = "内检级别")
    @Enumerated(EnumType.STRING)
    private ITPType internalInspection;

    @Schema(description = "其他外检")
    @Enumerated(EnumType.STRING)
    private ITPType otherInspection;

    @Schema(description = "业主外检")
    @Enumerated(EnumType.STRING)
    private ITPType ownerInspection;

    @Schema(description = "第三方外检 ")
    @Enumerated(EnumType.STRING)
    private ITPType thirdPartyInspection;

    @Schema(description = "check清单文件临时名")
    private List<String> checkListFileName;

    @Schema(description = "工序阶段id")
    private Long processCategoryId;


    @Schema(description = "生成建造日志使用的类")
    private String constructionLogClass;


    @Schema(description = "生成报告使用的生成类")
    private String reportGenerateClass;

    @Schema(description = "功能块")
    private String funcPart;

    @Schema(description = "一次性处理报告，如入库外检等")
    private Boolean isOneOffReport;

    public ITPType getInternalInspection() {
        return internalInspection;
    }

    public void setInternalInspection(ITPType internalInspection) {
        this.internalInspection = internalInspection;
    }

    public ITPType getOtherInspection() {
        return otherInspection;
    }

    public void setOtherInspection(ITPType otherInspection) {
        this.otherInspection = otherInspection;
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public short getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(short orderNo) {
        this.orderNo = orderNo;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public Long getProcessStageId() {
        return processStageId;
    }

    public void setProcessStageId(Long processStageId) {
        this.processStageId = processStageId;
    }

    public Long getProcessCategoryId() {
        return processCategoryId;
    }

    public void setProcessCategoryId(Long processCategoryId) {
        this.processCategoryId = processCategoryId;
    }

    public List<String> getCheckListFileName() {
        return checkListFileName;
    }

    public void setCheckListFileName(List<String> checkListFileName) {
        this.checkListFileName = checkListFileName;
    }


    public boolean isConstructLog() {
        return constructLog;
    }

    public void setConstructLog(boolean constructLog) {
        this.constructLog = constructLog;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(ProcessType processType) {
        this.processType = processType;
    }

    public String getConstructionLogClass() {
        return constructionLogClass;
    }

    public void setConstructionLogClass(String constructionLogClass) {
        this.constructionLogClass = constructionLogClass;
    }

    public String getReportGenerateClass() {
        return reportGenerateClass;
    }

    public void setReportGenerateClass(String reportGenerateClass) {
        this.reportGenerateClass = reportGenerateClass;
    }

    public String getFuncPart() {
        return funcPart;
    }

    public void setFuncPart(String funcPart) {
        this.funcPart = funcPart;
    }

    public Boolean getOneOffReport() {
        return isOneOffReport;
    }

    public void setOneOffReport(Boolean oneOffReport) {
        isOneOffReport = oneOffReport;
    }
}
