package com.ose.tasks.entity.bpm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.vo.bpm.MailRunningStatus;
import com.ose.util.StringUtils;

import jakarta.persistence.*;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * GOE邮件人员。
 */
@Entity
@Table(name = "goe_mail_config")
public class GOEMailConfig extends BaseBizEntity {

    private static final long serialVersionUID = 8517357003235176194L;
    private Long orgId;

    private Long projectId;

    private String toMail;

    private String name;

    private String taskDefKey;

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

    public String getToMail() {
        return toMail;
    }

    public void setToMail(String toMail) {
        this.toMail = toMail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }
}

