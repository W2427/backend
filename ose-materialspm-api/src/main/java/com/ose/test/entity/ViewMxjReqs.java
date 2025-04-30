package com.ose.test.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;

/**
 * 请购单实体类。
 */
@Entity
@Table(name = "V_MXJ_REQS")
@NamedQuery(name = "ViewMxjReqs.findAll", query = "SELECT a FROM ViewMxjReqs a")
public class ViewMxjReqs extends BaseDTO {

    private static final long serialVersionUID = -5725891260099927636L;


    /**
     * 构造方法。
     */
    public ViewMxjReqs() {
    }

    @Id
    @Column(name = "R_ID")
    private String id;

    //项目id
    @Column(name = "PROJ_ID", nullable = false, length = 20)
    private Long projectId;

    //请购单编号
    @Column(name = "R_CODE", nullable = false, length = 100)
    private String reqCode;

    //专业ID
    @Column(name = "DP_ID", nullable = false, length = 12)
    private String dpId;

    //专业CODE
    @Column(name = "DP_CODE", nullable = false, length = 12)
    private String dpCode;

    //采购员
    @Column(name = "BUYER", nullable = false, length = 20)
    private String buyer;

    //SUPP
    @Column(name = "R_SUPP", nullable = false, length = 5)
    private String reqSupp;

    //DESCRIPTION
    @Column(name = "DESCRIPTION", nullable = true, length = 4000)
    private String description;

    //ORIGINATOR
    @Column(name = "ORIGINATOR", nullable = false, length = 20)
    private String originator;

    //RST_CODE
    @Column(name = "RST_CODE", nullable = true, length = 20)
    private String rstCode;

    //APPROVED_DATE
    @Column(name = "APPROVED_DATE", nullable = true, length = 20)
    private String approvedDate;

    //BOM_NO
    @Column(name = "BOM_NO", nullable = true, length = 500)
    private String bomNo;

    //请购原因
    @Column(name = "REQ_TYPE", nullable = true, length = 50)
    private String reqType;

    //CATEGORY
    @Column(name = "CATEGOR", nullable = true, length = 50)
    private String category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReqSupp() {
        return reqSupp;
    }

    public void setReqSupp(String reqSupp) {
        this.reqSupp = reqSupp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getRstCode() {
        return rstCode;
    }

    public void setRstCode(String rstCode) {
        this.rstCode = rstCode;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getBomNo() {
        return bomNo;
    }

    public void setBomNo(String bomNo) {
        this.bomNo = bomNo;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public String getEntitySubType() {
        return category;
    }

    public void setEntitySubType(String category) {
        this.category = category;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getReqCode() {
        return reqCode;
    }

    public void setReqCode(String reqCode) {
        this.reqCode = reqCode;
    }

    public String getDpId() {
        return dpId;
    }

    public void setDpId(String dpId) {
        this.dpId = dpId;
    }

    public String getDpCode() {
        return dpCode;
    }

    public void setDpCode(String dpCode) {
        this.dpCode = dpCode;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

}
