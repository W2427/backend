package com.ose.notifications.entity;

import org.hibernate.annotations.Where;

import jakarta.persistence.*;

/**
 * 通知配置数据实体。
 */
@Entity
@Table(name = "configs")
public class NotificationConfiguration extends NotificationConfigurationBase {

    private static final long serialVersionUID = 8666878359893539692L;

    @OneToOne
    @JoinColumns({
        @JoinColumn(updatable = false, insertable = false, name = "orgId", referencedColumnName = "orgId"),
        @JoinColumn(updatable = false, insertable = false, name = "templateId", referencedColumnName = "id")
    })
    @Where(clause = "delete = 0")
    private NotificationTemplate template;

    public NotificationTemplate getTemplate() {
        return template;
    }

    public void setTemplate(NotificationTemplate template) {
        this.template = template;
    }

}


