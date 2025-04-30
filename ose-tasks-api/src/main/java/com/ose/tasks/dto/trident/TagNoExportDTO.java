package com.ose.tasks.dto.trident;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagNoExportDTO implements Serializable {

    private static final long serialVersionUID = -8209051568053149195L;
    private String no;

    private String entityType;

    private String entitySubType;

    private String pkg;

    private String subSystem;

    private String startStatus;

    private String submitStatus;

    private String signedStatus;

    private String closedStatus;

    private String description;

    private String memo;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(String subSystem) {
        this.subSystem = subSystem;
    }

    public String getStartStatus() {
        return startStatus;
    }

    public void setStartStatus(String startStatus) {
        this.startStatus = startStatus;
    }

    public String getSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(String submitStatus) {
        this.submitStatus = submitStatus;
    }

    public String getSignedStatus() {
        return signedStatus;
    }

    public void setSignedStatus(String signedStatus) {
        this.signedStatus = signedStatus;
    }

    public String getClosedStatus() {
        return closedStatus;
    }

    public void setClosedStatus(String closedStatus) {
        this.closedStatus = closedStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }
}
