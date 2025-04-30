package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 工作包与出库单关联表
 */
@Entity
@Table(name = "m_wp_to_fahs")
@NamedQuery(name = "WpToFahsEntity.findAll", query = "SELECT a FROM WpToFahsEntity a")
public class WpToFahsEntity extends BaseDTO {

    private static final long serialVersionUID = 1524902653017775556L;
    @Id
    @Column(name = "wptf_id", nullable = false)
    private Integer wptfId;

    @Column(name = "fah_id", nullable = false)
    private Integer fahId;

    @Column(name = "wp_id", nullable = false)
    private Integer wpId;

    @Column(name = "proj_id", nullable = false)
    private String projId;

    @Column(name = "order_seq", nullable = false)
    private Integer orderSeq;

    @Column(name = "dp_id", nullable = false)
    private Integer dpId;

    @Column(name = "priority_type", nullable = false)
    private String priorityType;

    @Column(name = "attr_id")
    private Integer attrId;

    @Column(name = "usr_id", nullable = false)
    private String usrId;

    @Column(name = "lmod", nullable = false)
    private Date lmod;

    @Column(name = "int_rev", nullable = false)
    private Integer intRev;

    @Column(name = "attr_sort")
    private String attrSort;

    @Column(name = "aqc1_id")
    private Integer aqc1Id;

    @Column(name = "aqc2_id")
    private Integer aqc2Id;

    @Column(name = "aqc3_id")
    private Integer aqc3Id;

    @Column(name = "aqc4_id")
    private Integer aqc4Id;

    @Column(name = "aqc5_id")
    private Integer aqc5Id;

    @Column(name = "ros_date")
    private Date rosDate;

    @Column(name = "end_date")
    private Date endDate;

    public Integer getWptfId() {
        return wptfId;
    }

    public void setWptfId(Integer wptfId) {
        this.wptfId = wptfId;
    }

    public Integer getFahId() {
        return fahId;
    }

    public void setFahId(Integer fahId) {
        this.fahId = fahId;
    }

    public Integer getWpId() {
        return wpId;
    }

    public void setWpId(Integer wpId) {
        this.wpId = wpId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public Integer getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Integer orderSeq) {
        this.orderSeq = orderSeq;
    }

    public Integer getDpId() {
        return dpId;
    }

    public void setDpId(Integer dpId) {
        this.dpId = dpId;
    }

    public String getPriorityType() {
        return priorityType;
    }

    public void setPriorityType(String priorityType) {
        this.priorityType = priorityType;
    }

    public Integer getAttrId() {
        return attrId;
    }

    public void setAttrId(Integer attrId) {
        this.attrId = attrId;
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public Date getLmod() {
        return lmod;
    }

    public void setLmod(Date lmod) {
        this.lmod = lmod;
    }

    public Integer getIntRev() {
        return intRev;
    }

    public void setIntRev(Integer intRev) {
        this.intRev = intRev;
    }

    public String getAttrSort() {
        return attrSort;
    }

    public void setAttrSort(String attrSort) {
        this.attrSort = attrSort;
    }

    public Integer getAqc1Id() {
        return aqc1Id;
    }

    public void setAqc1Id(Integer aqc1Id) {
        this.aqc1Id = aqc1Id;
    }

    public Integer getAqc2Id() {
        return aqc2Id;
    }

    public void setAqc2Id(Integer aqc2Id) {
        this.aqc2Id = aqc2Id;
    }

    public Integer getAqc3Id() {
        return aqc3Id;
    }

    public void setAqc3Id(Integer aqc3Id) {
        this.aqc3Id = aqc3Id;
    }

    public Integer getAqc4Id() {
        return aqc4Id;
    }

    public void setAqc4Id(Integer aqc4Id) {
        this.aqc4Id = aqc4Id;
    }

    public Integer getAqc5Id() {
        return aqc5Id;
    }

    public void setAqc5Id(Integer aqc5Id) {
        this.aqc5Id = aqc5Id;
    }

    public Date getRosDate() {
        return rosDate;
    }

    public void setRosDate(Date rosDate) {
        this.rosDate = rosDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
