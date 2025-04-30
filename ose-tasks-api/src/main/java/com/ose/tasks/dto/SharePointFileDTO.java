package com.ose.tasks.dto;

import com.ose.dto.PageDTO;

import java.util.Date;
import java.util.Map;

/**
 * @author: DaiZeFeng
 * @date: 2023/2/2
*/
public class SharePointFileDTO extends PageDTO {
    private static final long serialVersionUID = -2926138795976772506L;

    private Long orgId;

    private Long projectId;

    private String fileName;

    private Date modifiedAt;

    private String modifiedBy;

    private String type;

    private Map<String,Object> metadata;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
