package com.ose.tasks.entity.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.util.LongUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Entity
@Table(name = "wps")
public class Wps extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -7722356964930253307L;

    @Column
    private Long orgId;

    @Column
    private Long projectId;

    @Column
    private String code;

    @Column
    private String pqrNo;

//    @Column(length = 1600)
//    private String baseMetal;
//
//    @Column(length = 1600)
//    private String baseMetalAlias;
//
//    @Column(length = 1600)
//    private String fillerMetal;

    @Column(length = 160)
    private String process;

    @Column(length = 1600)
    // detail list , 分割
    private String details;

    @Column(length = 5000)
    private String remark;

    @Column
    private Long fileId;

    @Transient
    private List<WpsDetail> wpsDetails;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPqrNo() {
        return this.pqrNo;
    }

    public void setPqrNo(String pqrNo) {
        this.pqrNo = pqrNo;
    }
//
//    public String getBaseMetal() {
//        return baseMetal;
//    }
//
//    public void setBaseMetal(String baseMetal) {
//        this.baseMetal = baseMetal;
//    }
//
//    public String getBaseMetalAlias() {
//        return baseMetalAlias;
//    }
//
//    public void setBaseMetalAlias(String baseMetalAlias) {
//        this.baseMetalAlias = baseMetalAlias;
//    }
//
//    public String getFillerMetal() {
//        return fillerMetal;
//    }
//
//    public void setFillerMetal(String fillerMetal) {
//        this.fillerMetal = fillerMetal;
//    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public List<Long> getDetails() {
        if (this.details == null) {
            return new ArrayList<>();
        }
        return LongUtils.change2Str(this.details.split(","));
    }

    public String getDetailsText() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @JsonProperty(value = "details", access = READ_ONLY)
    public List<ReferenceData> getWpsDetailRef() {

        if (this.details == null) {
            return new ArrayList<>();
        }

        List<ReferenceData> wpsDetailList = new ArrayList();
        for (String detail : this.details.split(",")) {
            wpsDetailList.add(new ReferenceData(LongUtils.parseLong(detail)));
        }

        return wpsDetailList;
    }

    public List<WpsDetail> getWpsDetails() {
        return wpsDetails;
    }

    public void setWpsDetails(List<WpsDetail> wpsDetails) {
        this.wpsDetails = wpsDetails;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}
