package com.ose.notifications.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 通知记录数据实体。
 */
@Entity
@Table(name = "notifications")
public class NotificationBasic extends NotificationBase {

    private static final long serialVersionUID = -2375122498126234515L;

}
