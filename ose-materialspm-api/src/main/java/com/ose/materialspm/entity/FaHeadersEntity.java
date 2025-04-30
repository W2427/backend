package com.ose.materialspm.entity;

import com.ose.dto.BaseDTO;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 领料单
 */
@Entity
@Table(name = "m_fa_headers")
@NamedQuery(name = "FaHeadersEntity.findAll", query = "SELECT a FROM FaHeadersEntity a")
public class FaHeadersEntity extends BaseDTO {

    private static final long serialVersionUID = -7219303139417419668L;
    @Id
    @Column(name = "fah_id", nullable = false)
    private Integer fahId;

    @Column(name = "proj_id", nullable = false)
    private String projId;

    @Column(name = "fah_code", nullable = false)
    private String fahCode;

    @Column(name = "run_number", nullable = false)
    private Integer runNumber;

    @Column(name = "dp_id", nullable = false)
    private Integer dpId;

    @Column(name = "lst_id", nullable = false)
    private Integer lstId;

    @Column(name = "fah_type", nullable = false)
    private String fahType;

    @Column(name = "shortage", nullable = false)
    private String shortage;

    @Column(name = "job_status", nullable = false)
    private String jobStatus;

    @Column(name = "split_type", nullable = false)
    private String splitType;

    @Column(name = "all_positions_ind", nullable = false)
    private String allPositionsInd;

    @Column(name = "subst_ind", nullable = false)
    private String substInd;

    @Column(name = "best_quantity_ind", nullable = false)
    private String bestQuantityInd;

    @Column(name = "use_only_site_status_ind", nullable = false)
    private String useOnlySiteStatusInd;

    @Column(name = "user_interact_ind", nullable = false)
    private String userInteractInd;

    @Column(name = "weight_ind", nullable = false)
    private String weightInd;

    @Column(name = "cost_ind", nullable = false)
    private String costInd;

    @Column(name = "hour_ind", nullable = false)
    private String hourInd;

    @Column(name = "ignore_null_date_ind", nullable = false)
    private String ignoreNullDateInd;

    @Column(name = "mark_already_ri_ind", nullable = false)
    private String markAlreadyRiInd;

    @Column(name = "only_endnodes_ind", nullable = false)
    private String onlyEndnodesInd;

    @Column(name = "tag_no_option", nullable = false)
    private String tagNoOption;

    @Column(name = "exclude_mir_qty_ind", nullable = false)
    private String excludeMirQtyInd;

    @Column(name = "priority", nullable = false)
    private String priority;

    @Column(name = "usr_id", nullable = false)
    private String usrId;

    @Column(name = "lmod", nullable = false)
    private Date lmod;

    @Column(name = "int_rev", nullable = false)
    private Integer intRev;

    @Column(name = "parent_fah_id")
    private Integer parentFahId;

    @Column(name = "split_result")
    private String splitResult;

    @Column(name = "stat_id")
    private Integer statId;

    @Column(name = "attr_id")
    private Integer attrId;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "allocate_level", nullable = false)
    private String allocateLevel;

    @Column(name = "jcs_job_id")
    private Integer jcsJobId;

    @Column(name = "job_start_user")
    private String jobStartUser;

    @Column(name = "job_start_date")
    private Date jobStartDate;

    @Column(name = "job_end_date")
    private Date jobEndDate;

    @Column(name = "job_status_summary")
    private String jobStatusSummary;

    @Column(name = "mar_logfile_name")
    private String marLogfileName;

    @Column(name = "dtp_id")
    private Integer dtpId;

    @Column(name = "dd_id")
    private Integer ddId;

    @Column(name = "weight_attr_id")
    private Integer weightAttrId;

    @Column(name = "weight_option")
    private String weightOption;

    @Column(name = "emts_id")
    private Integer emtsId;

    @Column(name = "notification_time")
    private String notificationTime;

    public Integer getFahId() {
        return fahId;
    }

    public void setFahId(Integer fahId) {
        this.fahId = fahId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getFahCode() {
        return fahCode;
    }

    public void setFahCode(String fahCode) {
        this.fahCode = fahCode;
    }

    public Integer getRunNumber() {
        return runNumber;
    }

    public void setRunNumber(Integer runNumber) {
        this.runNumber = runNumber;
    }

    public Integer getDpId() {
        return dpId;
    }

    public void setDpId(Integer dpId) {
        this.dpId = dpId;
    }

    public Integer getLstId() {
        return lstId;
    }

    public void setLstId(Integer lstId) {
        this.lstId = lstId;
    }

    public String getFahType() {
        return fahType;
    }

    public void setFahType(String fahType) {
        this.fahType = fahType;
    }

    public String getShortage() {
        return shortage;
    }

    public void setShortage(String shortage) {
        this.shortage = shortage;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getSplitType() {
        return splitType;
    }

    public void setSplitType(String splitType) {
        this.splitType = splitType;
    }

    public String getAllPositionsInd() {
        return allPositionsInd;
    }

    public void setAllPositionsInd(String allPositionsInd) {
        this.allPositionsInd = allPositionsInd;
    }

    public String getSubstInd() {
        return substInd;
    }

    public void setSubstInd(String substInd) {
        this.substInd = substInd;
    }

    public String getBestQuantityInd() {
        return bestQuantityInd;
    }

    public void setBestQuantityInd(String bestQuantityInd) {
        this.bestQuantityInd = bestQuantityInd;
    }

    public String getUseOnlySiteStatusInd() {
        return useOnlySiteStatusInd;
    }

    public void setUseOnlySiteStatusInd(String useOnlySiteStatusInd) {
        this.useOnlySiteStatusInd = useOnlySiteStatusInd;
    }

    public String getUserInteractInd() {
        return userInteractInd;
    }

    public void setUserInteractInd(String userInteractInd) {
        this.userInteractInd = userInteractInd;
    }

    public String getWeightInd() {
        return weightInd;
    }

    public void setWeightInd(String weightInd) {
        this.weightInd = weightInd;
    }

    public String getCostInd() {
        return costInd;
    }

    public void setCostInd(String costInd) {
        this.costInd = costInd;
    }

    public String getHourInd() {
        return hourInd;
    }

    public void setHourInd(String hourInd) {
        this.hourInd = hourInd;
    }

    public String getIgnoreNullDateInd() {
        return ignoreNullDateInd;
    }

    public void setIgnoreNullDateInd(String ignoreNullDateInd) {
        this.ignoreNullDateInd = ignoreNullDateInd;
    }

    public String getMarkAlreadyRiInd() {
        return markAlreadyRiInd;
    }

    public void setMarkAlreadyRiInd(String markAlreadyRiInd) {
        this.markAlreadyRiInd = markAlreadyRiInd;
    }

    public String getOnlyEndnodesInd() {
        return onlyEndnodesInd;
    }

    public void setOnlyEndnodesInd(String onlyEndnodesInd) {
        this.onlyEndnodesInd = onlyEndnodesInd;
    }

    public String getTagNoOption() {
        return tagNoOption;
    }

    public void setTagNoOption(String tagNoOption) {
        this.tagNoOption = tagNoOption;
    }

    public String getExcludeMirQtyInd() {
        return excludeMirQtyInd;
    }

    public void setExcludeMirQtyInd(String excludeMirQtyInd) {
        this.excludeMirQtyInd = excludeMirQtyInd;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
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

    public Integer getParentFahId() {
        return parentFahId;
    }

    public void setParentFahId(Integer parentFahId) {
        this.parentFahId = parentFahId;
    }

    public String getSplitResult() {
        return splitResult;
    }

    public void setSplitResult(String splitResult) {
        this.splitResult = splitResult;
    }

    public Integer getStatId() {
        return statId;
    }

    public void setStatId(Integer statId) {
        this.statId = statId;
    }

    public Integer getAttrId() {
        return attrId;
    }

    public void setAttrId(Integer attrId) {
        this.attrId = attrId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getAllocateLevel() {
        return allocateLevel;
    }

    public void setAllocateLevel(String allocateLevel) {
        this.allocateLevel = allocateLevel;
    }

    public Integer getJcsJobId() {
        return jcsJobId;
    }

    public void setJcsJobId(Integer jcsJobId) {
        this.jcsJobId = jcsJobId;
    }

    public String getJobStartUser() {
        return jobStartUser;
    }

    public void setJobStartUser(String jobStartUser) {
        this.jobStartUser = jobStartUser;
    }

    public Date getJobStartDate() {
        return jobStartDate;
    }

    public void setJobStartDate(Date jobStartDate) {
        this.jobStartDate = jobStartDate;
    }

    public Date getJobEndDate() {
        return jobEndDate;
    }

    public void setJobEndDate(Date jobEndDate) {
        this.jobEndDate = jobEndDate;
    }

    public String getJobStatusSummary() {
        return jobStatusSummary;
    }

    public void setJobStatusSummary(String jobStatusSummary) {
        this.jobStatusSummary = jobStatusSummary;
    }

    public String getMarLogfileName() {
        return marLogfileName;
    }

    public void setMarLogfileName(String marLogfileName) {
        this.marLogfileName = marLogfileName;
    }

    public Integer getDtpId() {
        return dtpId;
    }

    public void setDtpId(Integer dtpId) {
        this.dtpId = dtpId;
    }

    public Integer getDdId() {
        return ddId;
    }

    public void setDdId(Integer ddId) {
        this.ddId = ddId;
    }

    public Integer getWeightAttrId() {
        return weightAttrId;
    }

    public void setWeightAttrId(Integer weightAttrId) {
        this.weightAttrId = weightAttrId;
    }

    public String getWeightOption() {
        return weightOption;
    }

    public void setWeightOption(String weightOption) {
        this.weightOption = weightOption;
    }

    public Integer getEmtsId() {
        return emtsId;
    }

    public void setEmtsId(Integer emtsId) {
        this.emtsId = emtsId;
    }

    public String getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }
}
