package com.ose.notifications.entity;

import com.ose.dto.OperatorDTO;
import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 通知批次数据实体。
 */
@Entity
@Table(name = "batches")
public class NotificationBatch extends NotificationBatchBase {

    private static final long serialVersionUID = 9021696412376066497L;

    @Schema(description = "参数数据（JSON）")
    @Lob
    @Column(name = "parameter_json", length = 40960)
    private String parameterJSON;

    @OneToOne
    @JoinColumn(updatable = false, insertable = false, name = "templateId", referencedColumnName = "id")
    private NotificationTemplate template;

    public NotificationBatch() {
        super();
        setCreatedAt(new Date());
    }

    public NotificationBatch(OperatorDTO operator, NotificationConfiguration configuration) {

        this();

        setOrgId(configuration.getOrgId());
        setProjectId(configuration.getProjectId());
        setType(configuration.getType());
        setTemplateId(configuration.getTemplateId());
        setAnnouncement(configuration.getAnnouncement());

        if (operator != null) {
            setCreatedBy(operator.getId());
            setCreatorName(operator.getName());
        }

    }

    public String getParameterJSON() {
        return parameterJSON;
    }

    public void setParameterJSON(String parameterJSON) {
        this.parameterJSON = parameterJSON;
    }

    public void setParameters(Object parameters) {
        if (parameters == null) {
            setParameterType(null);
            this.parameterJSON = null;
        } else {
            setParameterType(parameters.getClass().getTypeName());
            this.parameterJSON = StringUtils.toJSON(parameters);
        }
    }

    public NotificationTemplate getTemplate() {
        return template;
    }

    public void setTemplate(NotificationTemplate template) {
        this.template = template;
    }

}
