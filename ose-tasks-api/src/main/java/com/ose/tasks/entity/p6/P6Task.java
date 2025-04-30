package com.ose.tasks.entity.p6;

import com.ose.dto.BaseDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

/**
 * @author : http://www.chiner.pro
 * @date : 2021-12-7
 * @desc :
 */
@Table(name="TASK")
@Entity
public class P6Task extends BaseDTO {
    /** ; */
    @Id
    private Integer taskId ;
    /** ; */
    private Integer projId ;
    /** ; */
    private Integer wbsId ;
    /** ; */
    private Integer clndrId ;
    /** ; */
    private Double physCompletePct ;
    /** ; */
    private String revFdbkFlag ;
    /** ; */
    private String lockPlanFlag ;
    /** ; */
    private String autoComputeActFlag ;
    /** ; */
    private String completePctType ;
    /** ; */
    private String taskType ;
    /** ; */
    private String durationType ;
    /** ; */
    private String statusCode ;
    /** ; */
    private String taskCode ;
    /** ; */
    private String taskName ;
    /** ; */
    private Integer rsrcId ;
    /** ; */
    private Double totalFloatHrCnt ;
    /** ; */
    private Double freeFloatHrCnt ;
    /** ; */
    private Double remainDrtnHrCnt ;
    /** ; */
    private Double actWorkQty ;
    /** ; */
    private Double remainWorkQty ;
    /** ; */
    private Double targetWorkQty ;
    /** ; */
    private Double targetDrtnHrCnt ;
    /** ; */
    private Double targetEquipQty ;
    /** ; */
    private Double actEquipQty ;
    /** ; */
    private Double remainEquipQty ;
    /** ; */
    private Date cstrDate ;
    /** ; */
    private Date actStartDate ;
    /** ; */
    private Date actEndDate ;
    /** ; */
    private Date lateStartDate ;
    /** ; */
    private Date lateEndDate ;
    /** ; */
    private Date expectEndDate ;
    /** ; */
    private Date earlyStartDate ;
    /** ; */
    private Date earlyEndDate ;
    /** ; */
    private Date restartDate ;
    /** ; */
    private Date reendDate ;
    /** ; */
    private Date targetStartDate ;
    /** ; */
    private Date targetEndDate ;
    /** ; */
    private Date remLateStartDate ;
    /** ; */
    private Date remLateEndDate ;
    /** ; */
    private String cstrType ;
    /** ; */
    private String priorityType ;
    /** ; */
    private Date suspendDate ;
    /** ; */
    private Date resumeDate ;
    /** ; */
    private Integer floatPath ;
    /** ; */
    private Integer floatPathOrder ;
    /** ; */
    private String guid ;
    /** ; */
    private String tmplGuid ;
    /** ; */
    private Date cstrDate2 ;
    /** ; */
    private String cstrType2 ;
    /** ; */
    private String drivingPathFlag ;
    /** ; */
    private Double actThisPerWorkQty ;
    /** ; */
    private Double actThisPerEquipQty ;
    /** ; */
    private Date externalEarlyStartDate ;
    /** ; */
    private Date externalLateEndDate ;
    /** ; */
    private Integer locationId ;
    /** ; */
    private Double estWt ;
    /** ; */
    private Date createDate ;
    /** ; */
    private String createUser ;
    /** ; */
    private Date updateDate ;
    /** ; */
    private String updateUser ;
    /** ; */
    private Integer deleteSessionId ;
    /** ; */
    private Date deleteDate ;

    /** ; */
    public Integer getTaskId(){
        return this.taskId;
    }
    /** ; */
    public void setTaskId(Integer taskId){
        this.taskId=taskId;
    }
    /** ; */
    public Integer getProjId(){
        return this.projId;
    }
    /** ; */
    public void setProjId(Integer projId){
        this.projId=projId;
    }
    /** ; */
    public Integer getWbsId(){
        return this.wbsId;
    }
    /** ; */
    public void setWbsId(Integer wbsId){
        this.wbsId=wbsId;
    }
    /** ; */
    public Integer getClndrId(){
        return this.clndrId;
    }
    /** ; */
    public void setClndrId(Integer clndrId){
        this.clndrId=clndrId;
    }
    /** ; */
    public Double getPhysCompletePct(){
        return this.physCompletePct;
    }
    /** ; */
    public void setPhysCompletePct(Double physCompletePct){
        this.physCompletePct=physCompletePct;
    }
    /** ; */
    public String getRevFdbkFlag(){
        return this.revFdbkFlag;
    }
    /** ; */
    public void setRevFdbkFlag(String revFdbkFlag){
        this.revFdbkFlag=revFdbkFlag;
    }
    /** ; */
    public String getLockPlanFlag(){
        return this.lockPlanFlag;
    }
    /** ; */
    public void setLockPlanFlag(String lockPlanFlag){
        this.lockPlanFlag=lockPlanFlag;
    }
    /** ; */
    public String getAutoComputeActFlag(){
        return this.autoComputeActFlag;
    }
    /** ; */
    public void setAutoComputeActFlag(String autoComputeActFlag){
        this.autoComputeActFlag=autoComputeActFlag;
    }
    /** ; */
    public String getCompletePctType(){
        return this.completePctType;
    }
    /** ; */
    public void setCompletePctType(String completePctType){
        this.completePctType=completePctType;
    }
    /** ; */
    public String getTaskType(){
        return this.taskType;
    }
    /** ; */
    public void setTaskType(String taskType){
        this.taskType=taskType;
    }
    /** ; */
    public String getDurationType(){
        return this.durationType;
    }
    /** ; */
    public void setDurationType(String durationType){
        this.durationType=durationType;
    }
    /** ; */
    public String getStatusCode(){
        return this.statusCode;
    }
    /** ; */
    public void setStatusCode(String statusCode){
        this.statusCode=statusCode;
    }
    /** ; */
    public String getTaskCode(){
        return this.taskCode;
    }
    /** ; */
    public void setTaskCode(String taskCode){
        this.taskCode=taskCode;
    }
    /** ; */
    public String getTaskName(){
        return this.taskName;
    }
    /** ; */
    public void setTaskName(String taskName){
        this.taskName=taskName;
    }
    /** ; */
    public Integer getRsrcId(){
        return this.rsrcId;
    }
    /** ; */
    public void setRsrcId(Integer rsrcId){
        this.rsrcId=rsrcId;
    }
    /** ; */
    public Double getTotalFloatHrCnt(){
        return this.totalFloatHrCnt;
    }
    /** ; */
    public void setTotalFloatHrCnt(Double totalFloatHrCnt){
        this.totalFloatHrCnt=totalFloatHrCnt;
    }
    /** ; */
    public Double getFreeFloatHrCnt(){
        return this.freeFloatHrCnt;
    }
    /** ; */
    public void setFreeFloatHrCnt(Double freeFloatHrCnt){
        this.freeFloatHrCnt=freeFloatHrCnt;
    }
    /** ; */
    public Double getRemainDrtnHrCnt(){
        return this.remainDrtnHrCnt;
    }
    /** ; */
    public void setRemainDrtnHrCnt(Double remainDrtnHrCnt){
        this.remainDrtnHrCnt=remainDrtnHrCnt;
    }
    /** ; */
    public Double getActWorkQty(){
        return this.actWorkQty;
    }
    /** ; */
    public void setActWorkQty(Double actWorkQty){
        this.actWorkQty=actWorkQty;
    }
    /** ; */
    public Double getRemainWorkQty(){
        return this.remainWorkQty;
    }
    /** ; */
    public void setRemainWorkQty(Double remainWorkQty){
        this.remainWorkQty=remainWorkQty;
    }
    /** ; */
    public Double getTargetWorkQty(){
        return this.targetWorkQty;
    }
    /** ; */
    public void setTargetWorkQty(Double targetWorkQty){
        this.targetWorkQty=targetWorkQty;
    }
    /** ; */
    public Double getTargetDrtnHrCnt(){
        return this.targetDrtnHrCnt;
    }
    /** ; */
    public void setTargetDrtnHrCnt(Double targetDrtnHrCnt){
        this.targetDrtnHrCnt=targetDrtnHrCnt;
    }
    /** ; */
    public Double getTargetEquipQty(){
        return this.targetEquipQty;
    }
    /** ; */
    public void setTargetEquipQty(Double targetEquipQty){
        this.targetEquipQty=targetEquipQty;
    }
    /** ; */
    public Double getActEquipQty(){
        return this.actEquipQty;
    }
    /** ; */
    public void setActEquipQty(Double actEquipQty){
        this.actEquipQty=actEquipQty;
    }
    /** ; */
    public Double getRemainEquipQty(){
        return this.remainEquipQty;
    }
    /** ; */
    public void setRemainEquipQty(Double remainEquipQty){
        this.remainEquipQty=remainEquipQty;
    }
    /** ; */
    public Date getCstrDate(){
        return this.cstrDate;
    }
    /** ; */
    public void setCstrDate(Date cstrDate){
        this.cstrDate=cstrDate;
    }
    /** ; */
    public Date getActStartDate(){
        return this.actStartDate;
    }
    /** ; */
    public void setActStartDate(Date actStartDate){
        this.actStartDate=actStartDate;
    }
    /** ; */
    public Date getActEndDate(){
        return this.actEndDate;
    }
    /** ; */
    public void setActEndDate(Date actEndDate){
        this.actEndDate=actEndDate;
    }
    /** ; */
    public Date getLateStartDate(){
        return this.lateStartDate;
    }
    /** ; */
    public void setLateStartDate(Date lateStartDate){
        this.lateStartDate=lateStartDate;
    }
    /** ; */
    public Date getLateEndDate(){
        return this.lateEndDate;
    }
    /** ; */
    public void setLateEndDate(Date lateEndDate){
        this.lateEndDate=lateEndDate;
    }
    /** ; */
    public Date getExpectEndDate(){
        return this.expectEndDate;
    }
    /** ; */
    public void setExpectEndDate(Date expectEndDate){
        this.expectEndDate=expectEndDate;
    }
    /** ; */
    public Date getEarlyStartDate(){
        return this.earlyStartDate;
    }
    /** ; */
    public void setEarlyStartDate(Date earlyStartDate){
        this.earlyStartDate=earlyStartDate;
    }
    /** ; */
    public Date getEarlyEndDate(){
        return this.earlyEndDate;
    }
    /** ; */
    public void setEarlyEndDate(Date earlyEndDate){
        this.earlyEndDate=earlyEndDate;
    }
    /** ; */
    public Date getRestartDate(){
        return this.restartDate;
    }
    /** ; */
    public void setRestartDate(Date restartDate){
        this.restartDate=restartDate;
    }
    /** ; */
    public Date getReendDate(){
        return this.reendDate;
    }
    /** ; */
    public void setReendDate(Date reendDate){
        this.reendDate=reendDate;
    }
    /** ; */
    public Date getTargetStartDate(){
        return this.targetStartDate;
    }
    /** ; */
    public void setTargetStartDate(Date targetStartDate){
        this.targetStartDate=targetStartDate;
    }
    /** ; */
    public Date getTargetEndDate(){
        return this.targetEndDate;
    }
    /** ; */
    public void setTargetEndDate(Date targetEndDate){
        this.targetEndDate=targetEndDate;
    }
    /** ; */
    public Date getRemLateStartDate(){
        return this.remLateStartDate;
    }
    /** ; */
    public void setRemLateStartDate(Date remLateStartDate){
        this.remLateStartDate=remLateStartDate;
    }
    /** ; */
    public Date getRemLateEndDate(){
        return this.remLateEndDate;
    }
    /** ; */
    public void setRemLateEndDate(Date remLateEndDate){
        this.remLateEndDate=remLateEndDate;
    }
    /** ; */
    public String getCstrType(){
        return this.cstrType;
    }
    /** ; */
    public void setCstrType(String cstrType){
        this.cstrType=cstrType;
    }
    /** ; */
    public String getPriorityType(){
        return this.priorityType;
    }
    /** ; */
    public void setPriorityType(String priorityType){
        this.priorityType=priorityType;
    }
    /** ; */
    public Date getSuspendDate(){
        return this.suspendDate;
    }
    /** ; */
    public void setSuspendDate(Date suspendDate){
        this.suspendDate=suspendDate;
    }
    /** ; */
    public Date getResumeDate(){
        return this.resumeDate;
    }
    /** ; */
    public void setResumeDate(Date resumeDate){
        this.resumeDate=resumeDate;
    }
    /** ; */
    public Integer getFloatPath(){
        return this.floatPath;
    }
    /** ; */
    public void setFloatPath(Integer floatPath){
        this.floatPath=floatPath;
    }
    /** ; */
    public Integer getFloatPathOrder(){
        return this.floatPathOrder;
    }
    /** ; */
    public void setFloatPathOrder(Integer floatPathOrder){
        this.floatPathOrder=floatPathOrder;
    }
    /** ; */
    public String getGuid(){
        return this.guid;
    }
    /** ; */
    public void setGuid(String guid){
        this.guid=guid;
    }
    /** ; */
    public String getTmplGuid(){
        return this.tmplGuid;
    }
    /** ; */
    public void setTmplGuid(String tmplGuid){
        this.tmplGuid=tmplGuid;
    }
    /** ; */
    public Date getCstrDate2(){
        return this.cstrDate2;
    }
    /** ; */
    public void setCstrDate2(Date cstrDate2){
        this.cstrDate2=cstrDate2;
    }
    /** ; */
    public String getCstrType2(){
        return this.cstrType2;
    }
    /** ; */
    public void setCstrType2(String cstrType2){
        this.cstrType2=cstrType2;
    }
    /** ; */
    public String getDrivingPathFlag(){
        return this.drivingPathFlag;
    }
    /** ; */
    public void setDrivingPathFlag(String drivingPathFlag){
        this.drivingPathFlag=drivingPathFlag;
    }
    /** ; */
    public Double getActThisPerWorkQty(){
        return this.actThisPerWorkQty;
    }
    /** ; */
    public void setActThisPerWorkQty(Double actThisPerWorkQty){
        this.actThisPerWorkQty=actThisPerWorkQty;
    }
    /** ; */
    public Double getActThisPerEquipQty(){
        return this.actThisPerEquipQty;
    }
    /** ; */
    public void setActThisPerEquipQty(Double actThisPerEquipQty){
        this.actThisPerEquipQty=actThisPerEquipQty;
    }
    /** ; */
    public Date getExternalEarlyStartDate(){
        return this.externalEarlyStartDate;
    }
    /** ; */
    public void setExternalEarlyStartDate(Date externalEarlyStartDate){
        this.externalEarlyStartDate=externalEarlyStartDate;
    }
    /** ; */
    public Date getExternalLateEndDate(){
        return this.externalLateEndDate;
    }
    /** ; */
    public void setExternalLateEndDate(Date externalLateEndDate){
        this.externalLateEndDate=externalLateEndDate;
    }
    /** ; */
    public Integer getLocationId(){
        return this.locationId;
    }
    /** ; */
    public void setLocationId(Integer locationId){
        this.locationId=locationId;
    }
    /** ; */
    public Double getEstWt(){
        return this.estWt;
    }
    /** ; */
    public void setEstWt(Double estWt){
        this.estWt=estWt;
    }
    /** ; */
    public Date getCreateDate(){
        return this.createDate;
    }
    /** ; */
    public void setCreateDate(Date createDate){
        this.createDate=createDate;
    }
    /** ; */
    public String getCreateUser(){
        return this.createUser;
    }
    /** ; */
    public void setCreateUser(String createUser){
        this.createUser=createUser;
    }
    /** ; */
    public Date getUpdateDate(){
        return this.updateDate;
    }
    /** ; */
    public void setUpdateDate(Date updateDate){
        this.updateDate=updateDate;
    }
    /** ; */
    public String getUpdateUser(){
        return this.updateUser;
    }
    /** ; */
    public void setUpdateUser(String updateUser){
        this.updateUser=updateUser;
    }
    /** ; */
    public Integer getDeleteSessionId(){
        return this.deleteSessionId;
    }
    /** ; */
    public void setDeleteSessionId(Integer deleteSessionId){
        this.deleteSessionId=deleteSessionId;
    }
    /** ; */
    public Date getDeleteDate(){
        return this.deleteDate;
    }
    /** ; */
    public void setDeleteDate(Date deleteDate){
        this.deleteDate=deleteDate;
    }
}
