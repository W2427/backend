package com.ose.tasks.dto.wps;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class WpsUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = -8933061993012194300L;

    @Schema(description = "名称")
    private String code;

    @Schema(description = "PQR编号")
    private List<String> pqrNo;

//    @Schema(description = "基础材料")
//    private List<String> baseMetal;
//
//    @Schema(description = "母材别名")
//    private List<String> baseMetalAlias;

    @Schema(description = "焊接方式")
    private List<String> process;
//
//    @Schema(description = "填充材料")
//    private List<String> fillerMetal;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "WPS 详情")
    private List<WpsDetailDTO> details;

    @Schema(description = "文件 ID")
    private Long fileId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getPqrNo() {
        return pqrNo;
    }

    public void setPqrNo(List<String> pqrNo) {
        this.pqrNo = pqrNo;
    }
//
//    public List<String> getBaseMetal() {
//        return baseMetal;
//    }
//
//    public void setBaseMetal(List<String> baseMetal) {
//        this.baseMetal = baseMetal;
//    }
//
//    public List<String> getBaseMetalAlias() {
//        return baseMetalAlias;
//    }
//
//    public void setBaseMetalAlias(List<String> baseMetalAlias) {
//        this.baseMetalAlias = baseMetalAlias;
//    }

    public List<String> getProcess() {
        return process;
    }

    public void setProcess(List<String> process) {
        this.process = process;
    }
//
//    public List<String> getFillerMetal() {
//        return fillerMetal;
//    }
//
//    public void setFillerMetal(List<String> fillerMetal) {
//        this.fillerMetal = fillerMetal;
//    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<WpsDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<WpsDetailDTO> details) {
        this.details = details;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}
