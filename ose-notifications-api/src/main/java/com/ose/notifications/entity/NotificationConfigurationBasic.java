package com.ose.notifications.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 通知配置数据实体。
 */
@Entity
@Table(name = "configs")
public class NotificationConfigurationBasic extends NotificationConfigurationBase {

    private static final long serialVersionUID = 1431239008290388473L;

    @Override
    @JsonIgnore
    public Long getTemplateId() {
        return super.getTemplateId();
    }

    @JsonProperty(value = "template", access = JsonProperty.Access.READ_ONLY)
    public ReferenceData getTemplate() {
        return new ReferenceData(getTemplateId());
    }

}

