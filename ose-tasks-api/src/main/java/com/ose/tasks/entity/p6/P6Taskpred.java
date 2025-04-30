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
@Table(name="taskpred")
@Entity
public class P6Taskpred extends BaseDTO {
    /** ; */
    @Id
    private Integer taskPredId ;
    /** ; */
    private Integer taskId ;
    /** ; */
    private Integer predTaskId ;
    /** ; */
    private Integer projId ;
    /** ; */
    private Integer predProjId ;
    /** ; */
    private String predType ;
    /** ; */
    private Double lagHrCnt ;
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
    public Integer getTaskPredId(){
        return this.taskPredId;
    }
    /** ; */
    public void setTaskPredId(Integer taskPredId){
        this.taskPredId=taskPredId;
    }
    /** ; */
    public Integer getTaskId(){
        return this.taskId;
    }
    /** ; */
    public void setTaskId(Integer taskId){
        this.taskId=taskId;
    }
    /** ; */
    public Integer getPredTaskId(){
        return this.predTaskId;
    }
    /** ; */
    public void setPredTaskId(Integer predTaskId){
        this.predTaskId=predTaskId;
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
    public Integer getPredProjId(){
        return this.predProjId;
    }
    /** ; */
    public void setPredProjId(Integer predProjId){
        this.predProjId=predProjId;
    }
    /** ; */
    public String getPredType(){
        return this.predType;
    }
    /** ; */
    public void setPredType(String predType){
        this.predType=predType;
    }
    /** ; */
    public Double getLagHrCnt(){
        return this.lagHrCnt;
    }
    /** ; */
    public void setLagHrCnt(Double lagHrCnt){
        this.lagHrCnt=lagHrCnt;
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
