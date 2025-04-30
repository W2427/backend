package com.ose.tasks.entity.bpm;

import com.ose.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Date;

/**
 * bpm 执行错误日志
 */
@Entity
@Table(name = "bpm_error_log")
public class BpmError extends BaseEntity {

    private static final long serialVersionUID = 4295252718392516985L;
    //组织id
    private Long orgId;

    //项目id
    private Long projectId;

    //编号
    private String code;

    private Long actInstId;

    //下料时间
    private Date date;

    //备注
    private String memo;

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

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
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
}
