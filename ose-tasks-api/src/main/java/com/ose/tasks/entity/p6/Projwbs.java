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
@Table(name="PROJWBS")
@Entity
public class Projwbs extends BaseDTO {
    private static final long serialVersionUID = 1128991238904802942L;
    /** ; */
    @Id
    private Integer wbsId ;
    /** ; */
    private Integer projId ;
    /** ; */
    private Integer obsId ;
    /** ; */
    private Integer seqNum ;
    /** ; */
    private String projNodeFlag ;
    /** ; */
    private String sumDataFlag ;
    /** ; */
    private String statusCode ;
    /** ; */
    private String wbsShortName ;
    /** ; */
    private String wbsName ;
    /** ; */
    private Integer phaseId ;
    /** ; */
    private Integer parentWbsId ;
    /** ; */
    private Integer evUserPct ;
    /** ; */
    private Double evEtcUserValue ;
    /** ; */
    private Double origCost ;
    /** ; */
    private Double indepRemainTotalCost ;
    /** ; */
    private Double annDscntRatePct ;
    /** ; */
    private String dscntPeriodType ;
    /** ; */
    private Double indepRemainWorkQty ;
    /** ; */
    private Date anticipStartDate ;
    /** ; */
    private Date anticipEndDate ;
    /** ; */
    private String evComputeType ;
    /** ; */
    private String evEtcComputeType ;
    /** ; */
    private String guid ;
    /** ; */
    private String tmplGuid ;
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
    public Integer getWbsId(){
        return this.wbsId;
    }
    /** ; */
    public void setWbsId(Integer wbsId){
        this.wbsId=wbsId;
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
    public Integer getObsId(){
        return this.obsId;
    }
    /** ; */
    public void setObsId(Integer obsId){
        this.obsId=obsId;
    }
    /** ; */
    public Integer getSeqNum(){
        return this.seqNum;
    }
    /** ; */
    public void setSeqNum(Integer seqNum){
        this.seqNum=seqNum;
    }
    /** ; */
    public String getProjNodeFlag(){
        return this.projNodeFlag;
    }
    /** ; */
    public void setProjNodeFlag(String projNodeFlag){
        this.projNodeFlag=projNodeFlag;
    }
    /** ; */
    public String getSumDataFlag(){
        return this.sumDataFlag;
    }
    /** ; */
    public void setSumDataFlag(String sumDataFlag){
        this.sumDataFlag=sumDataFlag;
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
    public String getWbsShortName(){
        return this.wbsShortName;
    }
    /** ; */
    public void setWbsShortName(String wbsShortName){
        this.wbsShortName=wbsShortName;
    }
    /** ; */
    public String getWbsName(){
        return this.wbsName;
    }
    /** ; */
    public void setWbsName(String wbsName){
        this.wbsName=wbsName;
    }
    /** ; */
    public Integer getPhaseId(){
        return this.phaseId;
    }
    /** ; */
    public void setPhaseId(Integer phaseId){
        this.phaseId=phaseId;
    }
    /** ; */
    public Integer getParentWbsId(){
        return this.parentWbsId;
    }
    /** ; */
    public void setParentWbsId(Integer parentWbsId){
        this.parentWbsId=parentWbsId;
    }
    /** ; */
    public Integer getEvUserPct(){
        return this.evUserPct;
    }
    /** ; */
    public void setEvUserPct(Integer evUserPct){
        this.evUserPct=evUserPct;
    }
    /** ; */
    public Double getEvEtcUserValue(){
        return this.evEtcUserValue;
    }
    /** ; */
    public void setEvEtcUserValue(Double evEtcUserValue){
        this.evEtcUserValue=evEtcUserValue;
    }
    /** ; */
    public Double getOrigCost(){
        return this.origCost;
    }
    /** ; */
    public void setOrigCost(Double origCost){
        this.origCost=origCost;
    }
    /** ; */
    public Double getIndepRemainTotalCost(){
        return this.indepRemainTotalCost;
    }
    /** ; */
    public void setIndepRemainTotalCost(Double indepRemainTotalCost){
        this.indepRemainTotalCost=indepRemainTotalCost;
    }
    /** ; */
    public Double getAnnDscntRatePct(){
        return this.annDscntRatePct;
    }
    /** ; */
    public void setAnnDscntRatePct(Double annDscntRatePct){
        this.annDscntRatePct=annDscntRatePct;
    }
    /** ; */
    public String getDscntPeriodType(){
        return this.dscntPeriodType;
    }
    /** ; */
    public void setDscntPeriodType(String dscntPeriodType){
        this.dscntPeriodType=dscntPeriodType;
    }
    /** ; */
    public Double getIndepRemainWorkQty(){
        return this.indepRemainWorkQty;
    }
    /** ; */
    public void setIndepRemainWorkQty(Double indepRemainWorkQty){
        this.indepRemainWorkQty=indepRemainWorkQty;
    }
    /** ; */
    public Date getAnticipStartDate(){
        return this.anticipStartDate;
    }
    /** ; */
    public void setAnticipStartDate(Date anticipStartDate){
        this.anticipStartDate=anticipStartDate;
    }
    /** ; */
    public Date getAnticipEndDate(){
        return this.anticipEndDate;
    }
    /** ; */
    public void setAnticipEndDate(Date anticipEndDate){
        this.anticipEndDate=anticipEndDate;
    }
    /** ; */
    public String getEvComputeType(){
        return this.evComputeType;
    }
    /** ; */
    public void setEvComputeType(String evComputeType){
        this.evComputeType=evComputeType;
    }
    /** ; */
    public String getEvEtcComputeType(){
        return this.evEtcComputeType;
    }
    /** ; */
    public void setEvEtcComputeType(String evEtcComputeType){
        this.evEtcComputeType=evEtcComputeType;
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
