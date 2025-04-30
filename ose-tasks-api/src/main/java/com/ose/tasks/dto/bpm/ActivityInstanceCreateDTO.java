package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;

/**
 * 实体管理 数据传输对象
 */
public class ActivityInstanceCreateDTO extends BaseDTO {


    private static final long serialVersionUID = -5910308947384928264L;

    private ContextDTO context;
    private Long orgId;
    private Long projectId;
    private OperatorDTO operator;
    private ActivityInstanceDTO actInstDTO;
    private String userAgent;
    private String authorization;

    public ContextDTO getContext() {
        return context;
    }

    public void setContext(ContextDTO context) {
        this.context = context;
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

    public OperatorDTO getOperator() {
        return operator;
    }

    public void setOperator(OperatorDTO operator) {
        this.operator = operator;
    }

    public ActivityInstanceDTO getActInstDTO() {
        return actInstDTO;
    }

    public void setActInstDTO(ActivityInstanceDTO actInstDTO) {
        this.actInstDTO = actInstDTO;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}
