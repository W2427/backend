package com.ose.notifications.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 通知模版数据实体。
 */
@Entity
@Table(name = "templates")
public class NotificationTemplateBasic extends NotificationTemplateBase {

    private static final long serialVersionUID = -8024494930266564002L;

}
