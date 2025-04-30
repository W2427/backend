package com.ose.tasks.entity.bpm;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.bpm.ActInstFinishState;

/**
 * 交接单实体
 */
@Entity
@Table(name = "bpm_cutting")
public class BpmCutting extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -8635813266138087045L;

    //组织id
    private Long orgId;

    //项目id
    private Long projectId;

    //编号
    private String no;

    //名称
    private String name;

    //下料时间
    private Date date;

    //备注
    private String memo;

    private Long operator;

    @Transient
    private ActInstFinishState finishState;

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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Transient
    public ActInstFinishState getFinishState() {
        return finishState;
    }

    public void setFinishState(ActInstFinishState finishState) {
        this.finishState = finishState;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    @JsonProperty(value = "operator", access = JsonProperty.Access.READ_ONLY)
    public ReferenceData getOperatorReference() {
        return this.operator == null ? null : new ReferenceData(this.operator);
    }

    @Override
    public Set<Long> relatedUserIDs() {
        Set<Long> relatedUserIDs = new HashSet<>();
        relatedUserIDs.add(this.operator);
        return relatedUserIDs;
    }
}
