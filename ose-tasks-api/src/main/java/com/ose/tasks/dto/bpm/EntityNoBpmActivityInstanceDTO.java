package com.ose.tasks.dto.bpm;

import java.util.Date;

import com.ose.dto.BaseDTO;

/**
 */
public class EntityNoBpmActivityInstanceDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7409483222999461504L;

    private Long id;

    private String entityNo;

    private String entityName;

    private String version;

    private Date entityType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Date getEntityType() {
        return entityType;
    }

    public void setEntityType(Date entityType) {
        this.entityType = entityType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


}
